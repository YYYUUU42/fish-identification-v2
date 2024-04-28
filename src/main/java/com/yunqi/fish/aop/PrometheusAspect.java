package com.yunqi.fish.aop;

import com.alibaba.fastjson.JSON;
import com.yunqi.fish.common.AppHttpCodeEnum;
import com.yunqi.fish.common.MethodEnum;
import com.yunqi.fish.common.ResponseResult;
import com.yunqi.fish.manager.ResponseTimeManager;
import com.yunqi.fish.model.entity.ClassAndMethod;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 监控接口的响应时长
 */
@Component
@Aspect
@RequiredArgsConstructor
public class PrometheusAspect {

	/**
	 * 定义注册器
	 */
	final MeterRegistry registry;
	private final ResponseTimeManager responseTimeManager;

	private Counter counter_all_total;
	private ConcurrentHashMap<String, Counter> counterMap = new ConcurrentHashMap();

	private final Logger logger = LoggerFactory.getLogger(PrometheusAspect.class);

	ThreadLocal<Long> startTime = new ThreadLocal<>();

	/**
	 * 监控controller层的接口
	 */
	@Pointcut("@annotation(com.yunqi.fish.anno.Time)")
	private void pointCut() {
	}

	/**
	 * 启动时初始化指标，所有接口的请求次数统计
	 */
	@PostConstruct
	public void init() {
		counter_all_total = registry.counter("aop_all_requests_count", "aop_all_method", "count");
	}

	/**
	 * 有些业务场景也可以在这里监控接口
	 *
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Before("pointCut()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		Counter counter_total = null;

		String classMethod = getClassMethodName(joinPoint);
		if (counterMap.containsKey(classMethod)) {
			counter_total = counterMap.get(classMethod);
		} else {
			counter_total = registry.counter("aop_requests_count", "aop_method", classMethod);
			counterMap.put(classMethod, counter_total);
		}
		startTime.set(System.currentTimeMillis());
		counter_total.increment();
		counter_all_total.increment();
	}

	/**
	 * 获取请求的接口名
	 *
	 * @param joinPoint
	 * @return
	 */
	private String getClassMethodName(JoinPoint joinPoint) {
		String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
		String className = declaringTypeName.substring(declaringTypeName.lastIndexOf(".") + 1);
		return className + "." + joinPoint.getSignature().getName();
	}

	/**
	 * 获取请求的参数
	 *
	 * @param joinPoint
	 * @return
	 */
	private String getMethodArgs(ProceedingJoinPoint joinPoint) {
		// 构造参数组集合
		List<Object> argList = new ArrayList<>();
		for (Object arg : joinPoint.getArgs()) {
			// request/response无法使用toJSON
			if (arg instanceof HttpServletRequest) {
				argList.add("request");
			} else if (arg instanceof HttpServletResponse) {
				argList.add("response");
			} else if (arg instanceof MultipartFile) {
				argList.add("file");
			} else {
				argList.add(JSON.toJSON(arg));
			}
		}
		return JSON.toJSON(argList).toString();
	}

	/**
	 * 每个接口的耗时数据采集
	 *
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
//	@Around(value = "pointCut()")
//	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//		/**
//		 * 改服务请求的总次数
//		 */
//		counter_all_total.increment();
//		String classMethod = getClassMethodName(joinPoint);
//		Timer timer = Metrics.timer("pc_reponse_usedtime", "method_name", classMethod);
//		//该服务中每个接口请求过程没有抛异常的次数、最大响应时间、总的请求时间
//		Object result = timer.recordCallable(() -> {
//			try {
//				Object proceed = joinPoint.proceed();
//				//该服务中每个接口请求后返回错误码的次数
//				if (proceed instanceof ResponseResult) {
//					ResponseResult responseResult = (ResponseResult) proceed;
//					if (responseResult.getCode() != 200) {
//						Counter reponse_error_total = registry.counter("reponse_error_count", "aop_method", classMethod);
//						reponse_error_total.increment();
//						String methodArgs = getMethodArgs(joinPoint);
//						logger.info("接口返回错误码[{}],接口返回错误信息[{}],错误接口名是[{}],所传参数[{}]", responseResult.getCode(), responseResult.getMessage(), classMethod, methodArgs);
//					}
//				}
//				return proceed;
//			} catch (Throwable e) {
//				//该服务中每个接口请求过程中抛异常的次数
//				Counter reponse_throw_total = registry.counter("reponse_throw_count", "aop_method", classMethod);
//				reponse_throw_total.increment();
//				String methodArgs = getMethodArgs(joinPoint);
//				logger.info("接口抛异常方法名[{}]，所传参数[{}]", classMethod, methodArgs);
//				return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
//			}
//		});
//		return result;
//	}

	@AfterReturning(returning = "returnVal", pointcut = "pointCut()")
	public void doAftereReturning(JoinPoint joinPoint, Object returnVal) {
		AtomicLong app_reponse_usedtime = null;
		app_reponse_usedtime = registry.gauge("reponse_usedtime", new AtomicLong(0));
		app_reponse_usedtime.set((System.currentTimeMillis() - startTime.get()));

		long responseTime = System.currentTimeMillis() - startTime.get();
		String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
		String className = declaringTypeName.substring(declaringTypeName.lastIndexOf(".") + 1);
		String methodName = joinPoint.getSignature().getName();
		String logInfo = String.format("%s 类的 %s 方法，请求执行时间：{} ms", className,methodName);
		logger.info(logInfo, responseTime);

		responseTimeManager.timeoutProcessing(new ClassAndMethod(className,methodName,responseTime));
	}

	/**
	 * 接口抛异常时走这里
	 *
	 * @param joinPoint
	 * @param exception
	 */
	@AfterThrowing(value = "pointCut()", throwing = "exception")
	public void logTestAfterReturing3(JoinPoint joinPoint, Throwable exception) {
		logger.info("采集接口访问次数报错的接口名[{}],类名[{}]", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
		logger.info("采集接口访问信息报错日志{}", exception.getStackTrace());
		exception.printStackTrace();
	}
}
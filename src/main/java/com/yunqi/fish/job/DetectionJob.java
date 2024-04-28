package com.yunqi.fish.job;

import com.alibaba.fastjson.JSONObject;
import com.yunqi.fish.utils.MD5;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;

import static com.yunqi.fish.common.constant.CommonConstant.IDENTIFICATION_URL;

/**
 * 定时检测算法调用接口是否能正常使用
 */
@Component
@RequiredArgsConstructor
public class DetectionJob {

    private final StringRedisTemplate stringRedisTemplate;

    private final Logger logger = LoggerFactory.getLogger(DetectionJob.class);


    /**
     * 每八小时调用一次算法，判断接口是否还能正确使用
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0 0/8 * * ?")
    public void algorithmDetection() throws Exception {
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        path = URLDecoder.decode(path, "utf-8");
        String filePath = path + "static/detectionImage.jpg";

        MultipartFile file = getMultipartFile(filePath);

        String redisKey = String.format("image:" + "%s", MD5.encrypt(file));

        //先删除缓存，直接调用接口
        if (stringRedisTemplate.hasKey(redisKey)) {
            stringRedisTemplate.delete(redisKey);
        }

        postImage(filePath);
    }

    /**
     * 根据文件路径得到 MultipartFile 对象
     *
     * @param filePath
     * @return
     */
    private MultipartFile getMultipartFile(String filePath) {
        File f = new File(filePath);
        String fieldName = f.getName();
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("file", "application/sql", false, fieldName);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(f);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MultipartFile mfile = new CommonsMultipartFile(item);
        return mfile;
    }

    /**
     * 发送 post 请求
     *
     * @param filePath
     * @throws Exception
     */
    private void postImage(String filePath) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String openid = "test";
        File file = new File(filePath);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .addFormDataPart("openid", openid)
                .build();

        Request request = new Request.Builder()
                .url(IDENTIFICATION_URL)
                .post(requestBody)
                .addHeader("Content-Type", "multipart/form-data")
                .addHeader("Accept", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        logger.info("响应结果为：{}", JSONObject.parseObject(responseBody));
    }

}

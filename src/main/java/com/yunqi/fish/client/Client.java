package com.yunqi.fish.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 调用 python 端算法，得到运行结果
 */
public class Client {
	static final String HOST = "127.0.0.1";
	static final int PORT = 50007;

	private static final Logger logger = LoggerFactory.getLogger(Client.class);

	/**
	 * 客户端调用算法
	 *
	 * @param option1
	 * @param option2
	 * @return
	 * @throws IOException
	 */
	public static String exc(String option1, String option2){
		try {
			Socket client = new Socket(HOST, PORT);
			OutputStreamWriter os = new OutputStreamWriter(client.getOutputStream());
			StringBuilder sb = new StringBuilder();
			// 命令以空格隔开
			sb.append(option1 + " " + option2);
			os.write(sb.toString());
			os.flush();

			//得到运行完算法后，返回结果所在的目录
			String exp = getExp(client.getInputStream());

			return exp;
		} catch (IOException e) {
			logger.error("获取算法返回结果目录异常", e);
		}
		throw new RuntimeException();
	}

	/**
	 * 得到运行完算法后，返回结果所在的目录
	 *
	 * @param inputStream
	 * @return
	 */
	public static String getExp(InputStream inputStream) {
		StringBuilder contentBuilder = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				contentBuilder.append(line).append('\n');
			}

			// 确保内容不为空，防止 IndexOutOfBoundsException
			if (contentBuilder.length() > 0) {
				int index1 = contentBuilder.lastIndexOf("detect") + 7;
				int index2 = contentBuilder.lastIndexOf("labels");

				// 检查 index1 和 index2 的有效性
				if (index1 > 0 && index2 > index1) {
					return contentBuilder.substring(index1, index2).trim();
				}
			}

		} catch (IOException e) {
			logger.error("获取算法返回结果目录异常",e);
		}

		// 如果发生异常或未找到有效区间，返回空字符串或其他默认值
		return "";
	}
}

package com.yunqi.fish.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5 {


	public static String encrypt(MultipartFile image) throws IOException {
		char hexChars[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'a', 'b', 'c', 'd', 'e', 'f'};

		try {
			byte[] bytes = image.getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bytes);
			bytes = md.digest();
			int j = bytes.length;
			char[] chars = new char[j * 2];
			int k = 0;
			for (int i = 0; i < bytes.length; i++) {
				byte b = bytes[i];
				chars[k++] = hexChars[b >>> 4 & 0xf];
				chars[k++] = hexChars[b & 0xf];
			}
			return new String(chars);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException("MD5加密出错！！+" + e);
		}
	}

	public static String computeMD5(File file) {
		DigestInputStream din = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			//第一个参数是一个输入流
			din = new DigestInputStream(new BufferedInputStream(new FileInputStream(file)), md5);

			byte[] b = new byte[1024];
			while (din.read(b) != -1) ;

			byte[] digest = md5.digest();

			StringBuilder result = new StringBuilder(file.getName());
			result.append(": ");
			result.append(DatatypeConverter.printHexBinary(digest));
			return result.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (din != null) {
					din.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}

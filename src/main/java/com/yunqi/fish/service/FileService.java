package com.yunqi.fish.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;


public interface FileService {

	/**
	 * 删除指定目录中的文件
	 * @param file
	 */
	void deleteFilesInDirectory(File file);

	/**
	 * 将图片存储到指定目录中,并返回图片外链
	 * @param file
	 * @return
	 */
	String imageUpload(MultipartFile file);

	/**
	 * 在nginx中创建图片，并且返回图片的地址
	 * @param path
	 * @return
	 */
	String returnImageLink(String path);
}

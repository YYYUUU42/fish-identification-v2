package com.yunqi.fish.service.impl;

import com.yunqi.fish.service.FileService;
import com.yunqi.fish.utils.HashUtil;
import com.yunqi.fish.utils.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import static com.yunqi.fish.common.constant.CommonConstant.*;

@Service
public class FileServiceImpl implements FileService {

	private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	/**
	 * 删除指定文件下的所有文件
	 *
	 * @param file
	 */
	@Override
	public void deleteFilesInDirectory(File file) {
		//判断文件不为null或文件目录存在
		if (file == null || !file.exists()) {
			logger.info("文件删除失败,请检查文件路径是否正确");
			return;
		}

		//取得这个目录下的所有子文件对象
		File[] files = file.listFiles();
		//遍历该目录下的文件对象
		for (File f : files) {
			//判断子目录是否存在子目录,如果是文件则删除
			if (f.isDirectory()) {
				deleteFilesInDirectory(f);
			} else {
				f.delete();
			}
		}
	}

	/**
	 * 将图片存储到指定目录中,并返回图片外链
	 *
	 * @param file
	 */
	@Override
	public String imageUpload(MultipartFile file) {
		String filename = file.getOriginalFilename();

		//待检测的图片存放的位置
		String realFile = IMG_PATH;
		File file1 = new File(realFile);

		//如果存在，则必须清除里面的图片后才能放进新的图片
		deleteFilesInDirectory(file1);

		File file2 = new File(realFile + File.separator + filename);
		try {
			file.transferTo(file2);
			logger.info("图片上传成功");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnImageLink(realFile);
	}

	/**
	 * 在nginx中创建图片，并且返回图片的地址
	 *
	 * @param path
	 * @return
	 */
	@Override
	public String returnImageLink(String path) {
		File dir = new File(path);

		File[] files = dir.listFiles(File::isFile);
		File file = files != null && files.length > 0 ? files[0] : null;
		String fileName = files != null && files.length > 0 ? files[0].getName() : "";

		String hash = HashUtil.hashToBase62(MD5.computeMD5(file));
		String newPath = hash + "." + fileName.substring(fileName.lastIndexOf(".") + 1);

		try {
			FileInputStream fis = new FileInputStream(path + "\\" + fileName);
			FileOutputStream fos = new FileOutputStream(NGINX_PATH + "\\" + newPath);

			byte[] bytes = new byte[100];
			int temp = 0;
			while ((temp = fis.read(bytes)) > 0) {
				fos.write(bytes, 0, temp);
			}

			fos.flush();
			fis.close();
			fos.close();

		} catch (Exception e) {
			logger.error("存储图片报错", e);
		}

		return NGINX_IMG + newPath;
	}
}

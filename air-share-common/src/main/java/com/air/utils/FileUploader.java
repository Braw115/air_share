package com.air.utils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.air.pojo.vo.RespVo;

import io.minio.MinioClient;

public class FileUploader {
	//private static final String url = "http://127.0.0.1:9000";
	private static final String url = "http://cloud.szfriendly.com:9000";
	private static final String username = "minio";
	private static final String password = "abcde12345";
	private static final List<String> format =new ArrayList<String>(Arrays.asList("jpg","jpeg","gif","png","bmp"));
	
	
	public static MinioClient getMinioClient() throws Exception {
		// Create a minioClient with the Minio Server name, Port, Access key and Secret
		// key.
		MinioClient minioClient = new MinioClient(url, username, password);

		// Check if the bucket already exists.
		boolean isExist = minioClient.bucketExists("airshare");
		if (isExist) {
			System.out.println("Bucket already exists.");
		} else {
			// Make a new bucket called asiatrip to hold a zip file of photos.
			minioClient.makeBucket("airshare");
		}
		return minioClient;

	}

	public static RespVo<String> upload(List<MultipartFile> photo) {
		String img = null;
		for (MultipartFile file : photo) {
			// 获取图片的文件名
			String fileName = file.getOriginalFilename();
			// 获取图片的扩展名
			String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
			//限制大小
			if(file.getSize() >= 1048576*10)  //10M
				return new RespVo(false,"errorSize");
			//限制格式
			boolean flag = true;
			for(String f : format) {
				if(f.equals(extensionName)) {
					flag = true;
					break;
				}
				flag = false;
			}
			if(flag == false)
				return new RespVo(false,"errorFormat");		//不支持此格式上传
			// 新的图片文件名 = 获取时间戳+"."图片扩展名
			String newFileName = String.valueOf(System.currentTimeMillis()) + "." + extensionName;
			Long s = System.currentTimeMillis() % 100;
			newFileName = s + "/" + newFileName;
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(file.getBytes());
				FileUploader.getMinioClient().putObject("airshare"
						+ "", newFileName, bais, bais.available(),
						"application/octet-stream");
				bais.close();
				// System.out.println("myobject is uploaded successfully");
				if(img == null)
					img = newFileName;
				else
					img = img +","+newFileName;	//用','做分隔符
			} catch (Exception e) {
				return new RespVo(false,"fail");
			}
		}
		return new RespVo<>(true,img);
	}
}
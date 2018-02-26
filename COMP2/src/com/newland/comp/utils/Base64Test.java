package com.newland.comp.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

import android.util.Base64;

public class Base64Test {

	public static void main(String[] args) {
		// String strImg = GetImageStr();
		// System.out.println(strImg);
		// GenerateImage(strImg);
	}

	// 图片转化成base64字符串
	public static String GetImageStr(String imgPath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

		return Base64.encodeToString(image2byte(imgPath), Base64.DEFAULT);

	}

	public static byte[] image2byte(String path) {
		byte[] data = null;
		FileInputStream input = null;
		try {
			input = new FileInputStream(new File(path));
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int numBytesRead = 0;
			while ((numBytesRead = input.read(buf)) != -1) {
				output.write(buf, 0, numBytesRead);
			}
			data = output.toByteArray();
			output.close();
			input.close();
		} catch (FileNotFoundException ex1) {
			ex1.printStackTrace();
		} catch (IOException ex1) {
			ex1.printStackTrace();
		}
		return data;
	}

	public static String getStringEncode(String s) {
	
		if (StringUtil.isEmpty(s)) {
			return s;
		}
		byte[] data = s.getBytes();
		return Base64.encodeToString(data, Base64.DEFAULT);
	}
}

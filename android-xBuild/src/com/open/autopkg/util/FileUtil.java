package com.open.autopkg.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

	public static void copyFile(String sourceFile, String targetFile){
		copyFile(new File(sourceFile), new File(targetFile));
	}

	public static void emptyFolder(String folder) {
		emptyFile(new File(folder));
	}

	/**
	 * 复制文件
	 */
	public static void copyFile(File sourceFile, File targetFile) {
		
		try {
				BufferedInputStream inBuff = null;
				BufferedOutputStream outBuff = null;
				try {
					inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
					outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
	
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = inBuff.read(b)) != -1) {
						outBuff.write(b, 0, len);
					}
					outBuff.flush();
				} finally {
					if (inBuff != null)
						inBuff.close();
					if (outBuff != null)
						outBuff.close();
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 删除文件全部内容
	 * 
	 * @param file
	 */
	public static void emptyFile(File file) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write("");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
	
	public static String checkDir(String dir) {
		if(dir==null)
		{
			return "";
		}
		else if(!new File(dir).isDirectory())
		{
			dir="";
		}
		return dir;
	}
	
	public static boolean createFile(String filePath)
	{
		try
		{
			File file = new File(filePath);
			if (!file.exists())
			{
				if (!file.getParentFile().exists())
				{
					file.getParentFile().mkdirs();
				}

				return file.createNewFile();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	
	public static boolean createDir(String filePath)
	{
		try
		{
			File file = new File(filePath);
			file.mkdirs();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean deleteFile(String filePath)
	{
		try {
			File file = new File(filePath);
			if (file.exists())
			{
				return file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}

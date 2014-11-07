package com.open.autopkg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * Zip文件工具类
 * 
 * @author Luxh
 */
public class ZipFileUtil {
	
	static Text text_logs;

	/**
	 * 把文件压缩成zip格式
	 * 
	 * @param files
	 *            需要压缩的文件
	 * @param zipFilePath
	 *            压缩后的zip文件路径 ,如"D:/test/aa.zip";
	 */
	public static boolean compressFile(String srcPath, String desPath, String desName, Text Logs) throws IOException {
		text_logs = Logs;
		File srcFile = new File(srcPath);
		if(!srcFile.exists()){
			appendLogs(srcPath+"源文件不存在！\r\n");
			return false;
		}
		if ("".equalsIgnoreCase(desName)) {
			desName = srcFile.getName();
		}
		ZipArchiveOutputStream zipOutputStream = null;
		try{
			zipOutputStream = new ZipArchiveOutputStream(new FileOutputStream(desPath + "/" + desName));
			zipOutputStream.setUseZip64(Zip64Mode.AsNeeded);
	
			compressFiles2ZipBase(srcFile,	zipOutputStream, "");
		}finally{
			if(zipOutputStream!=null){
				zipOutputStream.close();
				zipOutputStream = null;
			}
		}
		return true;
	}
	
	public static void compressFiles2ZipBase(File file, ZipArchiveOutputStream zaos, String base) throws IOException {
		if (file != null) {
			if(base.equals("")){
				base = file.getName();
			}else{
				base = base + File.separator + file.getName();
			}
			if(file.isDirectory()){
					
				for(File f:file.listFiles()){
					compressFiles2ZipBase(f, zaos, base);
				}
			}else{
				ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, base);
				zaos.putArchiveEntry(zipArchiveEntry);
				InputStream is = null;
				try {
					is = new FileInputStream(file);
					byte[] buffer = new byte[1024 * 5];
					int len = -1;
					while ((len = is.read(buffer)) != -1) {
						// 把缓冲区的字节写入到ZipArchiveEntry
						zaos.write(buffer, 0, len);
					}
					// Writes all necessary data for this entry.
					zaos.closeArchiveEntry();
				}finally {
					if (is != null){
						is.close();
						is = null;
					}
				}
			}
		}
	}
	

	/**
	 * 把zip文件解压到指定的文件夹
	 * 
	 * @param zipFilePath
	 *            zip文件路径, 如 "D:/test/aa.zip"
	 * @param saveFileDir
	 *            解压后的文件存放路径, 如"D:/test/"
	 */
	public static void decompressZip(String zipFilePath, String saveFileDir) {
		if (isEndsWithZip(zipFilePath)) {
			File file = new File(zipFilePath);
			if (file.exists()) {
				InputStream is = null;
				// can read Zip archives
				ZipArchiveInputStream zais = null;
				try {
					is = new FileInputStream(file);
					zais = new ZipArchiveInputStream(is);
					ArchiveEntry archiveEntry = null;
					// 把zip包中的每个文件读取出来
					// 然后把文件写到指定的文件夹
					while ((archiveEntry = zais.getNextEntry()) != null) {
						// 获取文件名
						String entryFileName = archiveEntry.getName();
						// 构造解压出来的文件存放路径
						String entryFilePath = saveFileDir + entryFileName;
						byte[] content = new byte[(int) archiveEntry.getSize()];
						zais.read(content);
						OutputStream os = null;
						try {
							// 把解压出来的文件写到指定路径
							File entryFile = new File(entryFilePath);
							os = new FileOutputStream(entryFile);
							os.write(content);
						} catch (IOException e) {
							throw new IOException(e);
						} finally {
							if (os != null) {
								os.flush();
								os.close();
							}
						}

					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					try {
						if (zais != null) {
							zais.close();
						}
						if (is != null) {
							is.close();
						}
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	/**
	 * 判断文件名是否以.zip为后缀
	 * 
	 * @param fileName
	 *            需要判断的文件名
	 * @return 是zip文件返回true,否则返回false
	 */
	public static boolean isEndsWithZip(String fileName) {
		boolean flag = false;
		if (fileName != null && !"".equals(fileName.trim())) {
			if (fileName.endsWith(".ZIP") || fileName.endsWith(".zip")) {
				flag = true;
			}
		}
		return flag;
	}
	
	private static void appendLogs(final String logs){
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				text_logs.append(logs);
			}
		});
	}

}

package com.open.autopkg.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.open.autopkg.util.FileUtil;
import com.open.autopkg.xml.DiffApkConfigParser;
import com.open.autopkg.xml.DiffApkConfigXmlModify;

public class DiffApkConfig 
{
	
	public String diff_apkOldPath;
	public String diff_apkNewPath;
	public String diff_apkPatchPath;
	
	public String merge_apkOldPath;
	public String merge_apkNewPath;
	public String merge_apkPatchPath;
	
	
	public void readConfig()
	{
		try {
			
			String userDir = System.getProperty("user.dir");
			DiffApkConfigParser mEnvConfigParser = new DiffApkConfigParser(this);
			mEnvConfigParser.parse(new FileInputStream(userDir+"\\diff_apk\\config.xml"));
			
			diff_apkPatchPath		=FileUtil.checkDir(diff_apkPatchPath);
			diff_apkNewPath			=FileUtil.checkDir(diff_apkNewPath);
			diff_apkPatchPath		=FileUtil.checkDir(diff_apkPatchPath);
			merge_apkOldPath		=FileUtil.checkDir(merge_apkOldPath);
			merge_apkNewPath	=FileUtil.checkDir(merge_apkNewPath);
			merge_apkPatchPath	=FileUtil.checkDir(merge_apkPatchPath);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveConfig()
	{
		String userDir = System.getProperty("user.dir");
		DiffApkConfigXmlModify mDiffApkConfigXmlModify=new DiffApkConfigXmlModify();
		mDiffApkConfigXmlModify.modifyConfig(userDir+"\\diff_apk\\config.xml", diff_apkOldPath, diff_apkNewPath, diff_apkPatchPath,merge_apkOldPath,merge_apkNewPath,merge_apkPatchPath);
	}

}

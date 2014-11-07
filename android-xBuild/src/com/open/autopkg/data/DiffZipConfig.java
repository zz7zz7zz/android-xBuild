package com.open.autopkg.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.open.autopkg.util.FileUtil;
import com.open.autopkg.xml.DiffZipConfigParser;
import com.open.autopkg.xml.DiffZipConfigXmlModify;

public class DiffZipConfig 
{
	
	public String oldLuaZipPath;
	public String newLuaZipPath;
	public String diffLuaZipPath;
	
	
	public void readConfig()
	{
		try {
			
			String userDir = System.getProperty("user.dir");
			DiffZipConfigParser mEnvConfigParser = new DiffZipConfigParser(this);
			mEnvConfigParser.parse(new FileInputStream(userDir+"\\diff_zip\\config.xml"));
			
			diffLuaZipPath		=FileUtil.checkDir(diffLuaZipPath);
			newLuaZipPath		=FileUtil.checkDir(newLuaZipPath);
			diffLuaZipPath		=FileUtil.checkDir(diffLuaZipPath);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveConfig()
	{
		String userDir = System.getProperty("user.dir");
		DiffZipConfigXmlModify mDiffZipConfigXmlModify=new DiffZipConfigXmlModify();
		mDiffZipConfigXmlModify.modifyConfig(userDir+"\\diff_zip\\config.xml", oldLuaZipPath, newLuaZipPath, diffLuaZipPath);
	}

}

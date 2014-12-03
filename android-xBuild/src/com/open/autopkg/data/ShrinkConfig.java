package com.open.autopkg.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.open.autopkg.util.FileUtil;
import com.open.autopkg.xml.ShrinkConfigParser;
import com.open.autopkg.xml.ShrinkConfigXmlModify;

public class ShrinkConfig 
{
	
	public String srcImgPath;
	public String dstImgPath;
	public ArrayList<String> apiKeys=new ArrayList<String>();
	
	public void readConfig()
	{
		try {
			
			String userDir = System.getProperty("user.dir");
			ShrinkConfigParser mEnvConfigParser = new ShrinkConfigParser(this);
			mEnvConfigParser.parse(new FileInputStream(userDir+"\\shrink\\config.xml"));
			
			srcImgPath		=FileUtil.checkDir(srcImgPath);
			dstImgPath		=FileUtil.checkDir(dstImgPath);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveConfig()
	{
		String userDir = System.getProperty("user.dir");
		ShrinkConfigXmlModify mShrinkConfigXmlModify=new ShrinkConfigXmlModify();
		mShrinkConfigXmlModify.modifyConfig(userDir+"\\shrink\\config.xml", srcImgPath, dstImgPath);
	}

}

package com.open.autopkg.data;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.open.autopkg.util.FileUtil;
import com.open.autopkg.xml.BatConfigParser;
import com.open.autopkg.xml.ChannelConfigParser;
import com.open.autopkg.xml.EnvConfigParser;
import com.open.autopkg.xml.KeyStoreParser;
import com.open.autopkg.xml.XmlModify;

public class Config {
	
	public String jdkDir;
	public String sdkDir;
	public ArrayList<ProjectBean> projectList=new ArrayList<ProjectBean>(2);
	public ArrayList<LuaZipBean> LuaZipList=new ArrayList<LuaZipBean>(7);
	
	public void readConfig()
	{
		try {
				//
					projectList.clear();
					LuaZipList.clear();
					String userDir = System.getProperty("user.dir");
					EnvConfigParser mEnvConfigParser = new EnvConfigParser(this);
					mEnvConfigParser.parse(new FileInputStream(userDir+"\\build_scqp\\config_env.xml"));
					
					for(int i=0;i<this.projectList.size();i++)
					{
						ChannelConfigParser mChannelConfigParser=new ChannelConfigParser(this.projectList.get(i).channelList);
						mChannelConfigParser.parse(new FileInputStream(userDir+"\\build_scqp\\"+this.projectList.get(i).projectChannelFile));
						
						KeyStoreParser mKeyStoreParser=new KeyStoreParser(this.projectList.get(i).projectKeyStore);
						mKeyStoreParser.parse(new FileInputStream(userDir+"\\build_scqp\\"+this.projectList.get(i).projectKeyStoreFile));
					}
					
					BatConfigParser mBatConfigParser = new BatConfigParser(this.LuaZipList);
					mBatConfigParser.parse(new FileInputStream(userDir+"\\build_scqp\\config_luaZip.xml"));
					
					initJavaSdkEnv();
					initLuaEnv();
					
					jdkDir	=FileUtil.checkDir(jdkDir);
					sdkDir	=FileUtil.checkDir(sdkDir);
					
					for (int i = 0; i < projectList.size(); i++) 
					{					
						projectList.get(i).projectApkDir	=FileUtil.checkDir(projectList.get(i).projectApkDir);
						projectList.get(i).projectLuaDir	=FileUtil.checkDir(projectList.get(i).projectLuaDir);
					}
					
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void  initJavaSdkEnv()
	{
			boolean isJDKExist=new File(jdkDir).exists();
			boolean isSDKExist=new File(sdkDir).exists();
			if(isJDKExist&&isSDKExist)
			{
				return;
			}
			
			String[] envArray =System.getenv("path").split(";");
			for (String env : envArray) 
			{
				if(!isJDKExist && env.contains("jdk"))
				{
					int index=env.indexOf("\\", env.indexOf("jdk"));
					jdkDir=env.substring(0, index);
				}
				else if (!isSDKExist && env.contains("android"))
				{
					int sdkIndex=env.indexOf("sdk");
					if(sdkIndex>0)
					{
						int index=env.indexOf("\\", sdkIndex);
						sdkDir=env.substring(0, index);
					}
				}
			}
	}
	
	private void initLuaEnv()
	{
		String dllFilename = "luajava-1.1.dll";
		String luabinPath = jdkDir + "\\bin\\" + dllFilename;

		File luaDllfile = new File(luabinPath);
		if(luaDllfile.exists())
		{
			return;
		}
		else
		{
			FileUtil.copyFile("lib\\" + dllFilename, luabinPath);
		}
	}
	
	public void saveConfig(int selectedProjectIndex)
	{
		String userDir = System.getProperty("user.dir");
		XmlModify xmlModify = new XmlModify();
		xmlModify.modifyConfig(userDir+"\\build_scqp\\config_env.xml",
				this.jdkDir,
				this.sdkDir,
				this.projectList.get(selectedProjectIndex).projectApkDir,
				this.projectList.get(selectedProjectIndex).projectLuaDir,
				selectedProjectIndex);
	}
	
	public static  class ProjectBean 
	{
		public String projectName;
		public String projectAppId;
		public String projectApkDir;
		public String projectLuaDir;
		
		public String projectChannelFile;
		public String projectKeyStoreFile;
		public ArrayList<ChannelBean> channelList=new ArrayList<ChannelBean>(5);
		public KeyStore projectKeyStore=new KeyStore();
	}
	
	public static class ChannelBean
	{
		//对应友盟统计
		public String umeng_appkey;//52b11b9b56240b55920d4a3b 正式Key
		public String umeng_appkey_test;//536c4a4356240b0a7104a6cb 测试Key
		public String umeng_channel;//gdt-scqipai
		
		//对应Server统计
		public String channelId;//110332
		public String channelKey;//渠道Key
		public String channelName;//广点通
		public String channelOutputName;//scqp_gdt-scqipai_110332_v1.3.2.apk
	}
	
	public static class LuaZipBean
	{
		public String gamePkg;//hall
		public String gameName;//大厅
		public String gameEntry;//gameConfig
		public String zipPath;//game_hall
		public String batName;//sync_publish_app_all.bat
	}
	
	public static class KeyStore
	{
		public String Keystore;//
		public String KeystorePwd;//
		public String Alias;//
		public String AliasPwd;//
	}
}

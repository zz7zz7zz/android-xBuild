package com.open.autopkg.apk;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import com.open.autopkg.data.PropertyBean;
import com.open.autopkg.util.FileUtil;


public class AutoBuilder {
	
	public static interface IAutoBuild
	{
		public  void buildStarted();
		public  void printLog(String message);
		public  void buildFinished();
		public  void buildProgress();
	}
	
	private PropertyBean propertyBean;
	private DefaultLogger consoleLogger;
	private IAutoBuild mIAutoBuild;
	
	public AutoBuilder(PropertyBean pb ,IAutoBuild mAutoBuild)
	{
		this.propertyBean = pb;
		this.mIAutoBuild=mAutoBuild;
	}
		
	/**
	 * 正式开始打包
	 */
	public void antBuild() {
		consoleLogger = new DefaultLogger() {
			@Override
			public void buildFinished(BuildEvent event) {
				super.buildFinished(event);
				proBuildFinished();
				if(AutoBuilder.this.mIAutoBuild!=null)
				{
					AutoBuilder.this.mIAutoBuild.buildFinished();
				}
			}

			@Override
			public void buildStarted(BuildEvent event) {
				// TODO Auto-generated method stub
				super.buildStarted(event);
				
				if(AutoBuilder.this.mIAutoBuild!=null)
				{
					AutoBuilder.this.mIAutoBuild.buildStarted();
				}
			}
			
		};

		 PrintStream out=new PrintStream(new ByteArrayOutputStream())
		 {
			@Override
			public void write(byte[] buf, int off, int len) {
				// TODO Auto-generated method stub
				super.write(buf, off, len);
				
				if(AutoBuilder.this.mIAutoBuild!=null)
				{
					final String message = new String(buf, off, len);
					AutoBuilder.this.mIAutoBuild.printLog(message);
				}
			}
			 
		 };
        consoleLogger.setErrorPrintStream(out);
        consoleLogger.setOutputPrintStream(out);
        consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
		
		preAutoBuild();

        Project p = new Project();
        p.setProperty("java.home", propertyBean.getJavaDir());
        p.addBuildListener(consoleLogger);
	    try {
	    	p.fireBuildStarted();
	    	p.init();
	    	File buildFile = new File(propertyBean.getProjectDir() + "\\build.xml");
	    	ProjectHelper.configureProject(p, buildFile);
	    	String defaultTarget = p.getDefaultTarget();
	    	p.executeTarget(defaultTarget);
	    	p.fireBuildFinished(null);
	    } catch (BuildException e) {
	    	p.fireBuildFinished(e);
	    }
	    
		if(AutoBuilder.this.mIAutoBuild!=null)
		{
			AutoBuilder.this.mIAutoBuild.printLog(out.toString());
		}
	}
	
	/**
	 * 打包预处理
	 */
	private void preAutoBuild(){
		writeProperties();
		copyPropertyFile(propertyBean.getProjectDir());
		
		checkLibraryProject(propertyBean.getProjectDir());
		clearUTF8Mark(propertyBean.getProjectDir());
		for (String refProject : refProjectList)
		{
			clearUTF8Mark(refProject);
		}
	}
	
	private void clearUTF8Mark(String prejectHome) {
		File srcFile = new File(prejectHome+"\\src");
		if (srcFile.exists()) clearUTF8Mark(srcFile);
	}

	private void clearUTF8Mark(File file) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				clearUTF8Mark(f);
			}
		} else {
			FileInputStream fis = null;
			InputStream is = null;
			ByteArrayOutputStream baos = null;
			OutputStream out = null;
			try {
				fis = new FileInputStream(file);
				is = new BufferedInputStream(fis);
				baos = new ByteArrayOutputStream();
				byte b[] = new byte[3];
				is.read(b);
				// System.out.println(b[0] + ":" + b[1] + ":" + b[2]);
				if (-17 == b[0] && -69 == b[1] && -65 == b[2]) {
					System.out.println("Modify BOM file: " + file.getAbsolutePath());
					b = new byte[1024];
					while (true) {
						int bytes = 0;
						try {
							bytes = is.read(b);
						} catch (IOException e) {}
						if (bytes == -1) {
							break;
						}
						baos.write(b, 0, bytes);
						b = baos.toByteArray();
					}
					file.delete();
					out = new FileOutputStream(file);
					baos.writeTo(out);
				}
			} catch (Exception e) {
			} finally {
				try {
					if (fis != null) fis.close();
					if (out != null) out.close();
					if (is != null) is.close();
					if (baos != null) baos.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 将配置信息写入build.properties中
	 */
	private void writeProperties() 
	{
		InputStream in = null;
		OutputStream out = null;
		try {
					in = new FileInputStream(propertyBean.getBuildPropertiesPath());
					Properties prop = new Properties();
					prop.load(in);
					//调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
					//强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
					out = new FileOutputStream(propertyBean.getBuildPropertiesPath());
					
					prop.setProperty("sdk.dir", propertyBean.getSdkDir());
					prop.setProperty("java.dir", propertyBean.getJavaDir());
					
					prop.setProperty("project.dir", propertyBean.getProjectDir());
					prop.setProperty("project.name", propertyBean.getProjectName());
					prop.setProperty("project.version", "v"+propertyBean.getProjectVersion());
					
					prop.setProperty("key.store", propertyBean.getKeyStore());
					prop.setProperty("key.store.password", propertyBean.getKeyStorePwd());
					prop.setProperty("key.alias", propertyBean.getKeyAlias());
					prop.setProperty("key.alias.password", propertyBean.getKeyAliasPwd());
					
					prop.setProperty("jar.libs.dir",propertyBean.getJarLibsDir());
					prop.setProperty("apk.out.dir", propertyBean.getApkOutputDir());
					prop.setProperty("channel.list", propertyBean.getChannelList());
					
					//以适合使用 load 方法加载到 Properties 表中的格式，
					//将此 Properties 表中的属性列表（键和元素对）写入输出流
					prop.store(out, "Save");
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {			
			try {
				if (in != null) in.close();
				if (out != null) out.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 将build.properties复制到工程目录下
	 * @param projectHome
	 */
	private void copyPropertyFile(String projectHome)
	{
		try {
				File srcProFile = new File(propertyBean.getBuildPropertiesPath());
				File dstProFile = new File(projectHome + "\\build.properties");
				if (dstProFile.exists()) 
				{
					dstProFile.delete();
				}
				dstProFile.createNewFile();
				FileUtil.copyFile(srcProFile, dstProFile);
	
				File srcBuildFile = new File(propertyBean.getBuildXmlPath());
				File dstBuildFile = new File(projectHome + "\\build.xml");
				if (dstBuildFile.exists()) 
				{
					dstBuildFile.delete();
				}
				dstBuildFile.createNewFile();
				FileUtil.copyFile(srcBuildFile, dstBuildFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	List<String> refProjectList = new ArrayList<String>();
	private void checkLibraryProject(String projectHome) {
		InputStream in = null;
		try {
			in = new FileInputStream(projectHome + "\\project.properties");
			Properties prop = new Properties();
			prop.load(in);
			int index = 1;
			List<String> tempRefProjectList = new ArrayList<String>();
			while(true) {
				String refProject = prop.getProperty("android.library.reference." + index++);
				if (refProject == null || "".equals(refProject)) 
				{
					break;
				}

				refProject = new String(refProject.getBytes("ISO8859-1"), "UTF-8");//防止中文乱码
				String refProjectPath = updatePath(projectHome, refProject);
				refProjectPath = refProjectPath.replace("/", "\\");//统一路径分隔符
				copyPropertyFile(refProjectPath);
				updateProperty(refProjectPath + "\\build.properties", "project.dir", refProjectPath);

				tempRefProjectList.add(refProjectPath);
			}
			for (String refProject : tempRefProjectList) {
				refProjectList.add(refProject);
				checkLibraryProject(refProject);
			}
		} catch (IOException e) {
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private String updatePath(String head, String suffix) {
		while(suffix.contains("../")) {
			suffix = suffix.substring(suffix.indexOf("../") + 3);
			head = head.substring(0, head.lastIndexOf("\\"));
		}
		return head + "\\" + suffix;
	}
	
	private void updateProperty(String propertyFilePath, String propertyName, String propertyValue) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(propertyFilePath);
			Properties prop = new Properties();
			prop.load(in);
			out = new FileOutputStream(propertyFilePath);
			prop.setProperty(propertyName, propertyValue);
			prop.store(out, "Save");
		} catch (IOException e) {
		} finally {
			try {
				if (in != null) in.close();
				if (out != null) out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * Ant执行完后的回调
	 */
	public void proBuildFinished() {
	
		proAntBuild();
	}
	
	private void proAntBuild() {
		//删除打包项目下的build.properties和build.xml
		deletePropertyFile(propertyBean.getProjectDir());

		for (String projectHome : refProjectList) {
			deletePropertyFile(projectHome);
		}
		refProjectList.clear();
	}
	
	/** 删除打包项目下的build.properties和build.xml */
	private void deletePropertyFile(String projectHome) {
		File targetProFile = new File(projectHome + "\\build.properties");
		if (targetProFile.exists()) targetProFile.delete();

		File targetBuildFile = new File(projectHome + "\\build.xml");
		if (targetBuildFile.exists()) targetBuildFile.delete();
		
	}
	
	public void setPrintStream(PrintStream ps){
		consoleLogger.setErrorPrintStream(ps);
		consoleLogger.setOutputPrintStream(ps);
	}
	
}

package com.open.autopkg.data;

public class PropertyBean {
	
	private String javaDir;
	private String sdkDir;
	
	private String projectDir;
	private String projectName;
	private String projectVersion;
	
	private String keyStore;
	private String keyStorePwd;
	private String keyAlias;
	private String keyAliasPwd;
	
	private String jarLibsDir;
	private String channelList;
	private String apkOutputDir;
	
	private String buildXmlPath;
	private String buildPropertiesPath;
	
	
	public String getJavaDir() {
		return javaDir;
	}
	public void setJavaDir(String javaDir) {
		this.javaDir = javaDir;
	}
	public String getSdkDir() {
		return sdkDir;
	}
	public void setSdkDir(String sdkDir) {
		this.sdkDir = sdkDir;
	}
	public String getProjectDir() {
		return projectDir;
	}
	public void setProjectDir(String projectDir) {
		this.projectDir = projectDir;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectVersion() {
		return projectVersion;
	}
	public void setProjectVersion(String projectVersion) {
		this.projectVersion = projectVersion;
	}
	public String getChannelList() {
		return channelList;
	}
	public void setChannelList(String channelList) {
		this.channelList = channelList;
	}

	public String getJarLibsDir() {
		return jarLibsDir;
	}
	public void setJarLibsDir(String jarLibsDir) {
		this.jarLibsDir = jarLibsDir;
	}
	public String getKeyStore() {
		return keyStore;
	}
	public void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}
	public String getKeyStorePwd() {
		return keyStorePwd;
	}
	public void setKeyStorePwd(String keyStorePwd) {
		this.keyStorePwd = keyStorePwd;
	}
	public String getKeyAlias() {
		return keyAlias;
	}
	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}
	public String getKeyAliasPwd() {
		return keyAliasPwd;
	}
	public void setKeyAliasPwd(String keyAliasPwd) {
		this.keyAliasPwd = keyAliasPwd;
	}
	public String getApkOutputDir() {
		return apkOutputDir;
	}
	public void setApkOutputDir(String apkOutputDir) {
		this.apkOutputDir = apkOutputDir;
	}
	
	public String getBuildXmlPath() {
		return buildXmlPath;
	}
	public void setBuildXmlPath(String buildXmlPath) {
		this.buildXmlPath = buildXmlPath;
	}
	public String getBuildPropertiesPath() {
		return buildPropertiesPath;
	}
	public void setBuildPropertiesPath(String buildPropertiesPath) {
		this.buildPropertiesPath = buildPropertiesPath;
	}
}

package com.open.autopkg.xml;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.open.autopkg.data.Config;
import com.open.autopkg.data.Config.ProjectBean;

public class EnvConfigParser extends DefaultHandler {

	public static final String NODE_JDKDIR									="jdkDir";
	public static final String NODE_SDKDIR									="sdkDir";
	public static final String NODE_PROJECT								="project";
	public static final String NODE_PROJECTITEM						="projectItem";
	public static final String NODE_PROJECTID							="projectId";
	public static final String NODE_PROJECTNAME					="projectName";
	public static final String NODE_PROJECTAPKDIR					="projectApkDir";
	public static final String NODE_PROJECTLUADIR					="projectLuaDir";
	public static final String NODE_PROJECTCHANNELFILE		="projectChannelFile";
	public static final String NODE_KEYSTOREFILE						="keyStoreFile";
	
	private Config mEnvConfig;
	private ProjectBean currentProject;
	private String currentNodeName=null;
	
	public EnvConfigParser(Config mEnvConfig) 
	{
		this.mEnvConfig=mEnvConfig;
	}
	
	public void parse(InputStream is) throws Exception {
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse(is, this);
	}
	
	@Override
    public void startDocument() throws SAXException {
//System.out.print("startDocument() A:\n");
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//System.out.print("startElement() B:"+"uri:"+uri+"localName:"+localName+"qName:"+qName+"\n");
//		System.out.print("startElement() B:"+"localName:"+localName+"qName:"+qName+"\n");
		currentNodeName=qName;
		if(NODE_PROJECTITEM.equals(currentNodeName))
		{
			currentProject=new ProjectBean();
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) {
//System.out.print("characters() C:"+"ch:"+new String(ch)+"start:"+start+"length:"+length+"\n");
		 String value = new String(ch, start, length).trim();
		 if(value==null||"".equals(value))
		 {
			 return;
		 }
		 
		if(NODE_JDKDIR.equals(currentNodeName))
		{
			mEnvConfig.jdkDir=value;
		}
		else if(NODE_SDKDIR.equals(currentNodeName))
		{
			mEnvConfig.sdkDir=value;
		}
		else if(NODE_PROJECTID.equals(currentNodeName))
		{
			currentProject.projectAppId=value;
		}
		else if(NODE_PROJECTNAME.equals(currentNodeName))
		{
			currentProject.projectName=value;
		}
		else if(NODE_PROJECTAPKDIR.equals(currentNodeName))
		{
			currentProject.projectApkDir=value;
		}
		else if(NODE_PROJECTLUADIR.equals(currentNodeName))
		{
			currentProject.projectLuaDir=value;
		}
		else if(NODE_PROJECTCHANNELFILE.equals(currentNodeName))
		{
			currentProject.projectChannelFile=value;
		}
		else if(NODE_KEYSTOREFILE.equals(currentNodeName))
		{
			currentProject.projectKeyStoreFile=value;
		}
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
//System.out.print("endElement() D:"+"uri:"+uri+"localName:"+localName+"qName:"+qName+"\n");
		if(NODE_PROJECTITEM.equals(qName))
		{
			mEnvConfig.projectList.add(currentProject);
		}
	}
	
	@Override
	public void endDocument () throws SAXException {
//System.out.print("endDocument() E:"+"\n");
	}
	

}

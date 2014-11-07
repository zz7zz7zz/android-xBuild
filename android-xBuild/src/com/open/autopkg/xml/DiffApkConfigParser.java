package com.open.autopkg.xml;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.open.autopkg.data.DiffApkConfig;

public class DiffApkConfigParser extends DefaultHandler {

	public static final String NODE_DIFF_APKOLDPATH									="diff_apkOldPath";
	public static final String NODE_DIFF_APKNEWPATH									="diff_apkNewPath";
	public static final String NODE_DIFF_APKPATCHPATH								="diff_apkPatchPath";
	
	public static final String NODE_MERGE_APKOLDPATH									="merge_apkOldPath";
	public static final String NODE_MERGE_APKNEWPATH									="merge_apkNewPath";
	public static final String NODE_MERGE_APKPATCHPATH								="merge_apkPatchPath";
	
	private String currentNodeName=null;
	private DiffApkConfig mDiffApkConfig;
	
	public DiffApkConfigParser(DiffApkConfig mDiffZipBean) 
	{
		this.mDiffApkConfig=mDiffZipBean;
	}
	
	public void parse(InputStream is) throws Exception 
	{
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
		currentNodeName=qName;
	}
	
	@Override
	public void characters(char[] ch, int start, int length) {
//System.out.print("characters() C:"+"ch:"+new String(ch)+"start:"+start+"length:"+length+"\n");
		 String value = new String(ch, start, length).trim();
		 if(value==null||"".equals(value))
		 {
			 return;
		 }
		 
		if(NODE_DIFF_APKOLDPATH.equals(currentNodeName))
		{
			mDiffApkConfig.diff_apkOldPath=value;
		}
		else if(NODE_DIFF_APKNEWPATH.equals(currentNodeName))
		{
			mDiffApkConfig.diff_apkNewPath=value;
		}
		else if(NODE_DIFF_APKPATCHPATH.equals(currentNodeName))
		{
			mDiffApkConfig.diff_apkPatchPath=value;
		}		
		else if(NODE_MERGE_APKOLDPATH.equals(currentNodeName))
		{
			mDiffApkConfig.merge_apkOldPath=value;
		}		
		else if(NODE_MERGE_APKNEWPATH.equals(currentNodeName))
		{
			mDiffApkConfig.merge_apkNewPath=value;
		}		
		else if(NODE_MERGE_APKPATCHPATH.equals(currentNodeName))
		{
			mDiffApkConfig.merge_apkPatchPath=value;
		}		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
//System.out.print("endElement() D:"+"uri:"+uri+"localName:"+localName+"qName:"+qName+"\n");

	}
	
	@Override
	public void endDocument () throws SAXException {
//System.out.print("endDocument() E:"+"\n");
	}
	

}

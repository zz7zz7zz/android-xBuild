package com.open.autopkg.xml;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.open.autopkg.data.DiffZipConfig;

public class DiffZipConfigParser extends DefaultHandler {

	public static final String NODE_OLDLUAZIPPATH									="oldLuaZipPath";
	public static final String NODE_NEWLUAZIPPATH									="newLuaZipPath";
	public static final String NODE_DIFFLUAZIPPATH									="diffLuaZipPath";
	
	private String currentNodeName=null;
	private DiffZipConfig mDiffZipConfig;
	
	public DiffZipConfigParser(DiffZipConfig mDiffZipBean) 
	{
		this.mDiffZipConfig=mDiffZipBean;
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
		 
		if(NODE_OLDLUAZIPPATH.equals(currentNodeName))
		{
			mDiffZipConfig.oldLuaZipPath=value;
		}
		else if(NODE_NEWLUAZIPPATH.equals(currentNodeName))
		{
			mDiffZipConfig.newLuaZipPath=value;
		}
		else if(NODE_DIFFLUAZIPPATH.equals(currentNodeName))
		{
			mDiffZipConfig.diffLuaZipPath=value;
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

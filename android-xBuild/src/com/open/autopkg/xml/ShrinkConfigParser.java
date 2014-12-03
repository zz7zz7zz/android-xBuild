package com.open.autopkg.xml;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.open.autopkg.data.ShrinkConfig;

public class ShrinkConfigParser extends DefaultHandler {

	public static final String NODE_SRCIMGPATH									="srcImgPath";
	public static final String NODE_DSTIMGPATH									="dstImgPath";
	public static final String NODE_APIKEY										="apiKey";
	
	private String currentNodeName=null;
	private ShrinkConfig mShrinkConfig;
	
	public ShrinkConfigParser(ShrinkConfig mDiffZipBean) 
	{
		this.mShrinkConfig=mDiffZipBean;
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
		 
		if(NODE_SRCIMGPATH.equals(currentNodeName))
		{
			mShrinkConfig.srcImgPath=value;
		}
		else if(NODE_DSTIMGPATH.equals(currentNodeName))
		{
			mShrinkConfig.dstImgPath=value;
		}
		else if(NODE_APIKEY.equals(currentNodeName))
		{
			mShrinkConfig.apiKeys.add(value);
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

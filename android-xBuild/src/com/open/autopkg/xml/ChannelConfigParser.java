package com.open.autopkg.xml;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.open.autopkg.data.Config.ChannelBean;

public class ChannelConfigParser extends DefaultHandler {

	public static final String NODE_ITEM											="item";
	public static final String NODE_CHANNELID								="channelId";
	public static final String NODE_CHANNELKEY							="channelKey";
	public static final String NODE_CHANNELNAME						="channelName";
	public static final String NODE_CHANNELOUTPUTNAME		="channelOutputName";
	public static final String NODE_UMENT_CHANNEL					="umeng_channel";
	public static final String NODE_UMENT_APPKEY						="umeng_appkey";
	public static final String NODE_UMENT_APPKEY_TEST			="umeng_appkey_test";
	
	private ArrayList<ChannelBean> channelList;
	private ChannelBean mChannelInfo;
	private String currentNodeName=null;
	
	public ChannelConfigParser(ArrayList<ChannelBean> channelList) 
	{
		this.channelList=channelList;
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
		if(NODE_ITEM.equals(currentNodeName))
		{
			mChannelInfo=new ChannelBean();
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
		 
		if(NODE_CHANNELID.equals(currentNodeName))
		{
			mChannelInfo.channelId=value;
		}
		else if(NODE_CHANNELKEY.equals(currentNodeName))
		{
			mChannelInfo.channelKey=value;
		}
		else if(NODE_CHANNELNAME.equals(currentNodeName))
		{
			mChannelInfo.channelName=value;
		}
		else if(NODE_CHANNELOUTPUTNAME.equals(currentNodeName))
		{
			mChannelInfo.channelOutputName=value;
		}
		else if(NODE_UMENT_CHANNEL.equals(currentNodeName))
		{
			mChannelInfo.umeng_channel=value;
		}
		else if(NODE_UMENT_APPKEY.equals(currentNodeName))
		{
			mChannelInfo.umeng_appkey=value;
		}
		else if(NODE_UMENT_APPKEY_TEST.equals(currentNodeName))
		{
			mChannelInfo.umeng_appkey_test=value;
		}

	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
//System.out.print("endElement() D:"+"uri:"+uri+"localName:"+localName+"qName:"+qName+"\n");
		if(NODE_ITEM.equals(qName))
		{
			channelList.add(mChannelInfo);
			mChannelInfo=null;
		}
	}
	
	@Override
	public void endDocument () throws SAXException {
//System.out.print("endDocument() E:"+"\n");
	}
	

}

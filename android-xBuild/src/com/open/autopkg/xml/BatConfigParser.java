package com.open.autopkg.xml;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.open.autopkg.data.Config.LuaZipBean;

public class BatConfigParser extends DefaultHandler {

	public static final String NODE_ITEM								="item";
	public static final String NODE_GAMEPKG					="gamePkg";
	public static final String NODE_GAMENAME				="gameName";
	public static final String NODE_GAMEENTRY				="gameEntry";
	public static final String NODE_ZIPPATH						="zipPath";
	public static final String NODE_BATNAME					="batName";
	
	private ArrayList<LuaZipBean> LuaZipList;
	private LuaZipBean currentLuaZip;
	private String currentNodeName=null;
	
	public BatConfigParser(ArrayList<LuaZipBean> LuaZipList) 
	{
		this.LuaZipList=LuaZipList;
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
		currentNodeName=qName;
		if(NODE_ITEM.equals(currentNodeName))
		{
			currentLuaZip=new LuaZipBean();
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
	
		if(NODE_GAMEPKG.equals(currentNodeName))
		{
			currentLuaZip.gamePkg=value;
		}
		else if(NODE_GAMENAME.equals(currentNodeName))
		{
			currentLuaZip.gameName=value;
		}
		else if(NODE_GAMEENTRY.equals(currentNodeName))
		{
			currentLuaZip.gameEntry=value;
		}
		else if(NODE_ZIPPATH.equals(currentNodeName))
		{
			currentLuaZip.zipPath=value;
		}
		else if(NODE_BATNAME.equals(currentNodeName))
		{
			currentLuaZip.batName=value;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
//System.out.print("endElement() D:"+"uri:"+uri+"localName:"+localName+"qName:"+qName+"\n");
		if(NODE_GAMEENTRY.equals(qName))
		{
			LuaZipList.add(currentLuaZip);
		}
	}
	
	@Override
	public void endDocument () throws SAXException {
//System.out.print("endDocument() E:"+"\n");
	}
	

}

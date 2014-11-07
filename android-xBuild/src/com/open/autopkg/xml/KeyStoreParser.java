package com.open.autopkg.xml;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.open.autopkg.data.Config.KeyStore;

public class KeyStoreParser extends DefaultHandler {

	public static final String NODE_KEYSTORE						="Keystore";
	public static final String NODE_KEYSTOREPWD				="KeystorePwd";
	public static final String NODE_ALIAS								="Alias";
	public static final String NODE_ALIASPWD						="AliasPwd";
	
	private KeyStore mKeyStore;
	private String currentNodeName=null;
	
	public KeyStoreParser(KeyStore mKeyStore) 
	{
		this.mKeyStore=mKeyStore;
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

	}
	
	@Override
	public void characters(char[] ch, int start, int length) {
//System.out.print("characters() C:"+"ch:"+new String(ch)+"start:"+start+"length:"+length+"\n");
		 String value = new String(ch, start, length).trim();
		 if(value==null||"".equals(value))
		 {
			 return;
		 }
		 
		if(NODE_KEYSTORE.equals(currentNodeName))
		{
			mKeyStore.Keystore=value;
		}
		else if(NODE_KEYSTOREPWD.equals(currentNodeName))
		{
			mKeyStore.KeystorePwd=value;
		}
		else if(NODE_ALIAS.equals(currentNodeName))
		{
			mKeyStore.Alias=value;
		}
		else if(NODE_ALIASPWD.equals(currentNodeName))
		{
			mKeyStore.AliasPwd=value;
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

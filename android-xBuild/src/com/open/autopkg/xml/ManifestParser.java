package com.open.autopkg.xml;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 解析AndroidManifest.xml文件
 * @author JackCheng
 *
 */
public class ManifestParser extends DefaultHandler{
	
	private StringBuilder sb;
	private String versioncode;
	private String versionname;
	private String packagename;

	public void parse(InputStream is) throws Exception {
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse(is, this);
	}
	
	@Override
    public void startDocument() throws SAXException {
		sb = new StringBuilder();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(qName.equals("manifest")){
			packagename = attributes.getValue("package");
			versioncode = attributes.getValue("android:versionCode");
			versionname = attributes.getValue("android:versionName");
		}
		
	}
	
	@Override
	public void characters(char[] ch, int start, int length) {
		if (ch != null && length > 0) {
			String value = new String(ch, start, length).trim();
			if(!"".equals(value)){
				sb.append(value);
			}
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
	}
	
	@Override
	public void endDocument () throws SAXException {
		
	}

	public String getVersioncode() {
		return versioncode;
	}

	public String getVersionname() {
		return versionname;
	}

	public String getPackagename() {
		return packagename;
	}

	
}

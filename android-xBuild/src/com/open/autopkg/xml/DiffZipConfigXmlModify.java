/**
 * 
 */
package com.open.autopkg.xml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author ZedLi
 *
 */
public class DiffZipConfigXmlModify {
	

	public void modifyConfig(String file,String oldLuaZipPath,String newLuaZipPath,String diffLuaZipPath)
	{
		Document document;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			Element root = document.getDocumentElement();  
			root.getElementsByTagName("oldLuaZipPath").item(0).setTextContent(oldLuaZipPath); 
			root.getElementsByTagName("newLuaZipPath").item(0).setTextContent(newLuaZipPath); 
			root.getElementsByTagName("diffLuaZipPath").item(0).setTextContent(diffLuaZipPath); 
			output(root,file);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void output(Node node, String filename) 
	{  
        TransformerFactory transFactory = TransformerFactory.newInstance();  
        try {  
          Transformer transformer = transFactory.newTransformer();  
          // 设置各种输出属性  
          transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8"); 
          transformer.setOutputProperty(OutputKeys.INDENT, "yes");  
          DOMSource source = new DOMSource(); 
          // 将待转换输出节点赋值给DOM源模型的持有者(holder)  
          source.setNode(node);  
          StreamResult result = new StreamResult();  
          if (filename == null) {  
            // 设置标准输出流为transformer的底层输出目标  
            result.setOutputStream(System.out);  
          } else {  
            result.setOutputStream(new FileOutputStream(filename));  
          }  
          // 执行转换从源模型到控制台输出流  
          transformer.transform(source, result);  
        } catch (TransformerConfigurationException e) {  
          e.printStackTrace();  
        } catch (TransformerException e) {  
          e.printStackTrace();  
        } catch (FileNotFoundException e) {  
          e.printStackTrace();  
        }  
      }  
}

package com.open.autopkg.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.open.autopkg.data.ShrinkConfig;

public class ShrinkTab extends Composite {

	//UI-Data
	private ShrinkConfig mConfig=new ShrinkConfig();
	private String[]		srcFileArray=null;
	private String[] 	dstFileArray=null;
	
	//UI
	private List srcImgList;
	private List dstImgList;
	
	private Text srcImgPathText;
	private Text dstImgPathText;
	private Text logText;
	 
	public ShrinkTab(Composite arg0, int arg1) {
		super(arg0, arg1);
		
		mConfig.readConfig();
		initView();
		initViewData();
	}
	
	
	private void initView()
	{
		Group apkLuaGroup = new Group(this, SWT.NONE);
		apkLuaGroup.setBounds(0, 0, 1280, 750);
		
		Group channelGroup=new Group(apkLuaGroup, SWT.NONE);
		channelGroup.setText("█ PNG/JPG压缩区域");
		channelGroup.setBounds(10, 20, 1250, 330);
		
		
		Label jdkLabel = new Label(channelGroup, SWT.NONE);
		jdkLabel.setBounds(10, 32, 80, 15);
		jdkLabel.setText("源图片目录：");
		
		srcImgPathText = new Text(channelGroup, SWT.BORDER );
		srcImgPathText.setBounds(100, 26, 500, 23);
		
		Button jdkBrowseBtn = new Button(channelGroup,SWT.NONE);
		jdkBrowseBtn.setBounds(620,25,61,23);
		jdkBrowseBtn.setText("浏览");
		jdkBrowseBtn.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				super.widgetSelected(arg0);
				DirectoryDialog dd= new DirectoryDialog(ShrinkTab.this.getShell());
				if(new File(mConfig.srcImgPath).isDirectory())
				{
					dd.setFilterPath(mConfig.srcImgPath);
				}
				dd.setText("请选择源图片目录");
				String jdkFile = dd.open();
				if (jdkFile!=null)
				{
					File directiory = new File(jdkFile);
					srcImgPathText.setText(directiory.getPath());
					mConfig.srcImgPath=directiory.getPath( );
					mConfig.saveConfig();
					
					initViewData();
				}
			}
		
		});
		
		Label adkLabel = new Label(channelGroup, SWT.NONE);
		adkLabel.setText("压缩后目录：");
		adkLabel.setBounds(10, 60, 80, 15);
		
		dstImgPathText = new Text(channelGroup, SWT.BORDER);
		dstImgPathText.setBounds(100, 55, 500, 23);

		Button adkBrowseBtn = new Button(channelGroup,SWT.NONE);
		adkBrowseBtn.setBounds(620,55,61,23);
		adkBrowseBtn.setText("浏览");
		adkBrowseBtn.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				super.widgetSelected(arg0);
				DirectoryDialog dd= new DirectoryDialog(ShrinkTab.this.getShell());
				if(new File(mConfig.dstImgPath).isDirectory())
				{
					dd.setFilterPath(mConfig.dstImgPath);
				}
				dd.setText("请选择压缩后图片目录");
				String sdkFile = dd.open();
				if (sdkFile!=null)
				{
					File directiory = new File(sdkFile);
					dstImgPathText.setText(directiory.getPath());
					mConfig.dstImgPath=directiory.getPath( );
					mConfig.saveConfig();
					initViewData();
				}
			}
		});
		
		Label channelLeftListLabel = new Label(channelGroup, SWT.NONE);
		channelLeftListLabel.setBounds(10, 130, 61, 15);
		channelLeftListLabel.setText("源文件夹：");

		srcImgList = new List(channelGroup, SWT.BORDER | SWT.V_SCROLL);
		srcImgList.setBounds(10, 150, 270, 160);
		
		Label channelRightListLabel = new Label(channelGroup, SWT.NONE);
		channelRightListLabel.setBounds(300, 130, 80, 15);
		channelRightListLabel.setText("压缩后文件夹：");
		
		dstImgList = new List(channelGroup, SWT.BORDER | SWT.V_SCROLL);
		dstImgList.setBounds(300, 150, 270, 160);
		dstImgList.addListener(SWT.MouseDoubleClick, new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				
			}
		});
		
		Button rightArrowBtn = new Button(channelGroup, SWT.NONE);
		rightArrowBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				mIShrinkResult.onShinkStart();
				
				for (int i = 0; i < srcFileArray.length; i++) 
				{
					shrink(srcFileArray[i]);
				}
				
				
			}
		});
		rightArrowBtn.setBounds(590, 200, 100, 50);
		rightArrowBtn.setText("压缩");
		

/************************************************************************/
/**************************Log输出区域*********************************************/	
/*************************************************************************/
		Group logGroup = new Group(channelGroup, SWT.NONE);
		logGroup.setText("Log输出");
		logGroup.setBounds(720, 30, 500, 290);
		logText = new Text(logGroup, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		logText.setBounds(10, 20, 490, 260);
		logText.addMenuDetectListener(new MenuDetectListener() {
			public void menuDetected(MenuDetectEvent e) {
				
			}
		});
		logText.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		logText.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
//		logText.setEditable(false);
		logText.setEditable(true);
	}

	private void initViewData()
	{
		srcImgPathText.setText(mConfig.srcImgPath);
		srcImgPathText.setToolTipText(mConfig.srcImgPath);
		dstImgPathText.setText(mConfig.dstImgPath);
		dstImgPathText.setToolTipText(mConfig.dstImgPath);

		File oldFile=new File(mConfig.srcImgPath);
//		srcFileArray=oldFile.list();
		srcFileArray=oldFile.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("png")||name.endsWith("PNG")||name.endsWith("jpg")||name.endsWith("JPG");
			}
		});
		
		File newFile=new File(mConfig.dstImgPath);
//		dstFileArray=newFile.list();
		dstFileArray=newFile.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("png")||name.endsWith("PNG")||name.endsWith("jpg")||name.endsWith("JPG");
			}
		});
			
		srcImgList.removeAll();
		dstImgList.removeAll();
		logText.setText("");
		
		if(null!=srcFileArray)
		{
			for(int i=0; i<srcFileArray.length; ++i)
			{
				srcImgList.add(srcFileArray[i]);
			}
		}
		
		if(null!=dstFileArray)
		{
			for(int i=0; i<dstFileArray.length; ++i)
			{
				dstImgList.add(dstFileArray[i]);
			}
		}
	}
	
	
	private int shrinkCount=0;
	private static final String API_URL = "https://api.tinypng.com/shrink";
	private void shrink(final String imgName)
	{
		new Thread(new Runnable(){

			@Override
			public void run() {
			
				boolean result=false;
				String errMsg = "";
			    final String input = mConfig.srcImgPath+"\\"+imgName;
			    final String output = mConfig.dstImgPath+"\\"+imgName;
			    
				HttpURLConnection connection=null;
				BufferedReader in=null;
				try {
							String userDir = System.getProperty("user.dir");
							System.setProperty("javax.net.ssl.trustStore", userDir+"\\jssecacerts\\jssecacerts");
							
							final String key = "XZmZMzW6sW300mGEiXp04VfZUXsMYzoO";//
//						    final String input = "D:\\input.png";
//						    final String output = "D:\\output.png";
		
						    connection = (HttpURLConnection) new URL(API_URL).openConnection();
						    connection.setConnectTimeout(30000);
						    connection.setReadTimeout(30000);
						    String auth = DatatypeConverter.printBase64Binary(("api:" + key).getBytes("UTF-8"));
						    connection.setRequestProperty("Authorization", "Basic " + auth);
						    connection.setDoOutput(true);
		
						    try (OutputStream request = connection.getOutputStream()) 
						    {
						      Files.copy(Paths.get(input), request);
						    }   
		
						    if (connection.getResponseCode() == 201)
						    {
							      // Compression was successful, retrieve output from Location header.
							      final String url = connection.getHeaderFields().get("Location").get(0);
								  connection.disconnect();
								  connection=null;
								    	
							      connection = (HttpURLConnection) new URL(url).openConnection();
							      connection.setConnectTimeout(30000);
							      connection.setReadTimeout(30000);
							      try (InputStream response = connection.getInputStream()) 
							      {
								        Files.copy(response, Paths.get(output), StandardCopyOption.REPLACE_EXISTING);
								        result=true;
								        System.out.println("Compression success.");
							      }
						    } 
						    else
						    {
							      // Something went wrong! You can parse the JSON body for details.
						    	 System.out.println("Compression failed.");
						    	 
							      try (InputStream response = connection.getInputStream()) 
							      {
							    	  	in = new BufferedReader(new InputStreamReader(response));
							            StringBuilder sb = new StringBuilder();
							            String line;
							            while ((line = in.readLine()) != null) 
							            {
							            	sb.append(line);
							            }
							            errMsg=sb.toString();
							      }
						    }

				} catch (Exception e2) {
					e2.printStackTrace();
					result=false;
					errMsg=getThrowableInfo(e2);
					System.out.println("Exception .");
				}finally{
						try {
									if (in != null) {
					                    in.close();
					                }
									if(null!=connection)
									{
										connection.disconnect();
										connection=null;
									}
						} catch (Exception e3) {
							e3.printStackTrace();
						}
				}
				
				mIShrinkResult.onShinkResult(result, imgName,errMsg);
			}
			
		}).start();
		 
	}
	
	IShrinkResult mIShrinkResult=new IShrinkResult()
	{
		
		@Override
		public void onShinkStart() {
			
			
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					shrinkCount=0;
					logText.append("\r\n压缩开始--------------------------\r\n");
				}
			});
			
		}
		
		@Override
		public void onShinkResult(final boolean result, final String imageName,final String errMsg) {
			
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					
					if(result)
					{
						dstImgList.add(imageName);
					}
					
					shrinkCount++;
					
					String txt=String.format("\r\n%s	压缩:	%s\r\n",imageName,(result?"成功":"失败\r\n"+errMsg+"\r\n"));
					logText.append(txt);
					logText.append(String.format("\r\n									已经成功(%s)个,失败(%s)个，总(%s)个\r\n",dstImgList.getItemCount(),shrinkCount-dstImgList.getItemCount(),srcImgList.getItemCount()));
				
					
					if(shrinkCount==srcImgList.getItemCount())
					{
						onShinkEnd();
					}
				}
			});
			
		}

		@Override
		public void onShinkEnd() {
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					shrinkCount=0;
					logText.append("\r\n压缩结束--------------------------\r\n");
				}
			});
			
		}
		
	};
	
	public interface IShrinkResult
	{
		public void onShinkStart ();
		
		public void onShinkResult (boolean result,String imageName,String errMsg);
		
		public void onShinkEnd ();
	}
	
	
	/**  
     * 获取错误的信息   
     * @param arg1  
     * @return  
     */    
    private String getThrowableInfo(Throwable arg1)   
    {    
        Writer writer = new StringWriter();    
        PrintWriter pw = new PrintWriter(writer);    
        arg1.printStackTrace(pw);    
        pw.close();    
        String error= writer.toString();    
        return error;    
    }
}

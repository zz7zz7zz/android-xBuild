package com.open.autopkg.ui;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
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

import com.open.autopkg.data.DiffZipConfig;

public class ShrinkTab extends Composite {

	//UI-Data
	private DiffZipConfig mConfig=new DiffZipConfig();
	private String[]		oldFileArray=null;
	private String[] 	newFileArray=null;
	private String[] 	diffFileArray=null;
	
	//UI
	private List zipOldList;
	private List zipNewList;
	private List zipDiffList;
	
	private Text oldLuaPathText;
	private Text newLuaZipPathText;
	private Text diffLuaZipPathText;
	private Text logText;
	
	 private static final String API_URL = "https://api.tinypng.com/shrink";
	 
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
		
		 oldLuaPathText = new Text(channelGroup, SWT.BORDER );
		oldLuaPathText.setBounds(100, 26, 500, 23);
		
		Button jdkBrowseBtn = new Button(channelGroup,SWT.NONE);
		jdkBrowseBtn.setBounds(620,25,61,23);
		jdkBrowseBtn.setText("浏览");
		jdkBrowseBtn.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				super.widgetSelected(arg0);
				DirectoryDialog dd= new DirectoryDialog(ShrinkTab.this.getShell());
				if(new File(mConfig.oldLuaZipPath).isDirectory())
				{
					dd.setFilterPath(mConfig.oldLuaZipPath);
				}
				dd.setText("请选择源图片目录");
				String jdkFile = dd.open();
				if (jdkFile!=null)
				{
					File directiory = new File(jdkFile);
					oldLuaPathText.setText(directiory.getPath());
					mConfig.oldLuaZipPath=directiory.getPath( );
					mConfig.saveConfig();
					
					initViewData();
				}
			}
		
		});
		
		Label adkLabel = new Label(channelGroup, SWT.NONE);
		adkLabel.setText("压缩后目录：");
		adkLabel.setBounds(10, 60, 80, 15);
		
		newLuaZipPathText = new Text(channelGroup, SWT.BORDER);
		newLuaZipPathText.setBounds(100, 55, 500, 23);

		Button adkBrowseBtn = new Button(channelGroup,SWT.NONE);
		adkBrowseBtn.setBounds(620,55,61,23);
		adkBrowseBtn.setText("浏览");
		adkBrowseBtn.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				super.widgetSelected(arg0);
				DirectoryDialog dd= new DirectoryDialog(ShrinkTab.this.getShell());
				if(new File(mConfig.newLuaZipPath).isDirectory())
				{
					dd.setFilterPath(mConfig.newLuaZipPath);
				}
				dd.setText("请选择压缩后图片目录");
				String sdkFile = dd.open();
				if (sdkFile!=null)
				{
					File directiory = new File(sdkFile);
					newLuaZipPathText.setText(directiory.getPath());
					mConfig.newLuaZipPath=directiory.getPath( );
					mConfig.saveConfig();
					initViewData();
				}
			}
		});
		
		Label channelLeftListLabel = new Label(channelGroup, SWT.NONE);
		channelLeftListLabel.setBounds(10, 130, 61, 15);
		channelLeftListLabel.setText("源文件夹：");

		zipOldList = new List(channelGroup, SWT.BORDER | SWT.V_SCROLL);
		zipOldList.setBounds(10, 150, 270, 160);
		
		Label channelRightListLabel = new Label(channelGroup, SWT.NONE);
		channelRightListLabel.setBounds(300, 130, 80, 15);
		channelRightListLabel.setText("压缩后文件夹：");
		
		zipNewList = new List(channelGroup, SWT.BORDER | SWT.V_SCROLL);
		zipNewList.setBounds(300, 150, 270, 160);
		zipNewList.addListener(SWT.MouseDoubleClick, new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				
			}
		});
		
		Button rightArrowBtn = new Button(channelGroup, SWT.NONE);
		rightArrowBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				new Thread(new Runnable(){

					@Override
					public void run() {
					
						boolean result=false;
					    final String input = "D:\\input.png";
					    final String output = "D:\\output.png";
					    
						HttpURLConnection connection=null;
						try {
									String userDir = System.getProperty("user.dir");
									System.setProperty("javax.net.ssl.trustStore", userDir+"\\jssecacerts\\jssecacerts");
									
									final String key = "XZmZMzW6sW300mGEiXp04VfZUXsMYzoO";//
//								    final String input = "D:\\input.png";
//								    final String output = "D:\\output.png";
				
								    connection = (HttpURLConnection) new URL(API_URL).openConnection();
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
										    try {
										    	connection.disconnect();
										    	connection=null;
											} catch (Exception e2) {
												// TODO: handle exception
											}
									      
									      connection = (HttpURLConnection) new URL(url).openConnection();
									      try (InputStream response = connection.getInputStream()) 
									      {
										        Files.copy(response, Paths.get(output), StandardCopyOption.REPLACE_EXISTING);
										        result=true;
									      }
								    } 
								    else
								    {
									      // Something went wrong! You can parse the JSON body for details.
									      System.out.println("Compression failed.");
								    }

						} catch (Exception e2) {
							e2.printStackTrace();
						}finally{
							if(null!=connection)
							{
								connection.disconnect();
								connection=null;
							}
						}
						
						mIShrinkResult.onShinkResult(result, input);
					}
					
				}).start();
				 
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
		oldLuaPathText.setText(mConfig.oldLuaZipPath);
		oldLuaPathText.setToolTipText(mConfig.oldLuaZipPath);
		newLuaZipPathText.setText(mConfig.newLuaZipPath);
		newLuaZipPathText.setToolTipText(mConfig.newLuaZipPath);
//		diffLuaZipPathText.setText(mConfig.diffLuaZipPath);
//		diffLuaZipPathText.setToolTipText(mConfig.diffLuaZipPath);
		
		File oldFile=new File(mConfig.oldLuaZipPath);
		oldFileArray=oldFile.list();
		
		File newFile=new File(mConfig.newLuaZipPath);
		newFileArray=newFile.list();
		
		File diffFile=new File(mConfig.diffLuaZipPath);
		diffFileArray=diffFile.list();
		
		
		zipOldList.removeAll();
		zipNewList.removeAll();
//		zipDiffList.removeAll();
		logText.setText("");
		
		if(null!=oldFileArray)
		{
			for(int i=0; i<oldFileArray.length; ++i)
			{
				zipOldList.add(oldFileArray[i]);
			}
		}
		
		if(null!=newFileArray)
		{
			for(int i=0; i<newFileArray.length; ++i)
			{
				zipNewList.add(newFileArray[i]);
			}
		}

//		if(null!=diffFileArray)
//		{
//			for(int i=0; i<diffFileArray.length; ++i)
//			{
//				zipDiffList.add(diffFileArray[i]);
//			}
//		}
	}
	
	IShrinkResult mIShrinkResult=new IShrinkResult()
	{
		@Override
		public void onShinkResult(final boolean result, final String imageName) {
			
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					String txt=String.format("%s	压缩:	%s\r\n",imageName,(result?"成功":"失败"));
					logText.append(txt);
					
				}
			});
			
		}
		
	};
	
	public interface IShrinkResult
	{
		public void onShinkResult (boolean result,String imageName);
	}
}

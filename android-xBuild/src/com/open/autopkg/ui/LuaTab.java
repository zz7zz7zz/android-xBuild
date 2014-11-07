package com.open.autopkg.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.open.autopkg.data.DiffZipConfig;

public class LuaTab extends Composite {

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
	
	public LuaTab(Composite arg0, int arg1) {
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
		channelGroup.setText("█ Lua包差分区域");
		channelGroup.setBounds(10, 20, 1250, 330);
		
		
		Label jdkLabel = new Label(channelGroup, SWT.NONE);
		jdkLabel.setBounds(10, 32, 80, 15);
		jdkLabel.setText("Lua旧包 路径：");
		
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
				DirectoryDialog dd= new DirectoryDialog(LuaTab.this.getShell());
				if(new File(mConfig.oldLuaZipPath).isDirectory())
				{
					dd.setFilterPath(mConfig.oldLuaZipPath);
				}
				dd.setText("请选择你的 Lua旧包 目录");
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
		adkLabel.setText("Lua新包 路径：");
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
				DirectoryDialog dd= new DirectoryDialog(LuaTab.this.getShell());
				if(new File(mConfig.newLuaZipPath).isDirectory())
				{
					dd.setFilterPath(mConfig.newLuaZipPath);
				}
				dd.setText("请选择你的 Lua新包 目录");
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
		
		Label projectPathLabel = new Label(channelGroup, SWT.NONE);
		projectPathLabel.setText("Lua差分 路径：");
		projectPathLabel.setBounds(10, 88, 80, 15);
		
		diffLuaZipPathText = new Text(channelGroup, SWT.BORDER);
		diffLuaZipPathText.setBounds(100, 88, 500, 23);

		
		Button projectPathBrowseBtn = new Button(channelGroup,SWT.NONE);
		projectPathBrowseBtn.setBounds(620, 85, 61, 23);
		projectPathBrowseBtn.setText("浏览");
		projectPathBrowseBtn.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				super.widgetSelected(arg0);
				DirectoryDialog dd= new DirectoryDialog(LuaTab.this.getShell());
				if(new File(mConfig.diffLuaZipPath).isDirectory())
				{
					dd.setFilterPath(mConfig.diffLuaZipPath);
				}
				dd.setText("请选择你的 Lua差分 目录");
				String sdkFile = dd.open();
				if (sdkFile!=null)
				{
					File directiory = new File(sdkFile);
					diffLuaZipPathText.setText(directiory.getPath());
					mConfig.diffLuaZipPath=directiory.getPath( );
					mConfig.saveConfig();
					initViewData();
				}
			}	
		});
		

		Label channelLeftListLabel = new Label(channelGroup, SWT.NONE);
		channelLeftListLabel.setBounds(10, 130, 61, 15);
		channelLeftListLabel.setText("Lua旧包：");

		zipOldList = new List(channelGroup, SWT.BORDER | SWT.V_SCROLL);
		zipOldList.setBounds(10, 150, 180, 160);
		
		Label channelRightListLabel = new Label(channelGroup, SWT.NONE);
		channelRightListLabel.setBounds(210, 130, 80, 15);
		channelRightListLabel.setText("Lua新包：");
		
		zipNewList = new List(channelGroup, SWT.BORDER | SWT.V_SCROLL);
		zipNewList.setBounds(210, 150, 180, 160);
		zipNewList.addListener(SWT.MouseDoubleClick, new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				
			}
		});
		
		channelRightListLabel = new Label(channelGroup, SWT.NONE);
		channelRightListLabel.setBounds(520, 130, 80, 15);
		channelRightListLabel.setText("Lua差分包：");
		
		zipDiffList = new List(channelGroup, SWT.BORDER | SWT.V_SCROLL);
		zipDiffList.setBounds(520, 150, 180, 160);
		zipDiffList.addListener(SWT.MouseDoubleClick, new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				
			}
		});
		
		Button rightArrowBtn = new Button(channelGroup, SWT.NONE);
		rightArrowBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				try {
						zipDiffList.removeAll();
						String userDir = System.getProperty("user.dir");
						if(null!=oldFileArray && null!=newFileArray)
						{
								for (int j = 0; j < oldFileArray.length; j++) 
								{
										String cmd=String.format("cmd /c java -jar %s -file1  %s -file2 %s -outputfile %s -verbose",
												userDir+"\\lib\\zipdiff.jar",
												mConfig.oldLuaZipPath+"\\"+oldFileArray[j],
												mConfig.newLuaZipPath+"\\"+newFileArray[0],
												mConfig.diffLuaZipPath+"\\"+oldFileArray[j].substring(0, oldFileArray[j].indexOf("."))+"-"+newFileArray[0]);
										
					//					Runtime.getRuntime().exec(cmds);
										Process process = Runtime.getRuntime().exec(cmd); 
										String strInfo = ""; 
										BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); 
										while((strInfo = reader.readLine()) != null)
										{ 
											System.out.println(strInfo); 
											logText.append("\r\n"+strInfo);
										}
										logText.append("\r\n");
										
										zipDiffList.add(oldFileArray[j].substring(0, oldFileArray[j].indexOf("."))+"-"+newFileArray[0]);
								}
							}
							
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		rightArrowBtn.setBounds(405, 200, 100, 50);
		rightArrowBtn.setText("差分");
		
//		Button cleanDirBtn = new Button(channelGroup, SWT.NONE);
//		cleanDirBtn.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				File diffFile=new File(diffLuaZipPath);
//				File[] diffFileArray=diffFile.listFiles();
//				for (int i = 0; i < diffFileArray.length; i++) {
//					diffFileArray[i].delete();
//				}
//				zipDiffList.removeAll();
//			}
//		});
//		cleanDirBtn.setBounds(560, 310, 100, 20);
//		cleanDirBtn.setText("清空文件夹");
	
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
		diffLuaZipPathText.setText(mConfig.diffLuaZipPath);
		diffLuaZipPathText.setToolTipText(mConfig.diffLuaZipPath);
		
		File oldFile=new File(mConfig.oldLuaZipPath);
		oldFileArray=oldFile.list();
		
		File newFile=new File(mConfig.newLuaZipPath);
		newFileArray=newFile.list();
		
		File diffFile=new File(mConfig.diffLuaZipPath);
		diffFileArray=diffFile.list();
		
		
		zipOldList.removeAll();
		zipNewList.removeAll();
		zipDiffList.removeAll();
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

		if(null!=diffFileArray)
		{
			for(int i=0; i<diffFileArray.length; ++i)
			{
				zipDiffList.add(diffFileArray[i]);
			}
		}
	}
}

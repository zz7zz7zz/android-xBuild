package com.open.autopkg.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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

import com.open.autopkg.data.DiffApkConfig;

public class ApkTab extends Composite 
{
	//UI-Data
	private DiffApkConfig mConfig=new DiffApkConfig();
	private String[]		diff_oldApkArray=null;
	private String[] 	diff_newApkArray=null;
	private String[] 	diff_patchArray=null;
	
	private String[]		merge_oldApkArray=null;
	private String[] 	merge_newApkArray=null;
	private String[] 	merge_patchArray=null;
	private ArrayList<String> 	mergepatchAvailableList=new ArrayList<String>();
	
	
	//UI
	private List 	diff_apkOldList;
	private List	diff_apkNewList;
	private List 	diff_apkPatchList;
	private Text 	diff_apkOldPathText;
	private Text 	diff_apkNewPathText;
	private Text 	diff_apkPatchPathText;
	private Text 	diff_logText;
	
	private List 	merge_apkOldList;
	private List 	merge_apkNewList;
	private List 	merge_apkPatchList;
	private Text 	merge_apkOldPathText;
	private Text 	merge_apkNewPathText;
	private Text 	merge_apkPatchPathText;
	private Text 	merge_logText;
	
	
	public ApkTab(Composite arg0, int arg1) 
	{
		super(arg0, arg1);
		
		mConfig.readConfig();
		initView();
		initViewData();
	}
	
	private void initView()
	{
		Group apkGroup = new Group(this, SWT.NONE);
		apkGroup.setBounds(0, 0, 1280, 750);
		
		Group oldApGroup=new Group(apkGroup, SWT.NONE);
		oldApGroup.setText("█ APK差分区域");
		oldApGroup.setBounds(10, 20, 1250, 330);
		
		
		Label jdkLabel = new Label(oldApGroup, SWT.NONE);
		jdkLabel.setBounds(10, 32, 80, 15);
		jdkLabel.setText("APK旧包 路径：");
		
		diff_apkOldPathText = new Text(oldApGroup, SWT.BORDER );
		diff_apkOldPathText.setBounds(100, 26, 500, 23);
		
		Button jdkBrowseBtn = new Button(oldApGroup,SWT.NONE);
		jdkBrowseBtn.setBounds(620,25,61,23);
		jdkBrowseBtn.setText("浏览");
		
		jdkBrowseBtn.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				super.widgetSelected(arg0);
				DirectoryDialog dd= new DirectoryDialog(ApkTab.this.getShell());
				if(new File(mConfig.diff_apkOldPath).isDirectory())
				{
					dd.setFilterPath(mConfig.diff_apkOldPath);
				}
				dd.setText("请选择你的 APK旧包 目录");
				String jdkFile = dd.open();
				if (jdkFile!=null)
				{
					File directiory = new File(jdkFile);
					mConfig.diff_apkOldPath=directiory.getPath();
					mConfig.saveConfig();
					initViewData();
				}
			}
		
		});
		
		Label adkLabel = new Label(oldApGroup, SWT.NONE);
		adkLabel.setText("APK新包 路径：");
		adkLabel.setBounds(10, 60, 80, 15);
		
		diff_apkNewPathText = new Text(oldApGroup, SWT.BORDER);
		diff_apkNewPathText.setBounds(100, 55, 500, 23);

		Button adkBrowseBtn = new Button(oldApGroup,SWT.NONE);
		adkBrowseBtn.setBounds(620,55,61,23);
		adkBrowseBtn.setText("浏览");
		adkBrowseBtn.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				super.widgetSelected(arg0);
				DirectoryDialog dd= new DirectoryDialog(ApkTab.this.getShell());
				if(new File(mConfig.diff_apkNewPath).isDirectory())
				{
					dd.setFilterPath(mConfig.diff_apkNewPath);
				}
				dd.setText("请选择你的 APK新包 目录");
				String sdkFile = dd.open();
				if (sdkFile!=null)
				{
					File directiory = new File(sdkFile);
					mConfig.diff_apkNewPath=directiory.getPath();
					mConfig.saveConfig();
					initViewData();
				}
			}
		});
		
		Label projectPathLabel = new Label(oldApGroup, SWT.NONE);
		projectPathLabel.setText("APK差分 路径：");
		projectPathLabel.setBounds(10, 88, 80, 15);
		
		diff_apkPatchPathText = new Text(oldApGroup, SWT.BORDER);
		diff_apkPatchPathText.setBounds(100, 88, 500, 23);

		
		Button projectPathBrowseBtn = new Button(oldApGroup,SWT.NONE);
		projectPathBrowseBtn.setBounds(620, 85, 61, 23);
		projectPathBrowseBtn.setText("浏览");
		projectPathBrowseBtn.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				super.widgetSelected(arg0);
				DirectoryDialog dd= new DirectoryDialog(ApkTab.this.getShell());
				if(new File(mConfig.diff_apkPatchPath).isDirectory())
				{
					dd.setFilterPath(mConfig.diff_apkPatchPath);
				}
				dd.setText("请选择你的 APK差分 目录");
				String sdkFile = dd.open();
				if (sdkFile!=null)
				{
					File directiory = new File(sdkFile);
					mConfig.diff_apkPatchPath=directiory.getPath();
					mConfig.saveConfig();
					initViewData();
				}
			}	
		});

		Label oldApkLabel = new Label(oldApGroup, SWT.NONE);
		oldApkLabel.setBounds(10, 130, 61, 15);
		oldApkLabel.setText("APK旧包：");

		diff_apkOldList= new List(oldApGroup, SWT.BORDER | SWT.V_SCROLL);
		diff_apkOldList.setBounds(10, 150, 180, 160);
		
		Label newApkLabel = new Label(oldApGroup, SWT.NONE);
		newApkLabel.setBounds(210, 130, 80, 15);
		newApkLabel.setText("APK新包：");
		
		diff_apkNewList = new List(oldApGroup, SWT.BORDER | SWT.V_SCROLL);
		diff_apkNewList.setBounds(210, 150, 180, 160);
		diff_apkNewList.addListener(SWT.MouseDoubleClick, new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				
			}
		});
		
		newApkLabel = new Label(oldApGroup, SWT.NONE);
		newApkLabel.setBounds(520, 130, 80, 15);
		newApkLabel.setText("Patch增量文件：");
		
		diff_apkPatchList = new List(oldApGroup, SWT.BORDER | SWT.V_SCROLL);
		diff_apkPatchList.setBounds(520, 150, 180, 160);
		diff_apkPatchList.addListener(SWT.MouseDoubleClick, new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				
			}
		});
		
		Button diffBtn = new Button(oldApGroup, SWT.NONE);
		diffBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				new Thread(new Runnable(){

					@Override
					public void run() {
						
						try {
									
									
									if(null!=diff_oldApkArray && null!=diff_newApkArray)
									{
											Display.getDefault().asyncExec(new Runnable() {
												
												@Override
												public void run() {
													diff_apkPatchList.removeAll();
													diff_logText.append("------------A:开始		生成差分文件：\r\n");
												}
											});
											String userDir = System.getProperty("user.dir");
											
											for (int j = 0; j < diff_oldApkArray.length; j++) 
											{
													final String patchName=diff_oldApkArray[j].substring(0, diff_oldApkArray[j].indexOf(".apk"))+"-"+diff_newApkArray[0].substring(0, diff_newApkArray[0].indexOf(".apk"))+".patch";
													String cmd=String.format("cmd /c %s  %s  %s %s",
															userDir+"\\diff_apk\\bsdiff.exe",
															mConfig.diff_apkOldPath+"\\"+diff_oldApkArray[j],
															mConfig.diff_apkNewPath+"\\"+diff_newApkArray[0],
															mConfig.diff_apkPatchPath+"\\"+patchName);
													
								//					Runtime.getRuntime().exec(cmds);
													Process process = Runtime.getRuntime().exec(cmd); 
													String strInfo = ""; 
													BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); 
													while((strInfo = reader.readLine()) != null)
													{ 
														System.out.println(strInfo); 
														//diff_logText.append("\r\n"+strInfo);
													}
													
													Display.getDefault().asyncExec(new Runnable() {
														
														@Override
														public void run() {
															diff_logText.append("\r\n------------:生成Patch文件："+patchName+"		成功\r\n");
															diff_apkPatchList.add(patchName);
														}
													});

											}
											
											Display.getDefault().asyncExec(new Runnable() {
												
												@Override
												public void run() {
													diff_logText.append("\r\n------------B:结束		生成差分文件：\r\n");
												}
											});
										}
								
					} catch (Exception e2) {
						e2.printStackTrace();
						Display.getDefault().asyncExec(new Runnable() {
							
							@Override
							public void run() {
								diff_logText.append("生成Patch文件：异常\r\n");
							}
						});
					}
						
					}
				}).start();
				
			}
		});
		diffBtn.setBounds(405, 200, 100, 50);
		diffBtn.setText("差分");
		
//		Button cleanPatchDirBtn = new Button(oldApGroup, SWT.NONE);
//		cleanPatchDirBtn.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				File patchFile=new File(diff_apkPatchPath);
//				File[] patchFileArray=patchFile.listFiles();
//				for (int i = 0; i < patchFileArray.length; i++) {
//					patchFileArray[i].delete();
//				}
//				diff_apkPatchList.removeAll();
//			}
//		});
//		cleanPatchDirBtn.setBounds(560, 310, 100, 20);
//		cleanPatchDirBtn.setText("清空文件夹");
	
/************************************************************************/
/**************************Log输出区域*********************************************/	
/*************************************************************************/
		Group apkdiffGroup = new Group(oldApGroup, SWT.NONE);
		apkdiffGroup.setText("Log输出");
		apkdiffGroup.setBounds(720, 30, 500, 290);
		diff_logText = new Text(apkdiffGroup, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		diff_logText.setBounds(10, 20, 490, 270);
		diff_logText.addMenuDetectListener(new MenuDetectListener() {
			public void menuDetected(MenuDetectEvent e) {
				
			}
		});
		diff_logText.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		diff_logText.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
//		logText.setEditable(false);
		diff_logText.setEditable(true);
		
		//----------------合成
		
		Group mergeGroup=new Group(apkGroup, SWT.NONE);
		mergeGroup.setText("█ APK合成区域");
		mergeGroup.setBounds(10, 360, 1250, 330);
		
		Label mergeGroupjdkLabel = new Label(mergeGroup, SWT.NONE);
		mergeGroupjdkLabel.setBounds(10, 32, 80, 15);
		mergeGroupjdkLabel.setText("APK旧包 路径：");
		
		merge_apkOldPathText = new Text(mergeGroup, SWT.BORDER );
		merge_apkOldPathText.setBounds(100, 26, 500, 23);
		
		Button mergeGroupjdkBrowseBtn = new Button(mergeGroup,SWT.NONE);
		mergeGroupjdkBrowseBtn.setBounds(620,25,61,23);
		mergeGroupjdkBrowseBtn.setText("浏览");
		mergeGroupjdkBrowseBtn.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				super.widgetSelected(arg0);
				DirectoryDialog dd= new DirectoryDialog(ApkTab.this.getShell());
				if(new File(mConfig.merge_apkOldPath).isDirectory())
				{
					dd.setFilterPath(mConfig.merge_apkOldPath);
				}
				dd.setText("请选择你的 APK旧包 目录");
				String jdkFile = dd.open();
				if (jdkFile!=null)
				{
					File directiory = new File(jdkFile);
					mConfig.merge_apkOldPath=directiory.getPath();
					mConfig.saveConfig();
					initViewData();
				}
			}
		
		});
		
		Label mergeGroupadkLabel = new Label(mergeGroup, SWT.NONE);
		mergeGroupadkLabel.setText("APK新包 路径：");
		mergeGroupadkLabel.setBounds(10, 60, 80, 15);
		
		merge_apkNewPathText = new Text(mergeGroup, SWT.BORDER);
		merge_apkNewPathText.setBounds(100, 55, 500, 23);

		Button mergeGroupadkBrowseBtn = new Button(mergeGroup,SWT.NONE);
		mergeGroupadkBrowseBtn.setBounds(620,55,61,23);
		mergeGroupadkBrowseBtn.setText("浏览");
		mergeGroupadkBrowseBtn.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				super.widgetSelected(arg0);
				DirectoryDialog dd= new DirectoryDialog(ApkTab.this.getShell());
				if(new File(mConfig.merge_apkNewPath).isDirectory())
				{
					dd.setFilterPath(mConfig.merge_apkNewPath);
				}
				dd.setText("请选择你的 APK新包 目录");
				String sdkFile = dd.open();
				if (sdkFile!=null)
				{
					File directiory = new File(sdkFile);
					mConfig.merge_apkNewPath=directiory.getPath();
					mConfig.saveConfig();
					initViewData();
				}
			}
		});
		
		Label mergeGroupprojectPathLabel = new Label(mergeGroup, SWT.NONE);
		mergeGroupprojectPathLabel.setText("APK差分 路径：");
		mergeGroupprojectPathLabel.setBounds(10, 88, 80, 15);
		
		merge_apkPatchPathText = new Text(mergeGroup, SWT.BORDER);
		merge_apkPatchPathText.setBounds(100, 88, 500, 23);

		
		Button mergeGroupprojectPathBrowseBtn = new Button(mergeGroup,SWT.NONE);
		mergeGroupprojectPathBrowseBtn.setBounds(620, 85, 61, 23);
		mergeGroupprojectPathBrowseBtn.setText("浏览");
		mergeGroupprojectPathBrowseBtn.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				super.widgetSelected(arg0);
				DirectoryDialog dd= new DirectoryDialog(ApkTab.this.getShell());
				if(new File(mConfig.merge_apkPatchPath).isDirectory())
				{
					dd.setFilterPath(mConfig.merge_apkPatchPath);
				}
				dd.setText("请选择你的 APK差分 目录");
				String sdkFile = dd.open();
				if (sdkFile!=null)
				{
					File directiory = new File(sdkFile);
					mConfig.merge_apkPatchPath=directiory.getPath();
					mConfig.saveConfig();
					initViewData();
				}
			}	
		});

		Label mergeGroupoldApkLabel = new Label(mergeGroup, SWT.NONE);
		mergeGroupoldApkLabel.setBounds(10, 130, 61, 15);
		mergeGroupoldApkLabel.setText("APK旧包：");

		merge_apkOldList = new List(mergeGroup, SWT.BORDER | SWT.V_SCROLL);
		merge_apkOldList.setBounds(10, 150, 180, 160);
		merge_apkOldList.addMouseListener(new MouseAdapter() {
	        public void mouseUp(MouseEvent e) {
	          String[] selectArray=merge_apkOldList.getSelection();
	          System.out.println("merge_apkOldList:"+selectArray[0]);
	         if(null!=selectArray[0] && selectArray.length>0)
	         {
	        	 merge_apkPatchList.removeAll();
	        	 mergepatchAvailableList.clear();
	        	 
	        	String prefix= selectArray[0].substring(0, selectArray[0].indexOf(".apk"));
	     		if(null!=merge_patchArray)
	    		{
	    			for(int i=0; i<merge_patchArray.length; ++i)
	    			{
	    				if(merge_patchArray[i].startsWith(prefix))
	    				{
	    					merge_apkPatchList.add(merge_patchArray[i]);
	    					mergepatchAvailableList.add(merge_patchArray[i]);
	    				}
	    			}
	    		}
	         }
	        }
	      });
		
		Label mergeGroupnewApkLabel = new Label(mergeGroup, SWT.NONE);
		mergeGroupnewApkLabel.setBounds(210, 130, 80, 15);
		mergeGroupnewApkLabel.setText("Patch增量文件：");
		
		merge_apkPatchList = new List(mergeGroup, SWT.BORDER | SWT.V_SCROLL);
		merge_apkPatchList.setBounds(210, 150, 180, 160);
		merge_apkPatchList.addMouseListener(new MouseAdapter() {
	        public void mouseUp(MouseEvent e) {
	          String[] selectArray=merge_apkPatchList.getSelection();
	          System.out.println("merge_apkPatchList:"+selectArray[0]);
	        }
	      });
		
		mergeGroupnewApkLabel = new Label(mergeGroup, SWT.NONE);
		mergeGroupnewApkLabel.setBounds(520, 130, 80, 15);
		mergeGroupnewApkLabel.setText("APK新包：");
		
		merge_apkNewList = new List(mergeGroup, SWT.BORDER | SWT.V_SCROLL);
		merge_apkNewList.setBounds(520, 150, 180, 160);
		merge_apkNewList.addListener(SWT.MouseDoubleClick, new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				
			}
		});
		
		Button mergeGroupdiffBtn = new Button(mergeGroup, SWT.NONE);
		mergeGroupdiffBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				final String[] oldselectArray=merge_apkOldList.getSelection();
				final String[] patchSelectArray=merge_apkPatchList.getSelection();
				
				new Thread(new Runnable(){

					@Override
					public void run() {
						
						try {
									if(null!=oldselectArray&&oldselectArray.length>0 && null!=patchSelectArray&&patchSelectArray.length>0)
									{
											Display.getDefault().asyncExec(new Runnable() {
												
												@Override
												public void run() {
													merge_apkNewList.removeAll();
													merge_logText.append("------------A:开始		合成APK文件：\r\n");
												}
											});
											
											String userDir = System.getProperty("user.dir");
											for (int j = 0; j <patchSelectArray.length; j++) 
											{
													
													final String newApkName=patchSelectArray[j].substring(patchSelectArray[j].indexOf("-")+1).replace("patch", "apk");
													
													String cmd=String.format("cmd /c %s  %s  %s %s",
															userDir+"\\diff_apk\\bspatch.exe",
															mConfig.merge_apkOldPath+"\\"+oldselectArray[0],
															mConfig.merge_apkNewPath+"\\"+newApkName,
															mConfig.merge_apkPatchPath+"\\"+patchSelectArray[j]);
													
								//					Runtime.getRuntime().exec(cmds);
													Process process = Runtime.getRuntime().exec(cmd); 
													String strInfo = ""; 
													BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); 
													while((strInfo = reader.readLine()) != null)
													{ 
														System.out.println(strInfo); 
													}
													
													Display.getDefault().asyncExec(new Runnable() {
														
														@Override
														public void run() {
															merge_logText.append("\r\n------------:合成文件："+newApkName+"		成功\r\n");
															merge_apkNewList.add(newApkName);
														}
													});

											}
											
											Display.getDefault().asyncExec(new Runnable() {
												
												@Override
												public void run() {
													merge_logText.append("------------B:结束		合成APK文件：\r\n");
												}
											});
										}
								
					} catch (Exception e2) {
						e2.printStackTrace();
						Display.getDefault().asyncExec(new Runnable() {
							
							@Override
							public void run() {
								merge_logText.append("合成文件：异常\r\n");
							}
						});
					}
						
					}
				}).start();
				
			}
		});
		mergeGroupdiffBtn.setBounds(405, 200, 100, 50);
		mergeGroupdiffBtn.setText("合成");
		
//		Button cleanNewApkDirBtn = new Button(mergeGroup, SWT.NONE);
//		cleanNewApkDirBtn.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				File newApkFileDir=new File(merge_apkNewPath);
//				File[] newApkFileArray=newApkFileDir.listFiles();
//				for (int i = 0; i < newApkFileArray.length; i++) {
//					newApkFileArray[i].delete();
//				}
//				merge_apkNewList.removeAll();
//			}
//		});
//		cleanNewApkDirBtn.setBounds(560, 310, 100, 20);
//		cleanNewApkDirBtn.setText("清空文件夹");
	
/************************************************************************/
/**************************Log输出区域*********************************************/	
/*************************************************************************/
		Group mergeGroupapkdiffGroup = new Group(mergeGroup, SWT.NONE);
		mergeGroupapkdiffGroup.setText("Log输出");
		mergeGroupapkdiffGroup.setBounds(720, 30, 500, 290);
		merge_logText = new Text(mergeGroupapkdiffGroup, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		merge_logText.setBounds(10, 20, 490, 260);
		merge_logText.addMenuDetectListener(new MenuDetectListener() {
			public void menuDetected(MenuDetectEvent e) {
				
			}
		});
		merge_logText.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		merge_logText.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
//		logText.setEditable(false);
		merge_logText.setEditable(true);
	
	}


	private void initViewData()
	{
		//差分
		diff_apkOldPathText.setText(mConfig.diff_apkOldPath);
		diff_apkOldPathText.setToolTipText(mConfig.diff_apkOldPath);
		diff_apkNewPathText.setText(mConfig.diff_apkNewPath);
		diff_apkNewPathText.setToolTipText(mConfig.diff_apkNewPath);
		diff_apkPatchPathText.setText(mConfig.diff_apkPatchPath);
		diff_apkPatchPathText.setToolTipText(mConfig.diff_apkPatchPath);
		
		File oldFile=new File(mConfig.diff_apkOldPath);
		diff_oldApkArray=oldFile.list();
		
		File newFile=new File(mConfig.diff_apkNewPath);
		diff_newApkArray=newFile.list();
		
		File patchFile=new File(mConfig.diff_apkPatchPath);
		diff_patchArray=patchFile.list();
		
		diff_apkOldList.removeAll();
		diff_apkNewList.removeAll();
		diff_apkPatchList.removeAll();
		
		diff_logText.setText("");
		
		if(null!=diff_oldApkArray)
		{
			for(int i=0; i<diff_oldApkArray.length; ++i)
			{
				diff_apkOldList.add(diff_oldApkArray[i]);
			}
		}
		
		if(null!=diff_newApkArray)
		{
			for(int i=0; i<diff_newApkArray.length; ++i)
			{
				diff_apkNewList.add(diff_newApkArray[i]);
			}
		}

		if(null!=diff_patchArray)
		{
			for(int i=0; i<diff_patchArray.length; ++i)
			{
				diff_apkPatchList.add(diff_patchArray[i]);
			}
		}
		
		//合成
		merge_apkOldPathText.setText(mConfig.merge_apkOldPath);
		merge_apkOldPathText.setToolTipText(mConfig.merge_apkOldPath);
		merge_apkNewPathText.setText(mConfig.merge_apkNewPath);
		merge_apkNewPathText.setToolTipText(mConfig.merge_apkNewPath);
		merge_apkPatchPathText.setText(mConfig.merge_apkPatchPath);
		merge_apkPatchPathText.setToolTipText(mConfig.merge_apkPatchPath);
		
		File  mergeoldFile=new File(mConfig.merge_apkOldPath);
		merge_oldApkArray=mergeoldFile.list();
		
		File  mergenewFile=new File(mConfig.merge_apkNewPath);
		merge_newApkArray=mergenewFile.list();
		
		File mergepatchFile=new File(mConfig.merge_apkPatchPath);
		merge_patchArray=mergepatchFile.list();
		
		merge_apkOldList.removeAll();
		merge_apkNewList.removeAll();
		merge_apkPatchList.removeAll();
		
		merge_logText.setText("");
		
		if(null!=merge_oldApkArray)
		{
			for(int i=0; i<merge_oldApkArray.length; ++i)
			{
				merge_apkOldList.add(merge_oldApkArray[i]);
			}
		}
		
		if(null!=merge_newApkArray)
		{
			for(int i=0; i<merge_newApkArray.length; ++i)
			{
				merge_apkNewList.add(merge_newApkArray[i]);
			}
		}

		if(null!=merge_patchArray)
		{
			for(int i=0; i<merge_patchArray.length; ++i)
			{
				merge_apkPatchList.add(merge_patchArray[i]);
			}
		}
		
	}
}

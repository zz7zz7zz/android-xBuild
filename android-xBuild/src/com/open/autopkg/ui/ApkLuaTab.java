package com.open.autopkg.ui;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.open.autopkg.apk.AutoBuilder;
import com.open.autopkg.apk.AutoBuilder.IAutoBuild;
import com.open.autopkg.data.Config;
import com.open.autopkg.data.Config.ChannelBean;
import com.open.autopkg.data.Config.LuaZipBean;
import com.open.autopkg.data.Constant;
import com.open.autopkg.data.PropertyBean;
import com.open.autopkg.lua.LuaInfo;
import com.open.autopkg.util.FileUtil;
import com.open.autopkg.xml.ManifestParser;

public class ApkLuaTab extends Composite {
	
	//Config-Data
	private Config mConfig=new Config();
	private String pkgName;
	private String pkgVersionName;
	private String pkgVersionCode;
	private LuaInfo mLuaInfo;
	
	//UI-Data
	private int selectedProjectIndex = 0;//当前选择的工程索引
	private java.util.List<Integer> leftChannels=new ArrayList<Integer>();//所选择的渠道
	private java.util.List<Integer> rightChannels=new ArrayList<Integer>();//所选择的渠道

	private final int time_compile=75*1000;//假设编译时间为75秒
	private final int time_buildPerApk=130*1000;//假设打一个包花费的时间为130秒
	private int time_buildAll;
	private long time_startBuild;
	private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 4, 3,  
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),  
            new ThreadPoolExecutor.DiscardOldestPolicy()); 
	private boolean isTaskRunning=true;
	//UI
	private Text jdkText;
	private Text adkText;
	private Text projectPathText;
	private Text luaPathText;
	
	
	private Text apkPkgNameText;
	private Text apkVersionNameText;
	private Text apkVersionCodeText;
	private Text hallVersionText;
	private Text dependApkText;
	private List channelLeftList;
	private List channelRightList;
	private Text logText;

	private Table luaGameInfoTable;
	private TableColumn tcGameName;
	private TableColumn tcGameVersion;

	private Button androidBtn;
	private Button iosBtn;
	private Button releaseBtn;
	private Button debugBtn;
	private Button oneKeyApkBtn;
	private ProgressBar buildProgressBar;
	private Label buildProgressLabel;

	public ApkLuaTab(Composite arg0, int arg1) {
		super(arg0, arg1);
		
		initData();
		initView();
	}
	
	private void initView()
	{
		/************************************************************************************************/
		/*************************************参数选项区域*****************************************************/
		/************************************************************************************************/
				Group apkLuaGroup = new Group(this, SWT.NONE);
				apkLuaGroup.setBounds(0, 0, 1280, 750);
				
				Group envGroup = new Group(apkLuaGroup, SWT.NONE);
				envGroup.setText("█ 参数选项");
				envGroup.setBounds(10, 10, 900, 170);
			
				final Combo combo = new Combo(envGroup, SWT.READ_ONLY);
				for(int i=0; i<mConfig.projectList.size(); ++i)
				{
					combo.add(mConfig.projectList.get(i).projectName, i);
				}
				combo.select(0);
				combo.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						selectedProjectIndex=combo.getSelectionIndex();
						initViewData();
					}
				});
				combo.setBounds(77, 17, 600, 23);

				Label projectLabel = new Label(envGroup, SWT.NONE);
				projectLabel.setBounds(10, 20, 61, 15);
				projectLabel.setText("工程名称：");
				
				Label jdkLabel = new Label(envGroup, SWT.NONE);
				jdkLabel.setBounds(10, 52, 61, 15);
				jdkLabel.setText("JDK路径：");
				
				jdkText = new Text(envGroup, SWT.BORDER );
				jdkText.setBounds(77, 46, 600, 23);
				
				Button jdkBrowseBtn = new Button(envGroup,SWT.NONE);
				jdkBrowseBtn.setBounds(700,50,61,23);
				jdkBrowseBtn.setText("浏览");
				jdkBrowseBtn.addSelectionListener(new SelectionAdapter(){

					@Override
					public void widgetSelected(SelectionEvent arg0) {

						super.widgetSelected(arg0);
						DirectoryDialog dd= new DirectoryDialog(ApkLuaTab.this.getShell());
						if(new File(mConfig.jdkDir).isDirectory())
						{
							dd.setFilterPath(mConfig.jdkDir);
						}
						dd.setText("请选择你的 JDK 目录");
						String jdkFile = dd.open();
						if (jdkFile!=null)
						{
							File directiory = new File(jdkFile);
							jdkText.setText(directiory.getPath());
							mConfig.jdkDir=jdkText.getText();
							
							mConfig.saveConfig(selectedProjectIndex);
							initViewData();
						}
					}
				
				});
				
				Label adkLabel = new Label(envGroup, SWT.NONE);
				adkLabel.setText("ADK路径：");
				adkLabel.setBounds(10, 81, 61, 15);
				
				adkText = new Text(envGroup, SWT.BORDER);
				adkText.setBounds(77, 75, 600, 23);

				Button adkBrowseBtn = new Button(envGroup,SWT.NONE);
				adkBrowseBtn.setBounds(700,75,61,23);
				adkBrowseBtn.setText("浏览");
				adkBrowseBtn.addSelectionListener(new SelectionAdapter(){

					@Override
					public void widgetSelected(SelectionEvent arg0) {

						super.widgetSelected(arg0);
						DirectoryDialog dd= new DirectoryDialog(ApkLuaTab.this.getShell());
						if(new File(mConfig.sdkDir).isDirectory())
						{
							dd.setFilterPath(mConfig.sdkDir);
						}
						dd.setText("请选择你的 Android SDK 目录");
						String sdkFile = dd.open();
						if (sdkFile!=null)
						{
							File directiory = new File(sdkFile);
							adkText.setText(directiory.getPath());
							mConfig.sdkDir=adkText.getText();
							
							mConfig.saveConfig(selectedProjectIndex);
							initViewData();
						}
					}
				});
				
				Label projectPathLabel = new Label(envGroup, SWT.NONE);
				projectPathLabel.setText("Android ：");
				projectPathLabel.setBounds(10, 108, 61, 15);
				
				projectPathText = new Text(envGroup, SWT.BORDER);
				projectPathText.setBounds(77, 102, 600, 23);

				
				Button projectPathBrowseBtn = new Button(envGroup,SWT.NONE);
				projectPathBrowseBtn.setBounds(700, 102, 61, 23);
				projectPathBrowseBtn.setText("浏览");
				projectPathBrowseBtn.addSelectionListener(new SelectionAdapter(){

					@Override
					public void widgetSelected(SelectionEvent arg0) {

						super.widgetSelected(arg0);
						DirectoryDialog dd= new DirectoryDialog(ApkLuaTab.this.getShell());
						if(new File(mConfig.projectList.get(selectedProjectIndex).projectApkDir).isDirectory())
						{
							dd.setFilterPath(mConfig.projectList.get(selectedProjectIndex).projectApkDir);
						}
						dd.setText("请选择你的 Android工程 目录");
						String sdkFile = dd.open();
						if (sdkFile!=null)
						{
							File directiory = new File(sdkFile);
							projectPathText.setText(directiory.getPath());
							mConfig.projectList.get(selectedProjectIndex).projectApkDir=projectPathText.getText();
							
							mConfig.saveConfig(selectedProjectIndex);
							initViewData();
						}
					}	
				});
				
				Label luaLabel = new Label(envGroup, SWT.NONE);
				luaLabel.setText("Lua 路径：");
				luaLabel.setBounds(10, 138, 61, 15);
				
				luaPathText = new Text(envGroup, SWT.BORDER);
				luaPathText.setBounds(77, 132, 600, 23);

				Button luaPathBrowseBtn = new Button(envGroup,SWT.NONE);
				luaPathBrowseBtn.setBounds(700, 132, 61, 23);
				luaPathBrowseBtn.setText("浏览");
				luaPathBrowseBtn.addSelectionListener(new SelectionAdapter(){

					@Override
					public void widgetSelected(SelectionEvent arg0) {

						super.widgetSelected(arg0);
						DirectoryDialog dd= new DirectoryDialog(ApkLuaTab.this.getShell());
						if(new File(mConfig.projectList.get(selectedProjectIndex).projectLuaDir).isDirectory())
						{
							dd.setFilterPath(mConfig.projectList.get(selectedProjectIndex).projectLuaDir);
						}
						dd.setText("请选择你的 Lua工程 目录");
						String sdkFile = dd.open();
						if (sdkFile!=null)
						{
							File directiory = new File(sdkFile);
							luaPathText.setText(directiory.getPath());
							mConfig.projectList.get(selectedProjectIndex).projectLuaDir=luaPathText.getText();
							
							mConfig.saveConfig(selectedProjectIndex);
							initViewData();
						}
					}	
				});
				
				Button initEnvBtn = new Button(envGroup,SWT.NONE);
				initEnvBtn.setBounds(780,40,100,118);
				initEnvBtn.setText("初始化环境");
				initEnvBtn.addSelectionListener(new SelectionAdapter(){

					@Override
					public void widgetSelected(SelectionEvent arg0) {

						super.widgetSelected(arg0);
						
						mConfig.jdkDir=jdkText.getText();
						mConfig.sdkDir=adkText.getText();
						mConfig.projectList.get(selectedProjectIndex).projectApkDir=projectPathText.getText();
						mConfig.projectList.get(selectedProjectIndex).projectLuaDir=luaPathText.getText();
						mConfig.saveConfig(selectedProjectIndex);
						
						initData();
						initViewData();
					}
				});
				

		/********************************************************************************************/
		/********************************应用/大厅/游戏属性************************************************************/
		/*********************************************************************************************/

				
				Group attGroup = new Group(apkLuaGroup, SWT.NONE);
				attGroup.setText("█ 应用/大厅/游戏属性");
				attGroup.setBounds(10, 200, 900, 160);
				
				Group apkInfoGroup= new Group(attGroup, SWT.NONE);
				apkInfoGroup.setText("APK信息");
				apkInfoGroup.setBounds(10, 20, 285, 120);
				
				Label apkPkgNameLabel = new Label(apkInfoGroup, SWT.NONE);
				apkPkgNameLabel.setBounds(10, 18, 110, 15);
				apkPkgNameLabel.setText("Package Name:");
				
				apkPkgNameText = new Text(apkInfoGroup, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
				apkPkgNameText.setBounds(120, 18, 152, 21);
				
				Label apkVersionNameLabel = new Label(apkInfoGroup, SWT.NONE);
				apkVersionNameLabel.setText("Version Name:");
				apkVersionNameLabel.setBounds(10, 50, 110, 15);
				
				apkVersionNameText = new Text(apkInfoGroup, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
				apkVersionNameText.setBounds(120, 50, 152, 21);
				
				Label apkVersionCodeLabel = new Label(apkInfoGroup, SWT.NONE);
				apkVersionCodeLabel.setText("Version  Code:");
				apkVersionCodeLabel.setBounds(10, 80, 110, 15);
				
				apkVersionCodeText = new Text(apkInfoGroup, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
				apkVersionCodeText.setBounds(120, 80, 152, 21);
				
				Group hallInfoGroup = new Group(attGroup, SWT.NONE);
				hallInfoGroup.setText("大厅");
				hallInfoGroup.setBounds(300, 20, 240, 120);
				
				Label hallVersionLabel = new Label(hallInfoGroup, SWT.NONE);
				hallVersionLabel.setBounds(10, 25, 61, 15);
				hallVersionLabel.setText("版本号：");
				
				hallVersionText = new Text(hallInfoGroup, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
				hallVersionText.setBounds(100, 23, 120, 21);
				
				Label dependApkLabel = new Label(hallInfoGroup, SWT.NONE);
				dependApkLabel.setBounds(10, 50, 82, 15);
				dependApkLabel.setText("依赖apk版本：");
				
				dependApkText = new Text(hallInfoGroup, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
				dependApkText.setToolTipText("大厅依赖的apk版本必须与VersionName一致");
				dependApkText.setBounds(100, 50, 120, 21);
				
				Group luaGameInfoGroup = new Group(attGroup, SWT.NONE);
				luaGameInfoGroup.setText("游戏");
				luaGameInfoGroup.setBounds(550, 20, 300, 130);
				
				luaGameInfoTable = new Table(luaGameInfoGroup, SWT.BORDER | SWT.FULL_SELECTION);
				luaGameInfoTable.setBounds(10, 20, 280, 116);
				luaGameInfoTable.setHeaderVisible(true);
				luaGameInfoTable.setLinesVisible(true);
				
				tcGameName = new TableColumn(luaGameInfoTable,SWT.CENTER);
				tcGameName.setText("游戏名称");
				tcGameName.setWidth(120);
				
				tcGameVersion = new TableColumn(luaGameInfoTable,SWT.CENTER);
				tcGameVersion.setText("版本号");
				tcGameVersion.setWidth(80);

		/************************************************************************************************/
		/*************************************打包设置******************************************************/
		/************************************************************************************************/
				Group channelGroup=new Group(apkLuaGroup, SWT.NONE);
				channelGroup.setText("█ 打包设置区域");
				channelGroup.setBounds(10, 370, 900, 300);

				Label channelLeftListLabel = new Label(channelGroup, SWT.NONE);
				channelLeftListLabel.setBounds(10, 30, 61, 15);
				channelLeftListLabel.setText("渠道列表：");

				channelLeftList = new List(channelGroup, SWT.BORDER | SWT.V_SCROLL);
				channelLeftList.addListener(SWT.MouseDoubleClick, new Listener() {
					
					@Override
					public void handleEvent(Event e) {

						if(channelLeftList.getItemCount() > 0)
						{
							if(e.y <= channelLeftList.getItemHeight()*(channelLeftList.getSelectionIndex()+1))
							{
								int iselected = channelLeftList.getSelectionIndex();
								if(iselected == -1)
								{
									showTips("至少选择一项！");
									return;
								}
								addToright(iselected);
							}
						}
					}
				});
				channelLeftList.setBounds(10, 50, 180, 235);
				
				java.util.List<ChannelBean> channels = mConfig.projectList.get(selectedProjectIndex).channelList;
				for(int i=0; i<channels.size(); ++i)
				{
					ChannelBean itmChannel = channels.get(i);
					channelLeftList.add(String.format("%s(%s)", itmChannel.channelId, itmChannel.channelName));
					
					leftChannels.add(i);
				}
				if(channels.size()>0)
				{
					channelLeftList.select(0);
				}
				
				Label channelRightListLabel = new Label(channelGroup, SWT.NONE);
				channelRightListLabel.setBounds(295, 30, 80, 15);
				channelRightListLabel.setText("已选择渠道：");
				
				channelRightList = new List(channelGroup, SWT.BORDER | SWT.V_SCROLL);
				channelRightList.setBounds(295, 50, 180, 235);
				channelRightList.addListener(SWT.MouseDoubleClick, new Listener() {
					
					@Override
					public void handleEvent(Event e) {

						if(channelRightList.getItemCount() > 0){
							if(e.y <= channelRightList.getItemHeight()*(channelRightList.getSelectionIndex()+1)){
								int iselected = channelRightList.getSelectionIndex();
								if(iselected == -1){
									showTips("选择一项！");
									return;
								}
								addToleft(iselected);
							}
						}
					}
				});
				
				Button rightArrowBtn = new Button(channelGroup, SWT.NONE);
				rightArrowBtn.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if(channelLeftList.getItemCount()>0){
							int iselected = channelLeftList.getSelectionIndex();
							if(iselected == -1){
								showTips("选择一项！");
								return;
							}
							addToright(iselected);
						}
					}
				});
				rightArrowBtn.setBounds(200, 70, 80, 25);
				rightArrowBtn.setText(">");
				
				Button leftArrowBtn = new Button(channelGroup, SWT.NONE);
				leftArrowBtn.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if(channelRightList.getItemCount()>0){
							int iselected = channelRightList.getSelectionIndex();
							if(iselected == -1){
								showTips("选择一项！");
								return;
							}
							addToleft(iselected);
						}
					}
				});
				leftArrowBtn.setText("<");
				leftArrowBtn.setBounds(200,130, 80, 25);
				
				Button allRightArrowBtn = new Button(channelGroup, SWT.NONE);
				allRightArrowBtn.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						while(true)
						{
							addToright(0);
							if(channelLeftList.getItemCount()<=0)
							{
								break;
							}
						}
					}
				});
				allRightArrowBtn.setBounds(200, 190, 80, 25);
				allRightArrowBtn.setText(">>");
				
				Button allLeftArrowBtn = new Button(channelGroup, SWT.NONE);
				allLeftArrowBtn.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						while(true)
						{
							addToleft(0);
							if(channelRightList.getItemCount()<=0)
							{
								break;
							}
						}
					}
				});
				allLeftArrowBtn.setBounds(200, 250, 80, 25);
				allLeftArrowBtn.setText("<<");
			
		/************************************************************************/
		/**************************Log输出区域*********************************************/	
		/*************************************************************************/
				Group logGroup = new Group(channelGroup, SWT.NONE);
				logGroup.setText("Log输出");
				logGroup.setBounds(490, 30, 400, 260);
				logText = new Text(logGroup, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
				logText.setBounds(10, 20, 390, 240);
				logText.addMenuDetectListener(new MenuDetectListener() {
					public void menuDetected(MenuDetectEvent e) {
						
					}
				});
				logText.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
				logText.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
//				logText.setEditable(false);
				logText.setEditable(true);
				

		/************************************************************************************************/
		/*************************************打包区域******************************************************/
		/************************************************************************************************/		
				
				Group packAppGroup=new Group(apkLuaGroup, SWT.NONE);
				packAppGroup.setText("█  打包区域");
				packAppGroup.setBounds(920, 10, 350, 400);
				
				Group platformGroup = new Group(packAppGroup, SWT.NONE);
				platformGroup.setText(" ");
				platformGroup.setBounds(10, 20, 320,40);
				
				androidBtn = new Button(platformGroup, SWT.RADIO);
				androidBtn.setBounds(20, 15, 97, 16);
				androidBtn.setText("Android");
				androidBtn.setSelection(true);
				
				iosBtn = new Button(platformGroup, SWT.RADIO);
				iosBtn.setBounds(150, 15, 97, 16);
				iosBtn.setText("IOS");
				
				Group TestGroup = new Group(packAppGroup, SWT.NONE);
				TestGroup.setText(" ");
				TestGroup.setBounds(10,65, 320,40);
				
				releaseBtn = new Button(TestGroup, SWT.RADIO);
				releaseBtn.setBounds(20, 15, 97, 16);
				releaseBtn.setText("正式");
				releaseBtn.setSelection(true);
				
				debugBtn = new Button(TestGroup, SWT.RADIO);
				debugBtn.setBounds(150,15, 97, 16);
				debugBtn.setText("测试");
				
				Group luaZipGroup = new Group(packAppGroup, SWT.NONE);
				luaZipGroup.setText("生成zip包 (用于上传至后台服务器)");
				luaZipGroup.setBounds(10,120, 340, 60);
				
				Button oneKeyLuaZipBtn = new Button(luaZipGroup, SWT.NONE);
				oneKeyLuaZipBtn.setBounds(10, 20,320, 35);
				oneKeyLuaZipBtn.setText("一键生成zip包");
				oneKeyLuaZipBtn.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						
						StringBuilder gameZipsSb=new StringBuilder();
						for (int i = 0; i < mConfig.LuaZipList.size(); i++) 
						{
								if(!"hall".equals(mConfig.LuaZipList.get(i).gamePkg))
								{
									gameZipsSb.append(mConfig.LuaZipList.get(i).gamePkg+" ");
								}
						}
						
						String userDir = System.getProperty("user.dir");
						
						StringBuilder cmds=new StringBuilder();
						cmds.append( "cmd /c start " + userDir +"\\build_scqp\\sync_publish_gameZip.bat");
						cmds.append(" ");
						cmds.append(iosBtn.getSelection() ? "ios" :"android");
						cmds.append(" ");
						cmds.append(mConfig.projectList.get(selectedProjectIndex).projectApkDir);
						cmds.append(" ");
						cmds.append(gameZipsSb.toString());
	
						
						final String excuteCmd=cmds.toString();
						new Thread(new Runnable(){

							@Override
							public void run() {
								try {
									
									appendLogs("\r\n--------------A:Zip打包开始-----------\r\n");
									appendLogs("--------------B:执行命令："+excuteCmd+"\r\n");
									
//									Runtime.getRuntime().exec(cmds);
									Process process = Runtime.getRuntime().exec(excuteCmd); 
									String strInfo = ""; 
									BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); 
									while((strInfo = reader.readLine()) != null)
									{ 
										System.out.println(strInfo); 
									}
									appendLogs("--------------C:Zip打包结束-----------\r\n");
									
									

									appendLogs("--------------D:拷贝文件开始-----------\r\n");
									
									String gameZipsPath=mConfig.projectList.get(selectedProjectIndex).projectApkDir+"\\gameZips";
									String destZipPath=System.getProperty("user.dir")+"\\apkgames\\";
									FileUtil.createDir(destZipPath);
									File dir=new File(gameZipsPath);
									String zipArray[]=dir.list();
									if(null!=zipArray)
									{
										FileUtil.delAllFile(destZipPath);
										for (int j = 0; j < zipArray.length; j++) 
										{
												FileUtil.copyFile(gameZipsPath+"\\"+zipArray[j], destZipPath+zipArray[j]);
												appendLogs("移动文件"+j+":"+zipArray[j]+"		成功\r\n");
										}
										
									}
									appendLogs("--------------E:拷贝文件结束-----------\r\n\r\n");
									
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							
						}).start();
						
						
					}
				});

			
				Group apkGroup = new Group(packAppGroup, SWT.NONE);
				apkGroup.setText("生成apk包 (可选内置游戏):");
				apkGroup.setBounds(10, 190, 340,200);
				
				final Button zipBtnArray[]=new Button[mConfig.LuaZipList.size()];
				for (int i = 0; i < zipBtnArray.length; i++) 
				{
					Button itemBtn = new Button(apkGroup,SWT.CHECK);
					itemBtn.setBounds(10+(i%4)*80,20+25*(i/4),80,16);
					itemBtn.setText(mConfig.LuaZipList.get(i).gameName);
					itemBtn.setData(mConfig.LuaZipList.get(i));
					itemBtn.setSelection("hall".equals(mConfig.LuaZipList.get(i).gamePkg));
					itemBtn.setEnabled(! "hall".equals(mConfig.LuaZipList.get(i).gamePkg));
					
					zipBtnArray[i]=itemBtn;
				}
				
				buildProgressBar = new ProgressBar(apkGroup, SWT.SMOOTH|SWT.INDETERMINATE);
				buildProgressBar.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				buildProgressBar.setBounds(10, 70,280,10);
				buildProgressBar.setVisible(false);
				

				buildProgressLabel = new Label(apkGroup, SWT.NONE);
				buildProgressLabel.setBounds(300,65,50,20);
				buildProgressLabel.setText("0%");
				buildProgressLabel.setVisible(false);
				
				/** 和内置游戏编译在一起 */
				oneKeyApkBtn = new Button(apkGroup, SWT.NONE);
				oneKeyApkBtn.setBounds(10,90,320,40);
				oneKeyApkBtn.setText("一键生成apk");		
				oneKeyApkBtn.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						
						if(rightChannels.size()<=0)
						{
							showTips("请选择要打包到渠道！");
							return;
						}
						else if(!pkgVersionName.equals(mLuaInfo.hallDepApkVersion))
						{
							showTips("Lua大厅版本必须与Apk版本一致");
							return;
						}
			
						String destApkPath=System.getProperty("user.dir")+"\\apks\\";
						FileUtil.createDir(destApkPath);
						
						int count=0;
						StringBuilder gameZipsSb=new StringBuilder();
						for (int i = 0; i < zipBtnArray.length; i++) 
						{
							if(!zipBtnArray[i].getSelection())
							{
								count++;
								LuaZipBean data=(LuaZipBean) zipBtnArray[i].getData();
								gameZipsSb.append(data.gamePkg+" ");
							}
							if (i==zipBtnArray.length-1)
							{
								appendLogs(String.format("\n选择了%s个游戏\n",zipBtnArray.length-count-1));
							}
						}
						
						String userDir = System.getProperty("user.dir");
						
						StringBuilder cmds=new StringBuilder();
						cmds.append( "cmd /c start " + userDir +"\\build_scqp\\sync_publish_app.bat");
						cmds.append(" ");
						cmds.append(iosBtn.getSelection() ? "ios" :"android");
						cmds.append(" ");
						cmds.append(mConfig.projectList.get(selectedProjectIndex).projectApkDir);
						cmds.append(" ");
						cmds.append(gameZipsSb.toString());

						final String excuteCmd=cmds.toString();
						
						final StringBuilder list = new StringBuilder();
						for(int i=0; i<rightChannels.size(); ++i)
						{
							ChannelBean item2 = mConfig.projectList.get(selectedProjectIndex).channelList.get(rightChannels.get(i));
							String sChannel = String.format("appid=%s;channel_id=%s;channel_key=%s;umeng_appkey=%s;umeng_channel=%s;apkname=%s", 
									mConfig.projectList.get(selectedProjectIndex).projectAppId,
									item2.channelId,
									item2.channelKey,
									(releaseBtn.getSelection()?
												mConfig.projectList.get(selectedProjectIndex).channelList.get(rightChannels.get(i)).umeng_appkey
											:	mConfig.projectList.get(selectedProjectIndex).channelList.get(rightChannels.get(i)).umeng_appkey_test),
								item2.umeng_channel,
							    String.format(item2.channelOutputName,pkgVersionName)
							    );
							
							if(i == rightChannels.size()-1)
							{
								list.append(sChannel);
							}
							else
							{
								list.append(sChannel);
								list.append(",");
							}
						}
							
						final PropertyBean mPropertyBean = new PropertyBean();
						mPropertyBean.setJavaDir(mConfig.jdkDir);
						mPropertyBean.setSdkDir(mConfig.sdkDir);

						mPropertyBean.setProjectDir( mConfig.projectList.get(selectedProjectIndex).projectApkDir);
						mPropertyBean.setProjectName(mConfig.projectList.get(selectedProjectIndex).projectName);
						mPropertyBean.setProjectVersion(pkgVersionName);			
						
						mPropertyBean.setKeyAlias(mConfig.projectList.get(selectedProjectIndex).projectKeyStore.Alias);
						mPropertyBean.setKeyAliasPwd(mConfig.projectList.get(selectedProjectIndex).projectKeyStore.AliasPwd);
						mPropertyBean.setKeyStore(userDir + "\\build_scqp\\boyaa_region_games.keystore");
						mPropertyBean.setKeyStorePwd(mConfig.projectList.get(selectedProjectIndex).projectKeyStore.KeystorePwd);
						
						mPropertyBean.setJarLibsDir("lib");
						mPropertyBean.setChannelList(list.toString());
						mPropertyBean.setApkOutputDir(userDir + "\\apks");
						
						mPropertyBean.setBuildPropertiesPath(userDir + "\\build_scqp\\build.properties");
						mPropertyBean.setBuildXmlPath(userDir + "\\build_scqp\\build.xml");
						
						time_buildAll=time_compile+time_buildPerApk*rightChannels.size();
						time_startBuild=System.currentTimeMillis();
						threadPool.execute(new Runnable() {
							
							@Override
							public void run() {
								isTaskRunning=true;
								
								mIAutoBuild.buildStarted();
								while(isTaskRunning)
								{
									try {
										Thread.sleep(1000);
										mIAutoBuild.buildProgress();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								
								}
								mIAutoBuild.buildFinished();
							}
						});

							new Thread(new Runnable(){

								@Override
								public void run() {
									try {
										
			//							Runtime.getRuntime().exec(cmds);
										
										appendLogs("\r\n--------------A:编译中-----------\r\n");
										
										Process process = Runtime.getRuntime().exec(excuteCmd); 
										String strInfo = ""; 
										BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); 
										while((strInfo = reader.readLine()) != null)
										{ 
											System.out.println(strInfo); 
										}
										appendLogs("\r\n--------------B:编译中-----------\r\n");
										
										appendLogs("\r\n--------------C:Apk打包中-----------\r\n");
									
										AutoBuilder builder = new AutoBuilder(mPropertyBean,mIAutoBuild);
										builder.antBuild();
										
										appendLogs("\r\n--------------D:Apk打包结束----------\r\n");
																		
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								}
								
							}).start();
							
					}
				});
				
				final Button oneKeyMutiLuaZipBtn = new Button(apkGroup, SWT.NONE);
				oneKeyMutiLuaZipBtn.setBounds(10, 150,320, 40);
				oneKeyMutiLuaZipBtn.setText("一键生成内置zip包");
				oneKeyMutiLuaZipBtn.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						
						
						int count=0;
						StringBuilder gameZipsSb=new StringBuilder();
						for (int i = 0; i < zipBtnArray.length; i++) 
						{
							if(!zipBtnArray[i].getSelection())
							{
								count++;
								LuaZipBean data=(LuaZipBean) zipBtnArray[i].getData();
								gameZipsSb.append(data.gamePkg+" ");
							}
							if (i==zipBtnArray.length-1)
							{
								appendLogs(String.format("\n选择了%s个游戏\n",zipBtnArray.length-count-1));
							}
						}
						
						String userDir = System.getProperty("user.dir");
						
						StringBuilder cmds=new StringBuilder();
						cmds.append( "cmd /c start " +userDir +"\\build_scqp\\sync_publish_app.bat");
						cmds.append(" ");
						cmds.append(iosBtn.getSelection() ? "ios" :"android");
						cmds.append(" ");
						cmds.append(mConfig.projectList.get(selectedProjectIndex).projectApkDir);
						cmds.append(" ");
						cmds.append(gameZipsSb.toString());
					
			
						final String excuteCmd=cmds.toString();
							
							new Thread(new Runnable(){

								@Override
								public void run() {
									try {
										
			//							Runtime.getRuntime().exec(cmds);
										
										appendLogs("\r\n--------------A:Zip打包开始-----------\r\n");
										appendLogs("--------------B:执行命令："+excuteCmd+"\r\n");
										
										Process process = Runtime.getRuntime().exec(excuteCmd); 
										String strInfo = ""; 
										BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); 
										while((strInfo = reader.readLine()) != null)
										{ 
											System.out.println(strInfo); 
										}
										appendLogs("\r\n--------------C:Zip打包开始-----------\r\n");
										
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								}
								
							}).start();
							
					}
				});

				

				
			/************************************************************************************************/
			/******************************上传区域uploadGroup***************************************************/
			/************************************************************************************************/
				Group uploadGroup=new Group(apkLuaGroup, SWT.NONE);
				uploadGroup.setText("█ 上传区域");
				uploadGroup.setBounds(920, 420, 350, 230);

				final Button button_All[]=new Button[mConfig.LuaZipList.size()];
				for (int i = 0; i < mConfig.LuaZipList.size(); i++) 
				{
					Button itemBtn = new Button(uploadGroup,SWT.CHECK);
					itemBtn.setBounds(10+(i%4)*80,20+25*(i/4),80,16);
					itemBtn.setText(mConfig.LuaZipList.get(i).gameName);
					itemBtn.setData(mConfig.LuaZipList.get(i));
					button_All[i]=itemBtn;
				}
				
				Button uploadZipBtn = new Button(uploadGroup,SWT.NONE);
				uploadZipBtn.setBounds(10,80,320,35);
				uploadZipBtn.setText("一键上传Zip");
				uploadZipBtn.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(SelectionEvent arg0) {

						super.widgetSelected(arg0);
//						String[] uploadData = new String[2];
						for(int i=0;i<2;i++)
						{
						
							//uploadFile();
//							if(button_All[i].getSelection()&&isExist(ZIP_PATH[i]))
								{
									
									//uploadFile(ZIP_PATH[i]);
//								System.out.println(getJdkDir());
								}
						}
						//System.out.println(getSDK());
								
					}
					
				});
				
				Button uploadApkBtn = new Button(uploadGroup,SWT.NONE);
				uploadApkBtn.setBounds(10,130,320,35);
				uploadApkBtn.setText("一键上传Apk");
				uploadApkBtn.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(SelectionEvent arg0) {

						super.widgetSelected(arg0);
								
					}
					
				});
				
				Button openDevAdminBtn =new Button(uploadGroup,SWT.NONE);
				openDevAdminBtn.setBounds(10,180,100,35);
				openDevAdminBtn.setText("内网后台");
				Button openAdminBtn = new Button(uploadGroup,SWT.NONE);
				openAdminBtn.setBounds(180,180,100,35);
				openAdminBtn.setText("外网后台");
				openDevAdminBtn.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(SelectionEvent arg0) {

						super.widgetSelected(arg0);
				            try {
				            	URI uri = new URI(Constant.URL_ADMIN_DEV);  
								Desktop.getDesktop().browse(uri);
							} catch (IOException | URISyntaxException e) {

								e.printStackTrace();
							}  
					}
					
				});
				
				openAdminBtn.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(SelectionEvent arg0) {

						super.widgetSelected(arg0);
						try {
			            	URI uri = new URI(Constant.URL_ADMIN);  
							Desktop.getDesktop().browse(uri);
						} catch (IOException | URISyntaxException e) {

							e.printStackTrace();
						}  
					}
					
				});
				
				initViewData();
			/***************************************上传区域结束***************************************************/
	}

	private IAutoBuild mIAutoBuild=new IAutoBuild() {
		
		@Override
		public void buildStarted() {
			
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					oneKeyApkBtn.setEnabled(false);
					buildProgressBar.setVisible(true);
					buildProgressLabel.setVisible(true);
				}
			});
			
		}
		
		@Override
		public void printLog(final String message) {
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					logText.append(message);
				}
			});
			
		}
		
		@Override
		public void buildFinished() {
			isTaskRunning=false;
			threadPool.shutdown();
			
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					oneKeyApkBtn.setEnabled(true);
					buildProgressBar.setVisible(false);
					buildProgressLabel.setVisible(false);
					buildProgressLabel.setText("0%");
				}
			});
		}

		@Override
		public void buildProgress() {
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
//					oneKeyApkBtn.setEnabled(false);
//					buildProgressBar.setVisible(true);
					buildProgressLabel.setVisible(true);
					buildProgressLabel.setText(Math.min(99, (int)((System.currentTimeMillis()-time_startBuild)*100/time_buildAll))+"%");
				}
			});
		}


	};
	
	private void initViewData(){

		jdkText.setText(mConfig.jdkDir);
		jdkText.setToolTipText(mConfig.jdkDir);
		adkText.setText(mConfig.sdkDir);
		adkText.setToolTipText(adkText.getText());
		projectPathText.setText(mConfig.projectList.get(selectedProjectIndex).projectApkDir);
		projectPathText.setToolTipText(projectPathText.getText());
		luaPathText.setText(mConfig.projectList.get(selectedProjectIndex).projectLuaDir);
		luaPathText.setToolTipText(luaPathText.getText());

		channelLeftList.removeAll();
		channelRightList.removeAll();
		leftChannels.clear();
		rightChannels.clear();
		java.util.List<ChannelBean> channels = mConfig.projectList.get(selectedProjectIndex).channelList;
		for(int i=0; i<channels.size(); ++i)
		{
			ChannelBean itmChannel = channels.get(i);
			channelLeftList.add(String.format("%s(%s)", itmChannel.channelId, itmChannel.channelName));
			leftChannels.add(i);
		}
		if(channels.size()>0)
		{
			channelLeftList.select(0);
		}
		
		androidBtn.setSelection(true);
		iosBtn.setSelection(false);
		releaseBtn.setSelection(true);
		debugBtn.setSelection(false);

		loadApkVersion();
		loadLuaVersions();
	}
	
	private void loadApkVersion()
	{
		try {
			ManifestParser mfParser = new ManifestParser();
			mfParser.parse(new FileInputStream(mConfig.projectList.get(selectedProjectIndex).projectApkDir+ "\\AndroidManifest.xml"));
			
			pkgName=mfParser.getPackagename();
			pkgVersionName=mfParser.getVersionname();
			pkgVersionCode=mfParser.getVersioncode();
			
			apkPkgNameText.setText(pkgName);
			apkPkgNameText.setToolTipText(pkgName);
			apkVersionNameText.setText(pkgVersionName);
			apkVersionCodeText.setText(pkgVersionCode);
		
			appendLogs("---------------初始化数据APK信息 完成 ------------------------\r\n\r\n");
		}  
		catch (Exception e) {
				appendLogs("---------------初始化数据APK信息 错误 ------------------------\r\n\r\n"+e.toString()+"\r\n");
		}
	}
	
	private void loadLuaVersions(){
		
//		//记载lua游戏中的版本信息
		mLuaInfo = new LuaInfo();
		String errInfo = mLuaInfo.initGameVersion(mConfig.projectList.get(selectedProjectIndex).projectLuaDir,mConfig.LuaZipList);
		if(errInfo.endsWith("success"))
		{
				hallVersionText.setText(mLuaInfo.hallVersion);
				dependApkText.setText(mLuaInfo.hallDepApkVersion);
		}
		else
		{
			appendLogs(errInfo+"\r\n");
		}
		
		//表头
		tcGameName.setText("游戏名称");
		tcGameVersion.setText("版本号");
		tcGameName.setWidth(120);
		tcGameVersion.setWidth(80);
		luaGameInfoTable.setHeaderVisible(true);
		luaGameInfoTable.removeAll();
		
		//表项
		for(int i=0; i<mLuaInfo.gameVersionList.size(); ++i)
		{
			java.util.Map<String, String> map = mLuaInfo.gameVersionList.get(i);
			TableItem item = new TableItem(luaGameInfoTable,SWT.NONE);
			item.setText(new String[]{map.get("gamename"), map.get("gameversion")});
		}
	}
	
	
	private boolean isItemSelected(int selectedIndex){
		for(int i=0; i<rightChannels.size(); ++i)
		{
			if(rightChannels.get(i)==selectedIndex)
			{
				return true;
			}
		}
		return false;
	}
	
	private void addToright(int selectedIndex){
		
		if(leftChannels.size()==0)
		{
			showTips("右移出错！");
			return;
		}
		
		int viewIndex=selectedIndex;
		int dataIndex=leftChannels.get(selectedIndex);
		
		ChannelBean selectedItem = mConfig.projectList.get(selectedProjectIndex).channelList.get(dataIndex);
		if(isItemSelected(dataIndex))
		{
			showTips("已经添加了该项！");
			return;
		}
		else if(selectedItem!=null)
		{
			leftChannels.remove(Integer.valueOf(dataIndex));
			rightChannels.add(Integer.valueOf(dataIndex));
			
			channelRightList.add(String.format("%s(%s)", selectedItem.channelId, selectedItem.channelName));
			channelLeftList.remove(viewIndex);
			printChannels();
		}
		else
		{
			showTips("右移出错，请检查错误先！");
		}
	}
	
	private void addToleft(int selectedIndex){
		
		if(rightChannels.size()==0)
		{
			showTips("左移出错！");
			return;
		}
		
		int viewIndex=selectedIndex;
		int dataIndex=rightChannels.get(selectedIndex);
		
		ChannelBean selectedItem = mConfig.projectList.get(selectedProjectIndex).channelList.get(dataIndex);
		boolean bl = isItemSelected(dataIndex);
		if(!bl)
		{
			showTips("没有该项，请检查错误");
			return;
		}
		else if(selectedItem!=null)
		{
			rightChannels.remove(Integer.valueOf(dataIndex));
			leftChannels.add(Integer.valueOf(dataIndex));
			
			channelRightList.remove(viewIndex);
			channelLeftList.add(String.format("%s(%s)", selectedItem.channelId, selectedItem.channelName));
			printChannels();
		}
		else
		{
			showTips("左移出错，请检查错误先！");
		}
	}
	
	private void printChannels(){
		StringBuilder logs = new StringBuilder();
		logs.append("\n-------------------\r\n");
		for(int i=0; i<rightChannels.size(); ++i)
		{
			logs.append("\t"		+	mConfig.projectList.get(selectedProjectIndex).channelList.get(rightChannels.get(i)).channelName );
			logs.append("\t" 		+ mConfig.projectList.get(selectedProjectIndex).channelList.get(rightChannels.get(i)).channelId );
			logs.append( "\t" 	+ mConfig.projectList.get(selectedProjectIndex).channelList.get(rightChannels.get(i)).umeng_channel );
			logs.append( "\r\n");
		}
		logs.append("-------------------\r\n");
		logs.append("-------------------共选择 ( "+rightChannels.size()+" ) 个渠道\r\n");
//		logText.append(logs);
		logText.setText(logs.toString());
	}
	
	private void appendLogs(final String logs){
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				logText.append(logs);
			}
		});
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		mConfig.readConfig();
	}


	
	private void showTips(String tips){
		MessageBox mb = new MessageBox(ApkLuaTab.this.getShell());
		mb.setMessage(tips);
		mb.setText("提示");
		mb.open();
	}
	
//	/**********************上传文件*****************************************/
//	private void uploadFile(final String filePath)
//	{
//		System.out.print(filePath);
//
//		new Thread(){
//			@Override
//			public void run() {
//
//				super.run();
//				try {
//					 	URL url=new URL(Constant.URL_UPLOAD);
//			            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
//			            connection.setDoInput(true);
//			            connection.setDoOutput(true);
//			            connection.setRequestMethod("POST");
//			            connection.addRequestProperty("FileName", "licheng"); //文件名
//			            //connection.addRequestProperty("","");					//版本号
//			            //connection.addRequestProperty("","");					//渠道号
//			            connection.setRequestProperty("content-type", "text/html");
//			            BufferedOutputStream  out=new BufferedOutputStream(connection.getOutputStream());
//			            
//			            //读取文件上传到服务器
//			            File file=new File(filePath);
//			            FileInputStream fileInputStream=new FileInputStream(file);
//			            byte[]bytes=new byte[1024];
//			            int numReadByte=0;
//			            while((numReadByte=fileInputStream.read(bytes,0,1024))>0)
//			            {
//			                out.write(bytes, 0, numReadByte);
//			            }
//
//			            out.flush();
//			            fileInputStream.close();
//			            //读取URLConnection的响应
//			            DataInputStream in=new DataInputStream(connection.getInputStream());
//			            
//			            appendLogs("--------------上传结束----------------\r\n");
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//			}
//			
//		}.start();
//	}
	
}

package com.open.autopkg.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.open.autopkg.util.FileUtil;

public class MainUI {
	
	private Shell shell;
	private Display display;
	
	public void show()
	{

		try {
			display = Display.getDefault();
		} catch (Error e) {
			String bitVersion=System.getProperty("sun.arch.data.model");
			String userDir = System.getProperty("user.dir");
			String libDir	 =  String.format("\\lib-%s", bitVersion);
			String src=userDir+libDir+"\\swt.jar";
			String dst=userDir+"\\lib\\swt.jar";
			FileUtil.deleteFile(dst);
			FileUtil.copyFile(src, dst);
			
			src=userDir+libDir+"\\luajava-1.1.jar";
			dst=userDir+"\\lib\\luajava-1.1.jar";
			FileUtil.deleteFile(dst);
			FileUtil.copyFile(src, dst);
			
			src=userDir+libDir+"\\luajava-1.1.dll";
			dst=userDir+"\\lib\\luajava-1.1.dll";
			FileUtil.deleteFile(dst);
			FileUtil.copyFile(src, dst);

			display = Display.getDefault();
		}
		
		initView();
		
		shell.open();
		shell.layout();
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent arg0) {
				System.exit(0);
			}

		});
		while (!shell.isDisposed()) 
		{
			if (!display.readAndDispatch()) 
			{
				display.sleep();
			}
		}
		display.dispose();
	}
	

	private void initView() 
	{
		shell = new Shell(display,SWT.CLOSE | SWT.MIN);
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent arg0) 
			{
				
			}
		});
		shell.setSize(1280, 750);
		shell.setText("xBuild");
		Image img = new Image(display, "images\\logo.png");
		shell.setImage(img);
		
		//居中
		{
			int width = shell.getMonitor().getClientArea().width;
			int height = shell.getMonitor().getClientArea().height;
			int x = shell.getSize().x;
			int y = shell.getSize().y;
			if (x > width) {
				shell.getSize().x = width;
			}
			if (y > height) {
				shell.getSize().y = height;
			}
			shell.setLocation((width - x) / 2, (height - y) / 2);
		}
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(0, 0, 1280, 750);
		
		LogoTab mLogoTab=new LogoTab(tabFolder, SWT.NONE);
		TabItem logoTabItem = new TabItem(tabFolder, SWT.NONE);
		logoTabItem.setText("说明                          ");
		logoTabItem.setControl(mLogoTab);
		
		ShrinkTab mTinyPngTab=new ShrinkTab(tabFolder, SWT.NONE);
		TabItem comdTabItem = new TabItem(tabFolder, SWT.NONE);
		comdTabItem.setText("PNG/JPG压缩                           ");
		comdTabItem.setControl(mTinyPngTab);
		
		ApkTab mApkTab=new ApkTab(tabFolder, SWT.NONE);
		TabItem comaTabItem = new TabItem(tabFolder, SWT.NONE);
		comaTabItem.setText("APK差分                           ");
		comaTabItem.setControl(mApkTab);
		
		LuaTab mLuaTab=new LuaTab(tabFolder, SWT.NONE);
		final TabItem combTabItem = new TabItem(tabFolder, SWT.NONE);  
        combTabItem.setText("ZIP差分                           ");  
        combTabItem.setControl(mLuaTab);
        
        ApkLuaTab mApkLuaTab=new ApkLuaTab(tabFolder, SWT.NONE);
		final TabItem comcTabItem = new TabItem(tabFolder, SWT.NONE);  
		comcTabItem.setText("APK/LUA打包                  ");  
		comcTabItem.setControl(mApkLuaTab);
		
		tabFolder.setSelection(4);
	}
}

package com.open.autopkg.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class LogoTab extends Composite {

	public LogoTab(Composite arg0, int arg1) {
		super(arg0, arg1);
		initView();
	}
	
	
	private void initView()
	{
		int w = LogoTab.this.getShell().getSize().x;
		int h = LogoTab.this.getShell().getSize().y;

		Button logoButton = new Button(this, SWT.NONE);  
		logoButton.setBounds((w-256)/2, (h-256)/2-80, 256, 256);  
        Image img = new Image(null, "images\\logo.png");
        logoButton.setImage(img);
        
		Label authorLabel = new Label(this, SWT.NONE);  
        authorLabel.setText("Author:  Long");  
        authorLabel.setFont(new Font(null,"宋体",15,SWT.NORMAL));//设置文字的字体字号
        authorLabel.setBounds((w-256)/2+50, (h-256)/2-100+256+50,256, 50);  
 	
	}
}

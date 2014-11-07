批处理文件按需使用，各文件功能说明如下

	sync_config.bat  			 -- 配置文件，文件路径、7z程序路径等
	
一、清理
	sync_clear.bat   			 -- 清理assets下所有lua相关文件，audio、fonts、images、scripts
	
二、复制
	1、sync_res.bat    		 -- 拷贝resource目录下所有文件到assets目录中
或	
	2、sync_res_audio.bat  -- 单独拷贝resource目录下audio/images/scripts中所有文件到assets目录中
		 sync_res_images.bat    
		 sync_res_scripts.bat
	*每次执行复制前都会检查对应目录是否在assests中存在，删除后拷贝。
	
三、编译lua
	sync_compile.bat 			 --将assets/scripts 中的lua文件编译为字节码，并保留main.lua error.lua等入口原文件。
	
四、一键实现
	sync_publish.bat  		 --依次执行上述一~三文件

五、自动打包apk
	sync_auto_package	 		 --启动软件，自动打包生成apk，部分参数需手动配置

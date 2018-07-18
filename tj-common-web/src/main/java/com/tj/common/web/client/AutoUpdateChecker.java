package com.tj.common.web.client;

import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoUpdateChecker {

	private static Logger logger = LoggerFactory.getLogger(AutoUpdateChecker.class) ;
			
	public static void check(String updateFilePath , String versionFilePath) {
		String currentVersion = CheckerResources.getCurrentVersion(versionFilePath) ;
		String latestVersion = CheckerResources.getLatestVersion() ;
		if(currentVersion != null && !latestVersion.equals(currentVersion)){
			initWindowBeauty();
			UpdateWindow updateWindow = new UpdateWindow(latestVersion,updateFilePath,versionFilePath) ;
			boolean result = updateWindow.doUpdate() ;
			if(result){
				updateWindow.dispose();
			}
		}
	}

	/**
	 * 初始化窗口美化器
	 */
	private static void initWindowBeauty() {
		try {
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
			//标题栏“设置”按钮不显示
			UIManager.put("RootPane.setupButtonVisible",false);
		} catch (Exception e) {
			logger.error("初始化窗口美化器失败", e);
		}
	}

}

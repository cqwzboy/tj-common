package com.tj.common.web.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 更新文件线程
 * 
 * @author silongz
 *
 */
public class UpdateFileExecutor {

	private static Logger logger = LoggerFactory.getLogger(UpdateFileExecutor.class);

	// 显示信息
	private JLabel tipLabel;

	private JProgressBar jpb;

	private String latestVersion;

	public UpdateFileExecutor(JLabel tipLabel, JProgressBar jpb, String latestVersion) {
		this.tipLabel = tipLabel;
		this.jpb = jpb;
		this.latestVersion = latestVersion;
	}

	public boolean excute(String updateFilePath,String versionFilePath) {
		URL url = null;
		InputStream is = null;
		File latestFile = new File(updateFilePath + ".tmp");
		String latestFileUrl = CheckerResources.getLatestFileUrl();
		HttpURLConnection httpUrl = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		try {
			if (!latestFile.exists()) {
				latestFile.createNewFile();
			}
			url = new URL(latestFileUrl);
			httpUrl = (HttpURLConnection) url.openConnection();
			httpUrl.connect();
			int totalSize = httpUrl.getContentLength();
			byte[] buffer = new byte[1024 * 1024];
			int size = 0;
			is = httpUrl.getInputStream();
			bis = new BufferedInputStream(is);
			fos = new FileOutputStream(latestFile);
			String loadingTxtPrefix = "正在从网络上下载新的更新文件(" + (totalSize / 1000000) + "M)";
			tipLabel.setText(loadingTxtPrefix);
			try {
				int readedSize = 0;
				while ((size = bis.read(buffer)) != -1) {
					readedSize += size;
					fos.write(buffer, 0, size);
					fos.flush();
					int percent = readedSize * 100 / totalSize;
					jpb.setValue(percent);
				}
			} catch (Exception e) {
				tipLabel.setText("更新文件异常\n");
				logger.error("更新文件时，读取文件内容异常", e);
				return false;
			}
			tipLabel.setText("文件下载完成");

			// 本地需要被更新的文件
			File currentFile = new File(updateFilePath);
			if (currentFile.exists()) {
				currentFile.delete();
			}
			replaceCurrentFile(currentFile, latestFile);
			updateCurrentVersionFile(versionFilePath);

		} catch (Exception e) {
			tipLabel.setText("更新文件异常\n");
			logger.error("更新文件异常", e);
			return false;
		} finally {
			try {
				fos.close();
				bis.close();
				is.close();
				httpUrl.disconnect();
			} catch (IOException e) {
				tipLabel.setText("更新文件异常\n");
				logger.error("更新文件完成后，关闭文件流异常", e);
				return false;
			}
		}
		latestFile.delete() ;
		return true;
	}

	/**
	 * 替换掉当前版本的文件
	 * 
	 * @param currentFile
	 * @param latestFile
	 * @throws Exception
	 */
	private void replaceCurrentFile(File currentFile, File latestFile) throws Exception {

		if (currentFile.exists()) {
			currentFile.delete();
		}
		FileInputStream in = new FileInputStream(latestFile);
		FileOutputStream out = new FileOutputStream(currentFile);

		byte[] buffer = new byte[1024 * 1024];
		int size;
		while ((size = in.read(buffer)) != -1) {
			out.write(buffer, 0, size);
			out.flush();
		}
		if (out != null) {
			out.close();
		}
		if (in != null) {
			in.close();
		}
	}

	/**
	 * 更新客户端当前版本号
	 */
	private void updateCurrentVersionFile(String versionFilePath) {
		FileOutputStream fos = null;
		try {
			File file = new File(versionFilePath);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(latestVersion.getBytes());
		} catch (IOException e) {
			logger.error("更新版本号异常", e);
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				logger.error("更新版本号完成后，关闭文件流异常", e);
			}
		}
	}
}

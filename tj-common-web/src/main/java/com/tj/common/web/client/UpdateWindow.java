package com.tj.common.web.client;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class UpdateWindow extends JFrame {
	
	private String latestVersion ;
	
	private String updateFilePath ;
	
	private String versionFilePath ;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2465119523542831181L;
	
	public UpdateWindow(String latestVersion,String updateFilePath , String versionFilePath) {
		this.latestVersion = latestVersion ;
		this.updateFilePath = updateFilePath ;
		this.versionFilePath = versionFilePath ;
		setAttb();
	}

	public boolean doUpdate(){
		JLabel title = new JLabel("更新文件");
		JPanel panel2 = new JPanel();
		panel2.add(title);
		panel2.setBounds(10, 10, 210, 20);
		
		JPanel panel = new JPanel();

		panel.setBounds(10,50,210,20);
		this.add(panel);
		this.add(panel2);
		
		JProgressBar jpb = new JProgressBar();
		jpb.setMaximum(100);// 设置最大值
		jpb.setMinimum(0);// 设置最小值
		jpb.setValue(0);// 设置初始值
		jpb.setStringPainted(true);// 设置进度条上是否显示进度具体进度如50%
		jpb.setPreferredSize(new Dimension(200, 15));
		panel.add(jpb);
		return new UpdateFileExecutor(title,jpb,latestVersion).excute(updateFilePath,versionFilePath); 
	}
	
	private void setAttb() {
		// 窗体设置
		this.setTitle(CheckerResources.getAppName() + " - 自动更新");
		this.setSize(250, 150);
		this.setLayout(null);
		this.setResizable(false);
		this.setVisible(true);
		this.setIconImage(new ImageIcon(this.getClass().getResource("/static/image/logo.png")).getImage());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 窗体居中
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}
}

package com.tj.common.zookeeper.base;

import org.apache.curator.framework.recipes.cache.ChildData;

public interface ZKListenerCallBack {
	/**
	 * 需要监听的路径
	 * @author luhl
	 * 2017年2月8日
	 * @return
	 */
	public String getListenPath();
	public void addNode(ChildData data);
	public void updateNode(ChildData data);
	public void removeNode(ChildData data);
}

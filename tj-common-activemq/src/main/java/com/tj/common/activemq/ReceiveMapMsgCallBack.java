package com.tj.common.activemq;

import java.util.Map;

public interface ReceiveMapMsgCallBack {

	void call(Map<String,Object> mapMsg) ;
}

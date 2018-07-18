package com.tj.common.kafka.consumer;

public interface KafkaReceiveMsgCallBack {

	void call(String key , String msg) ;
}

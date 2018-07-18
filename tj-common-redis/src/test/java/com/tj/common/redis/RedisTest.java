package com.tj.common.redis;

import java.util.Map;

public class RedisTest {

	public static void main(String[] args) {

		RedisClient client = RedisClientFactory.getClient(RedisClientType.SIMPLE) ;
//		client.setStr("key1", "1") ;
//		client.setStr("key2", "2") ;
//		client.setStr("key3", "3") ;
//		client.setStr("key4", "4") ;
//		client.hsetStr("key_f", "f1","kf1") ;
//		client.hsetStr("key_f", "f2","kf2") ;
//		
////		client = RedisClientFactory.getClient(RedisClientType.SENTINEL) ;
////		client.setStr("test_common_sentinel", "test_common_sentinel") ;
//		
//		List<BatchInfo> list = new ArrayList<BatchInfo>() ;
//		BatchInfo b1 = new BatchInfo() ;
//		b1.setKey("key5");
//		b1.setValue("5");
//		b1.setBatchCommandType(BatchCommandType.SET_STR);
//		
//		BatchInfo b2 = new BatchInfo() ;
//		b2.setKey("key1");
//		b2.setBatchCommandType(BatchCommandType.DEL);
//		
//		BatchInfo b3 = new BatchInfo() ;
//		b3.setKey("key6");
//		b3.setField("field1");
//		b3.setValue("6");
//		b3.setBatchCommandType(BatchCommandType.HSET_STR);
//		
//		BatchInfo b4 = new BatchInfo() ;
//		b4.setKey("key7");
//		b4.setField("field1");
//		b4.setValue("6");
//		b4.setBatchCommandType(BatchCommandType.HSET);
//		
//		BatchInfo b5 = new BatchInfo() ;
//		b5.setKey("key8");
//		b5.setValue("6");
//		b5.setBatchCommandType(BatchCommandType.SET);
//		
//		BatchInfo b6 = new BatchInfo() ;
//		b6.setKey("key_f");
//		b6.setField("f1");
//		b6.setBatchCommandType(BatchCommandType.HDEL);
//		
//		list.add(b1) ;
//		list.add(b2) ;
//		list.add(b3) ;
//		list.add(b4) ;
//		list.add(b5) ;
//		list.add(b6) ;
//		
//		client.batchWithTransaction(list);
//		client.hset("key1", "field1曾司龙乱码测试……&……&&%%6rtft45778798", "value1曾司龙乱码测试……&……&&%%6rtft4577") ;
//		System.out.println(client.hget("key1", "field1曾司龙乱码测试……&……&&%%6rtft45778798"));
//		client.hdel("key1", "field1曾司龙乱码测试……&……&&%%6rtft45778798") ;
//		System.out.println(client.hexists("key1", "field1曾司龙乱码测试……&……&&%%6rtft45778798"));
//		Map<String,String> map = client.hgetStrAll("tj-ai-py-info") ;
//		System.out.println(map == null ? 0:map.size());

//		BatchInfo b = new BatchInfo() ;
//		b.setKey("key_f");
//		b.setField("f1");
//		client.setJson("json_test",b) ;
//		BatchInfo bb = client.getJson("json_test",BatchInfo.class) ;
//		System.out.print(bb.getKey());
//		System.out.print(bb.getField());
		BatchInfo b = new BatchInfo() ;
		b.setKey("key_f");
		b.setField("f1");
		client.hsetJson("json_test_key","json_field",b) ;
		BatchInfo bb = client.hgetJson("json_test","json_field",BatchInfo.class) ;
		System.out.print(bb.getKey());
		System.out.print(bb.getField());
	}

}

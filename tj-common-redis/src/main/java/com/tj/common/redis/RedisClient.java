package com.tj.common.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Tuple;

public interface RedisClient {

	String set(String key, Object value);

	Object get(String key);

	long expire(String key, int seconds);

	long expireAt(String key, long unixTimestamp);

	String setStr(String key, String value);

	String getStr(String key);

	Long hset(String key, String field, Object value);

	Long hsetStr(String key, String field, String value);

	Object hget(String key, String field);
	
	Long hlen(String key);

	String hgetStr(String key, String field);

	Long del(String key);

	Long hdel(String key, String field);

	Boolean exists(String key);

	Boolean hexists(String key, String field);
	
	/**
	 * 获取hash结构的所有field和Object值
	 * @param key
	 * @return
	 */
	Map<String,Object> hgetAll(String key);
	
	/**
	 * 获取hash结构的所有field和String类型值
	 * @param key
	 * @return
	 */
	Map<String,String> hgetStrAll(String key);
	
	/**
	 * 原子+1
	 * @param key
	 * @return
	 */
	long incr(String key);
	/**
	 * 原子-1
	 * @param key
	 * @return
	 */
	long decr(String key);

    Long zadd(String key, double score, Object member);

    Long zaddStr(String key, double score, String member);
	/**
	 * 返回指定下标区间带有分值的zset集合
	 * @param key 对应zset的键
	 * @param start 下标开始值（闭区间）
	 * @param end 下标结束值（闭区间，负值表示倒数，-1表示倒数第一个元素）
	 * @return 返回符合条件的对象的有序集合（{@link java.util.LinkedHashSet<Tuple>}），
	 * 			{@link Tuple#getScore()}可获得对应元素的score值，
	 * 			{@link Tuple#getElement()}可获得对应字符串元素，
	 * 			{@link Tuple#getBinaryElement()}可获得对应对象的序列化字节流。
	 */
    Set<Tuple> zrangeWithScores(String key, long start, long end);

    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列。
     * 具有相同 score 值的成员按字典序来排列(该属性是有序集提供的，不需要额外的计算)。
     *
     * @param key 对应zset的键
     * @param min score的最小值（闭区间）
     * @param max score的最大值（闭区间，-1表示无上限）
     * @return
     */
    Set<Tuple> zrangeByScoreWithScores(String key, double min, double max);

    /**
     * 返回有序集合，成员按分数值递减(从大到小)排序。
     * @author limin
     * @param key 对应zset的键
     * @param start 下标开始值（闭区间）
     * @param end 下标结束值（闭区间，负值表示倒数，-1表示倒数第一个元素）
     * @return
     * 2017年11月22日
     */
    Set<Tuple> zrevrangeWithScores(String key, long start, long end);
	/**
	 * 返回指定分值区间的zset集合
	 * @param key 对应zset的键
	 * @param min score的最小值（闭区间）
	 * @param max score的最大值（闭区间，-1表示无上限）
	 * @return 返回符合条件的对象的有序集合
	 */
    Set<Object> zrangeByScore(String key, double min, double max);
    /**
	 * 返回指定分值区间的zset集合
	 * @param key 对应zset的键
	 * @param min score的最小值（闭区间）
	 * @param max score的最大值（闭区间，-1表示无上限）
	 * @return 返回符合条件的字符串的有序集合
	 */
    Set<String> zrangeByscoreStrings(String key, double min, double max);
    /**
	 * 删除指定zset中的字符串元素member
	 * @param key 对应zset的键
	 * @param members 要删除的字符串元素（字符串元素通过 {@link String#equals(Object)} 方法判断唯一性）
	 * @return 删除的元素数量
	 */
    Long zrem(String key, String... members);
    /**
	 * 删除指定分值之间的所有元素
	 * @param key 对应zset的键
	 * @param min score开始值（闭区间）
	 * @param max score结束值（闭区间）
	 * @return 删除的元素数量
	 */
    Long zremrangeByScore(String key, double min, double max);
    /**
	 * 返回指定zset中对应字符串元素的score值
	 * @param key 对应zset的键
	 * @param member 对应的字符串元素
	 * @return 指定zset中对应字符串元素的score值，若不存在则返回null
	 */
    Double zscore(String key, String member);

	/**
	 * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
	 * @param key 对应set的键
	 * @param members 要加入的字符串数组
	 * @return 被添加到集合中的新元素的数量，不包括被忽略的元素。
	 */
	Long saddStr(String key, String... members);

    /**
     * 移除并返回集合中的一个随机元素。
     * 如果只想获取一个随机元素，但不想该元素从集合中被移除的话，可以使用 SRANDMEMBER 命令。
     *
     * @param key
     * @return
     */
    String spopStr(String key);

    /**
	 * 判断 member 元素是否集合 key 的成员。
	 * @param key
	 * @param member
	 * @return 如果 member 元素是集合的成员，返回 1 。
	 * 			如果 member 元素不是集合的成员，或 key 不存在，返回 0 。
	 */
	Boolean sismemberStr(String key, String member);

	/**
	 * 返回集合 key 中的所有成员。
	 * 不存在的 key 被视为空集合。
	 * @param key
	 * @return 集合中的所有成员。
	 */
	Set<String> smembersStr(String key);

    /**
     * 返回集合 key 的基数(集合中元素的数量)。
     *
     * @param key
     * @return
     */
    Long scard(String key);

    /**
	 * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
	 * 当 key 不是集合类型，返回一个错误。
	 * @param key
	 * @param members
	 * @return 被成功移除的元素的数量，不包括被忽略的元素。
	 */
	Long sremStr(String key, String... members);

	/**
	 * 原子增加value值
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	long hIncr(String key , String field , long value) ;
	
	/**
	 * 模糊匹配key
	 * @param pattern(参数以*拼接时模糊查询。例如*wink，wink*，wi*k)
	 * @return	 返回所有匹配的key值
	 */
	Set<String> keys(String pattern);
	
	/**
	 * 获取key的过期时间
	 * @param key
	 * @return
	 */
	Long ttl(String key);
	
	/**
	 * 原子+1
	 * @param key
	 * @return
	 */
	long incr(String key, long value);
	
	/**
	 * 移除并返回列表 key 的头元素
	 * @param key
	 * @return
	 */
	String lpop(String key); 
	
	/**
	 * 将一个或多个值 value 插入到列表 key 的表头(对列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a )
	 * @param key
	 * @param values
	 * @return
	 */
	Long lpush(String key, String... values);
	
	/**
	 * 将一个或多个值 values 插入到列表 key 的表尾(比如对一个空列表 mylist 执行 RPUSH mylist a b c ，得出的结果列表为 a b c)。
	 * @param key
	 * @param values
	 * @return
	 */
	Long rpush(String key, String... values);
	
	/**
	 * 移除并返回列表 key 的尾元素。
	 * @param key
	 * @return
	 */
	String rpop(String key);
	
	/**
	 * 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。  
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	List<String> lrange(String key, long start, long end) ;

    /**
     * 保留列表 start 到 end 闭区间内的所有内容,删除剩下的内容
     * 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     *
     * @param key
     * @param start
     * @param end
     * @return 成功则返回true,失败则返回false(错误信息没有被透传)
     */
    boolean ltrim(String key, long start, long end);

    /**
     * 根据参数 count 的值，移除列表中与参数 value 相等的元素。
     * count 的值可以是以下几种：
     * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
     * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
     * count = 0 : 移除表中所有与 value 相等的值。
     *
     * @param key
     * @param count
     * @param value
     * @return
     */
    Long lrem(String key, long count, String value);

    /**
     * 返回列表 key 的长度。
     * 如果 key 不存在，则 key 被解释为一个空列表，返回 0 。
     * 如果 key 不是列表类型，返回一个错误。
     *
     * @param key
     * @return
     */
    Long llen(String key);

	/**
	 * 以json序列化方式存放对象
	 * @param key
	 * @param value
	 * @return
	 */
	<T> String setJson(String key, T value);

	/**
	 * 获取以json格式缓存的对象
	 * @param key
	 * @param cls
	 * @param <T>
	 * @return
	 */
	<T> T getJson(String key,Class<T> cls) ;

	/**
	 * hash结构，以json序列化方式存放对象
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	<T> Long hsetJson(String key, String field, T value);

	/**
	 * hash结构，获取以json格式缓存的对象
	 * @param key
	 * @param field
	 * @param cls
	 * @param <T>
	 * @return
	 */
	<T> T hgetJson(String key,String field, Class<T> cls) ;

	/**
	 * 获取hash结构指定key的所有fields
	 * @param key
	 * @return
	 */
	Set<String> hKeys(String key) ;


	//=================以下方法有事务控制===========================

	/**
	 * redis指定命令的事务
	 * @param batchs
	 */
	void batchWithTransaction(List<BatchInfo> batchs);

	/**
	 * redis不指定命令的事务，调用方自己实现回调方法，决定在同一个Transaction下使用哪些命令集
	 * @param transactionCallBack
	 */
	void batchWithTransaction(TransactionCallBack transactionCallBack) ;
}
package com.tj.common.redis.sentinel;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.tj.common.redis.*;
import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

import com.tj.common.lang.SerializeUtils;
import com.tj.common.redis.BatchInfo.BatchCommandType;

/**
 * Redis Sentinel客户端
 * 
 * @author silongz
 *
 */
public class RedisSentinelClient implements RedisClient {

	@Override
	public String set(final String key, final Object value) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public String call(Jedis jedis) {
				return jedis.set(getBytes(key), SerializeUtils.serialize(value));
			}
		});
	}

	@Override
	public Object get(final String key) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Object call(Jedis jedis) {
				byte[] result = jedis.get(getBytes(key));
				return SerializeUtils.unserialize(result);
			}
		});
	}

	@Override
	public long expire(final String key, final int seconds) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Long call(Jedis jedis) {
				return jedis.expire(key, seconds);
			}
		});
	}

	@Override
	public long expireAt(final String key, final long unixTimestamp) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Long call(Jedis jedis) {
				return jedis.expireAt(key, unixTimestamp);
			}
		});
	}

	@Override
	public String setStr(final String key, final String value) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public String call(Jedis jedis) {
				return jedis.set(key, value);
			}
		});
	}

	@Override
	public String getStr(final String key) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public String call(Jedis jedis) {
				return jedis.get(key);
			}
		});
	}

	@Override
	public Long hset(final String key, final String field, final Object value) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Long call(Jedis jedis) {
				return jedis.hset(getBytes(key), getBytes(field),
						SerializeUtils.serialize(value));
			}
		});
	}

	@Override
	public Long hsetStr(final String key, final String field,
			final String value) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Long call(Jedis jedis) {
				return jedis.hset(key, field, value);
			}
		});
	}

	@Override
	public Object hget(final String key, final String field) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Object call(Jedis jedis) {
				byte[] result = jedis.hget(getBytes(key), getBytes(field));
				return SerializeUtils.unserialize(result);
			}
		});
	}

	@Override
	public String hgetStr(final String key, final String field) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public String call(Jedis jedis) {
				return jedis.hget(key, field);
			}
		});

	}

	@Override
	public Long del(final String key) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Long call(Jedis jedis) {
				return jedis.del(key);
			}
		});
	}

	@Override
	public Long hdel(final String key, final String field) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Long call(Jedis jedis) {
				return jedis.hdel(key, field);
			}
		});
	}

	@Override
	public Boolean exists(final String key) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Boolean call(Jedis jedis) {
				return jedis.exists(key);
			}
		});
	}

	@Override
	public Boolean hexists(final String key, final String field) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Boolean call(Jedis jedis) {
				return jedis.hexists(key, field);
			}
		});
	}

	@Override
	public long incr(final String key) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Long call(Jedis jedis) {
				return jedis.incr(key);
			}
		});
	}

	@Override
	public long decr(final String key) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Long call(Jedis jedis) {
				return jedis.decr(key);
			}
		});
	}

	@Override
	public void batchWithTransaction(final List<BatchInfo> batchs) {
		if(batchs == null || batchs.size() <= 1){
			throw new IllegalArgumentException("两个以下的redis操作不需要使用事务控制");
		}
		RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public String call(Jedis jedis) {
				return doBatch(batchs, jedis);
			}

			/**
			 * 执行批量操作
			 * @param batchs
			 * @param jedis
			 * @return
			 */
			private String doBatch(final List<BatchInfo> batchs, Jedis jedis) {
				Transaction transaction = null ; 
				try {
					transaction = jedis.multi();  
					for (BatchInfo each : batchs) {
						doEachCommand(transaction, each);
					}
					transaction.exec();
				} finally {
					if(transaction != null){
						try {
							transaction.close();
						} catch (IOException e) {
							throw new RuntimeException("redis事务关闭异常",e);
						}
					}
				}
				return null ;
			}

			/**
			 * 执行每一个操作
			 * @param transaction
			 * @param each
			 */
			private void doEachCommand(Transaction transaction, BatchInfo each) {
				if(StringUtils.isBlank(each.getKey())){
					throw new IllegalArgumentException("redis key不能为空");
				}
				BatchCommandType batchCommandType = each.getBatchCommandType() ;
				if(batchCommandType == null){
					throw new IllegalArgumentException("redis 事务操作时，操作命令不能为空");
				}
				switch (batchCommandType) {
				case SET:
					transaction.set(each.getKey().getBytes(), SerializeUtils.serialize(each.getValue())) ;
					break;
				case SET_STR:
					if(each.getValue() instanceof String){
						transaction.set(each.getKey(), (String)each.getValue()) ;
					}else{
						throw new IllegalArgumentException("redis 事务操作时，传入了SET_STR命令，但是对应的值不是String类型");
					}
					break;
				case HSET:
					if(StringUtils.isBlank(each.getField())){
						throw new IllegalArgumentException("redis 事务操作时，传入了HSET命令，但是field为空");
					}
					transaction.hset(each.getKey().getBytes(), each.getField().getBytes() ,SerializeUtils.serialize(each.getValue())) ;
					break;
				case HSET_STR:
					if(StringUtils.isBlank(each.getField())){
						throw new IllegalArgumentException("redis 事务操作时，传入了HSET_STR命令，但是field为空");
					}
					if(each.getValue() instanceof String){
						transaction.hset(each.getKey(),  each.getField(), (String)each.getValue()) ;
					}else{
						throw new IllegalArgumentException("redis 事务操作时，传入了HSET_STR命令，但是对应的值不是String类型");
					}
					break;
				case DEL :
					transaction.del(each.getKey()) ;
					break;
				case HDEL :
					if(StringUtils.isBlank(each.getField())){
						throw new IllegalArgumentException("redis 事务操作时，传入了HDEL命令，但是field为空");
					}
					transaction.hdel(each.getKey(),each.getField()) ;
					break;
				case INCRBY :
					transaction.incrBy(each.getKey(), (Long)each.getValue()) ;
					break;
				case HINCRBY :
					transaction.hincrBy(each.getKey(),each.getField(),(Long)each.getValue()) ;
					break;	
				default:
					throw new IllegalArgumentException("redis 事务操作时，操作命令不合法");
				}
			}
		});
	}

	@Override
	public void batchWithTransaction(final TransactionCallBack transactionCallBack) {
		RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public String call(Jedis jedis) {
				Transaction transaction = null ;
				try {
					transaction = jedis.multi();
					transactionCallBack.doTransaction(transaction);
					transaction.exec();
				} finally {
					if(transaction != null){
						try {
							transaction.close();
						} catch (IOException e) {
							throw new RuntimeException("redis事务关闭异常",e);
						}
					}
				}
				return null ;
			}
		});
	}

	@Override
	public Map<String, Object> hgetAll(final String key) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Object call(Jedis jedis) {
				Map<byte[], byte[]> map = jedis.hgetAll(getBytes(key));
				Map<String,Object> resultMap = new HashMap<String,Object>() ;
				for (Entry<byte[], byte[]> entry : map.entrySet()) {
					String newKey = new String(entry.getKey()) ;
					Object newValue = SerializeUtils.unserialize(entry.getValue()) ;
					resultMap.put(newKey, newValue) ;
				}
				return resultMap ;
			}
		});
	}

	@Override
	public Map<String, String> hgetStrAll(final String key) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Object call(Jedis jedis) {
				return jedis.hgetAll(key);
			}
		});
	}

	@Override
	public long hIncr(final String key, final String field, final long value) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		if(StringUtils.isBlank(field)){
			throw new IllegalArgumentException("redis field不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Object call(Jedis jedis) {
				return jedis.hincrBy(getBytes(key), getBytes(field), value) ;
			}
		});
	}	

	@Override
	public Long hlen(final String key) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Object call(Jedis jedis) {
				Long result = jedis.hlen(getBytes(key));
				return result;
			}
		});
	}	
	
	@Override
    public Long zadd(final String key, final double score, final Object member) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
        return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Object call(Jedis jedis) {
                return jedis.zadd(getBytes(key), score, SerializeUtils.serialize(member));
            }
        });//, 0L, "存储Object到zset");
    }

    @Override
		public Long zaddStr(final String key, final double score, final String menber) {
		    return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
	            @SuppressWarnings("unchecked")
				@Override
	            public Long call(Jedis jedis) {
	                return jedis.zadd(key, score, menber);
	            }
	        });//, 0L, "存储String到zset");
	    }
	 @Override
	public Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
		if (StringUtils.isBlank(key)) {
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Object call(Jedis jedis) {
				Set<Tuple> result = jedis.zrangeWithScores(key, start, end);
				if (result == null) {
					return null;
				}
				return result;
			}
		});
	}

    @Override
    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("redis key不能为空");
        }
        return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
            @SuppressWarnings("unchecked")
            @Override
            public Object call(Jedis jedis) {
                Set<Tuple> result = jedis.zrangeByScoreWithScores(key, min, max);
                if (result == null) {
                    return null;
                }
                return result;
            }
        });
    }

	 @Override
	 public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long end) {
			if (StringUtils.isBlank(key)) {
				throw new IllegalArgumentException("redis key不能为空");
			}
			return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
				@SuppressWarnings("unchecked")
				@Override
				public Object call(Jedis jedis) {
					Set<Tuple> result = jedis.zrevrangeWithScores(key, start, end);
					if (result == null) {
						return null;
					}
					return result;
				}
			});
		}
	    @Override
	    public Set<Object> zrangeByScore(final String key, final double min, final double max) {
	    	if (StringUtils.isBlank(key)) {
				throw new IllegalArgumentException("redis key不能为空");
			}
			return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
				@SuppressWarnings("unchecked")
				@Override
				public Object call(Jedis jedis) {
					Set<byte[]> bytesResult = jedis.zrangeByScore(SerializeUtils.serialize(key), min, max);
					Set<Object> resultSet = new LinkedHashSet<>();
					for (byte[] bytes : bytesResult) {
	                    resultSet.add(SerializeUtils.unserialize(bytes));
	                }
	                return resultSet;
				}
			});
	    	//"从zset中查询Object集合");
	    }

	    @Override
	    public Set<String> zrangeByscoreStrings(final String key, final double min, final double max) {
	    	if (StringUtils.isBlank(key)) {
				throw new IllegalArgumentException("redis key不能为空");
			}
			return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
				@SuppressWarnings("unchecked")
				@Override
				public Object call(Jedis jedis) {
					return jedis.zrangeByScore(key, min, max);
				}
			});// "从zset中查询String集合");
	    }

	    @Override
	    public Long zrem(final String key, final String... members) {
	    	if (StringUtils.isBlank(key)) {
				throw new IllegalArgumentException("redis key不能为空");
			}
	    	return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
				@SuppressWarnings("unchecked")
				@Override
				public Object call(Jedis jedis) {
					return jedis.zrem(key, members);
				}
	    	});//"从zset中删除指定元素");
	    }

	    @Override
	    public Long zremrangeByScore(final String key, final double min, final double max) {
	    	if (StringUtils.isBlank(key)) {
				throw new IllegalArgumentException("redis key不能为空");
			}
	    	return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
	        	@SuppressWarnings("unchecked")
				@Override
				public Object call(Jedis jedis) {
					return jedis.zremrangeByScore(key, min, max);
				}
	        });//"根据score从zset中删除元素");
	    }

	    @Override
	    public Double zscore(final String key, final String member) {
	    	if (StringUtils.isBlank(key)) {
				throw new IllegalArgumentException("redis key不能为空");
			}
	    	return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
	        	@SuppressWarnings("unchecked")
				@Override
				public Object call(Jedis jedis) {
					return jedis.zscore(key, member);
				}
	        });//"从zset中获取元素的score");
	    }

	@Override
	public Long saddStr(final String key, final String... members) {
		if (StringUtils.isBlank(key)) {
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Long call(Jedis jedis) {
				return jedis.sadd(key, members);
			}
		});
	}

    @Override
    public String spopStr(final String key) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("redis key不能为空");
        }
        return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
            @SuppressWarnings("unchecked")
            @Override
            public String call(Jedis jedis) {
                return jedis.spop(key);
            }
        });
    }

    @Override
	public Boolean sismemberStr(final String key, final String member) {
		if (StringUtils.isBlank(key)) {
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Boolean call(Jedis jedis) {
				return jedis.sismember(key, member);
			}
		});
	}

	@Override
	public Set<String> smembersStr(final String key) {
		if (StringUtils.isBlank(key)) {
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Set<String> call(Jedis jedis) {
				return jedis.smembers(key);
			}
		});
	}

    @Override
    public Long scard(final String key) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("redis key不能为空");
        }
        return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
            @SuppressWarnings("unchecked")
            @Override
            public Long call(Jedis jedis) {
                return jedis.scard(key);
            }
        });
    }

    @Override
	public Long sremStr(final String key, final String... members) {
		if (StringUtils.isBlank(key)) {
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Long call(Jedis jedis) {
				return jedis.srem(key, members);
			}
		});
	}
	
	/**
	 * 获取key的字节数组
	 * @param s
	 * @return
	 */
	private byte [] getBytes(final String s) {
		byte [] bytes = null ;
		String charSet = RedisConfig.getCharset() ;
		if(StringUtils.isNotBlank(charSet)){
			try {
				bytes = s.getBytes(charSet) ;
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException("获取redis key 字节时异常",e);
			}
		}else{
			bytes = s.getBytes() ;
		}
		return bytes ;
	}

	@Override
	public Set<String> keys(final String pattern) {
		if (StringUtils.isBlank(pattern)) {
			throw new IllegalArgumentException("redis pattern不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Set<String> call(Jedis jedis) {
				return jedis.keys(pattern);
			}
		});
	}

	@Override
	public Long ttl(final String key) {
		if (StringUtils.isBlank(key)) {
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Long call(Jedis jedis) {
				return jedis.ttl(key);
			}
		});
	}

	@Override
	public long incr(final String key, final long value) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Long call(Jedis jedis) {
				return jedis.incrBy(key,value);
			}
		});
	}

	@Override
	public String lpop(final String key) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public String call(Jedis jedis) {
				return jedis.lpop(key) ;
			}
		});
	}

	@Override
	public Long lpush(final String key, final String... values) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Long call(Jedis jedis) {
				return jedis.lpush(key, values) ;
			}
		});
	}

	@Override
	public Long rpush(final String key, final String... values) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Long call(Jedis jedis) {
				return jedis.rpush(key, values) ;
			}
		});
	}

	@Override
	public String rpop(final String key) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public String call(Jedis jedis) {
				return jedis.rpop(key);
			}
		});
	}
	
	@Override
	public List<String> lrange(final String key, final long start, final long end) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public List<String> call(Jedis jedis) {
				return jedis.lrange(key, start, end) ;
			}
		});
	}

    @Override
    public boolean ltrim(final String key, final long start, final long end) {
        if(StringUtils.isBlank(key)){
            throw new IllegalArgumentException("redis key不能为空");
        }
        return "ok".equalsIgnoreCase(RedisTemplate.<String>excuteBySentinel(new TJKRedisCallBack() {
            @SuppressWarnings("unchecked")
            @Override
            public String call(Jedis jedis) {
                return jedis.ltrim(key, start, end);
            }
        }));
    }

    @Override
    public Long lrem(final String key, final long count, final String value) {
        if(StringUtils.isBlank(key)){
            throw new IllegalArgumentException("redis key不能为空");
        }
        return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
            @SuppressWarnings("unchecked")
            @Override
            public Long call(Jedis jedis) {
                return jedis.lrem(key, count, value);
            }
        });
    }

    @Override
    public Long llen(final String key) {
        if(StringUtils.isBlank(key)){
            throw new IllegalArgumentException("redis key不能为空");
        }
        return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
            @SuppressWarnings("unchecked")
            @Override
            public Long call(Jedis jedis) {
                return jedis.llen(key);
            }
        });
    }

	@Override
	public <T> String setJson(final String key, final T value) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public String call(Jedis jedis) {
				return jedis.set(key, JSON.toJSONString(value));
			}
		});
	}

	@Override
	public <T> T getJson(final String key,final Class<T> cls) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Object call(Jedis jedis) {
				String json = jedis.get(key);
				if (json == null) return null;
				return JSON.parseObject(json,cls) ;
			}
		});
	}

	@Override
	public <T> Long hsetJson(final String key, final String field, final T value) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Long call(Jedis jedis) {
				return jedis.hset(key, field,JSON.toJSONString(value));
			}
		});
	}

	@Override
	public <T> T hgetJson(final String key, final String field, final Class<T> cls) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Object call(Jedis jedis) {
				String json = jedis.hget(key, field);
				if (json == null) {
					return null;
				}
				return JSON.parseObject(json,cls);
			}
		});
	}

	@Override
	public Set<String> hKeys(final String key) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("redis key不能为空");
		}
		return RedisTemplate.excuteBySentinel(new TJKRedisCallBack() {
			@SuppressWarnings("unchecked")
			@Override
			public Object call(Jedis jedis) {
				return jedis.hkeys(key) ;
			}
		});
	}
}

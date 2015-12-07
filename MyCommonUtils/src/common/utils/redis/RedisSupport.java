package common.utils.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Tuple;

/**
 * redis的支持类,使用jedis类库来操作redis
 * 
 * @ClassName: RedisSupport
 * @author tank
 * @date 2014-10-15 下午3:07:23
 */
public class RedisSupport {

	private static RedisSupport context;
	private static Logger logger = LoggerFactory.getLogger(RedisSupport.class);

	private static redis.clients.util.Pool<Jedis> pool;
	// 加载配置信息
	static {

		Properties properties = new Properties();
		InputStream in = RedisSupport.class.getResourceAsStream("/redis.properties");

		try {
			properties.load(in);
			logger.info("{}", properties.getProperty("maxTotal"));
			Integer timeout = Integer.parseInt(properties.getProperty("timeout"));
			Integer maxTotal = Integer.parseInt(properties.getProperty("maxTotal"));
			Integer maxIdle = Integer.parseInt(properties.getProperty("maxIdle"));
			Integer maxWaitMillis = Integer.parseInt(properties.getProperty("maxWaitMillis"));
			Boolean testOnBorrow = Boolean.parseBoolean(properties.getProperty("testOnBorrow"));
			Boolean testOnReturn = Boolean.parseBoolean(properties.getProperty("testOnReturn"));

			String ip = properties.getProperty("ip");
			Integer port = Integer.parseInt(properties.getProperty("port"));

			String master = properties.getProperty("master");
			String sentinelStr = properties.getProperty("sentinel");

			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setMaxIdle(maxIdle);
			poolConfig.setMaxTotal(maxTotal);
			poolConfig.setMaxWaitMillis(maxWaitMillis);
			poolConfig.setTestOnBorrow(testOnBorrow);
			poolConfig.setTestOnReturn(testOnReturn);

			// //连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
			// poolConfig.setBlockWhenExhausted(true);
			// //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
			// poolConfig.setMinEvictableIdleTimeMillis(10000);
			// //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数
			// 时直接逐出,不再根据MinEvictableIdleTimeMillis判断 (默认逐出策略)
			// poolConfig.setSoftMinEvictableIdleTimeMillis(10000);
			// //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
			// poolConfig.setTimeBetweenEvictionRunsMillis(30000);

			if (master != null && sentinelStr != null && sentinelStr != "") {
				Set<String> sentinels = new HashSet<String>();
				String[] sentinelArray = sentinelStr.split(",");
				for (String sentinel : sentinelArray) {
					sentinels.add(sentinel);
				}
				pool = new JedisSentinelPool(master, sentinels, poolConfig, timeout);// timeout读取超时
			} else {
				pool = new JedisPool(poolConfig, ip, port, timeout);
			}

		} catch (IOException e) {
			logger.error("初始化权限数据:{}", e);
		}

	}

	private RedisSupport() {

	}

	public static RedisSupport getInstance() {
		if (context == null) {
			synchronized (RedisSupport.class) {
				if (context == null) {
					context = new RedisSupport();
				}
			}
		}
		return context;
	}

	public Jedis getJedis() {
		return pool.getResource();
	}

	public void returnResource(Jedis jedis) {
		if (jedis != null) {
			pool.returnResource(jedis);
		}
	}

	public void returnBrokenResource(Jedis jedis) {
		if (jedis != null) {
			pool.returnBrokenResource(jedis);
		}
	}

	public String type(String key) {
		Jedis jedis = getJedis();
		String keyType = "";
		try {
			keyType = jedis.type(key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return keyType;
	}

	public long expire(String key, int seconds) {
		Jedis jedis = getJedis();
		long retVal = 0l;
		try {
			retVal = jedis.expire(key, seconds);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return retVal;
	}

	public void set(String key, String value) {
		Jedis jedis = getJedis();
		try {
			jedis.set(key, value);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public Long incr(String key) {
		Jedis jedis = getJedis();
		long retVal = 0l;
		try {
			retVal = jedis.incr(key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return retVal;
	}

	public Long incrBy(String key, long integer) {
		Jedis jedis = getJedis();
		long retVal = 0l;
		try {
			retVal = jedis.incrBy(key, integer);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return retVal;
	}

	public String get(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.get(key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public void lpush(String key, String value) {
		Jedis jedis = getJedis();
		try {
			jedis.lpush(key, value);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public List<String> lrange(String redisKey, long start, long end) {
		Jedis jedis = getJedis();
		try {
			return jedis.lrange(redisKey, start, end);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return new ArrayList<String>();
	}

	public void lpushRtbLog(String key, String value) {
		Jedis jedis = getJedis();
		try {
			jedis.lpush(key, value);
			jedis.lpush(key + "_log", value);
			jedis.ltrim(key + "_log", 0, 19999);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public void rpush(String key, String value) {
		Jedis jedis = getJedis();
		try {
			jedis.rpush(key, value);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public List<String> blpop(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.blpop(0, key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public List<String> brpop(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.brpop(0, key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}
	public List<String> brpop(int timeout,String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.brpop(timeout, key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public String rpop(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.rpop(key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long lpush(String key, String... values) {
		Jedis jedis = getJedis();
		try {
			return jedis.lpush(key, values);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public String lpop(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.lpop(key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	/**
	 * <p>
	 * 
	 * @Title: zcard
	 *         </p>
	 *         <p>
	 * @Description: 返回key中包含的member数量
	 *               </p>
	 *               <p>
	 * @author Comsys-xuanning
	 *         </p>
	 * 
	 * @param key
	 * @return
	 */
	public long zcard(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.zcard(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return 0l;
	}

	/**
	 * 判断某个对象是否在集合中，适用于做i c a 去重
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public boolean sismember(String key, String member) {
		Jedis jedis = getJedis();
		try {
			return jedis.sismember(key, member);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return false;
	}

	/**
	 * 往集合中增加一个对象
	 * 
	 * @param key
	 * @param members
	 */
	public void sadd(String key, String members) {
		Jedis jedis = getJedis();
		try {
			jedis.sadd(key, members);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 往集合中增加一个对象
	 * 
	 * @param key
	 * @param members
	 */
	public void zadd(String key, long score, String member) {
		Jedis jedis = getJedis();
		try {
			jedis.zadd(key, score, member);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * <p>
	 * 
	 * @Title: zrevrangeWithScores
	 *         </p>
	 *         <p>
	 * @Description: 查询出前几条记录
	 *               </p>
	 *               <p>
	 * @author Comsys-xuanning
	 *         </p>
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
		Jedis jedis = getJedis();
		try {
			return jedis.zrevrangeWithScores(key, start, end);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	/**
	 * set删除一个对象
	 * 
	 * @param key
	 * @param members
	 */
	public void srem(String key, String member) {
		Jedis jedis = getJedis();
		try {
			jedis.srem(key, member);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * zset删除一个对象
	 * 
	 * @param key
	 * @param members
	 */
	public void zrem(String key, String member) {
		Jedis jedis = getJedis();
		try {
			jedis.zrem(key, member);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 返回有序集 key 中，成员 member 的 score 值
	 * 
	 * @param key
	 * @param members
	 */
	public Double zscore(String key, String member) {
		Jedis jedis = getJedis();
		try {
			return jedis.zscore(key, member);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return -1d;
	}

	/**
	 * hash表field's value增操作，增值为long类型，适用于增加次数
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public void hincrBy(String key, String field, long value) {
		Jedis jedis = getJedis();
		try {
			jedis.hincrBy(key, field, value);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * hash表field's value增操作，增值为long类型，适用于增加花费数
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public void hincrByFloat(String key, String field, double value) {
		Jedis jedis = getJedis();
		try {
			jedis.hincrByFloat(key, field, value);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public void zincrby(String key, double score, String value) {
		Jedis jedis = getJedis();
		try {
			jedis.zincrby(key, score, value);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public void sadd(String key, String... values) {
		Jedis jedis = getJedis();
		try {
			jedis.sadd(key, values);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public long sadd(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.scard(key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return 0;
	}

	public long scard(String redisKey) {

		Jedis jedis = getJedis();
		try {
			return jedis.scard(redisKey);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return 0l;
	}

	public Set<String> smembers(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.smembers(key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public void hset(String key, String field, String value) {
		Jedis jedis = getJedis();
		try {
			jedis.hset(key, field, value);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public long hlen(String redisKey) {
		Jedis jedis = getJedis();
		try {
			return jedis.hlen(redisKey);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return 0l;
	}

	public String hget(String key, String field) {
		Jedis jedis = getJedis();
		try {
			return jedis.hget(key, field);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public long hdel(String key, String field) {
		Jedis jedis = getJedis();
		try {
			return jedis.hdel(key, field);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return 0;
	}

	public Map<String, String> hgetAll(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.hgetAll(key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Set<String> sunion(String... keys) {
		Jedis jedis = getJedis();
		try {
			return jedis.sunion(keys);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public void del(String... keys) {
		Jedis jedis = getJedis();
		try {
			jedis.del(keys);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public long llen(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.llen(key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return 0;
	}

	public Set<String> keys(String keyPattern) {
		Jedis jedis = getJedis();
		try {
			return jedis.keys(keyPattern);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

}

package common.utils.redis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.beanutils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

/**
 * 为支持数据批量入redis
 * 
 * @ClassName: RedisSupport
 * @Description: TODO
 * @author tank
 * @date 2014-10-15 下午3:07:23
 */
public class RedisCacheSupport {

	private static Logger logger = LoggerFactory.getLogger(RedisCacheSupport.class);

	public Queue<RedisModel> rtbQueue = new ConcurrentLinkedQueue<RedisModel>();

	private static int maxCacheNum = 100;
	private static int timeOut = 3000;// 豪秒
	private static RedisCacheSupport context;
	private static boolean isExit = false;

	private RedisCacheSupport() {
	}

	public static RedisCacheSupport getInstance() {
		if (context == null) {
			synchronized (RedisCacheSupport.class) {
				if (context == null) {
					context = new RedisCacheSupport();
					context.executor();
				}
			}
		}
		return context;
	}

	private void executor() {
		ExecutorService pool = Executors.newFixedThreadPool(1);
		pool.execute(new BeeThread());

	}

	private class BeeThread extends Thread {
		@Override
		public void run() {

			List<RedisModel> list = new ArrayList<RedisModel>();
			long startTime = System.currentTimeMillis();
			while (true) {
				if (list.size() >= maxCacheNum) {
					persistList(list);
					startTime = System.currentTimeMillis();
				} else {
					long diffTime = System.currentTimeMillis() - startTime;
					if (diffTime > timeOut) {
						persistList(list);
						startTime = System.currentTimeMillis();
					}
				}

				if (rtbQueue.isEmpty()) {
					if (!isExit) {
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							logger.error("{}", e);
						}
						continue;
					} else {
						break;
					}

				}

				RedisModel data = rtbQueue.poll();
				list.add(data);

			}

			persistList(list);
		}
	}

	public void persistList(List<RedisModel> list) {
		logger.trace("取队列中的数据存到redis库中");
		if (!list.isEmpty()) {
			RedisSupport redis = RedisSupport.getInstance();
			Jedis jedis = redis.getJedis();
			for (RedisModel redisModel : list) {
				try {
					MethodUtils.invokeMethod(jedis, redisModel.getMethodName(), redisModel.getParams());
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			redis.returnResource(jedis);

			list.clear();
		}
	}

	class RedisModel {
		private String methodName;
		private Object[] params;

		public RedisModel(String methodName, Object... p) {
			this.methodName = methodName;
			this.params = p;
		}

		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

		public Object[] getParams() {
			return params;
		}

		public void setParams(Object[] params) {
			this.params = params;
		}

	}

	/*
	 * 请参考jedis中的方法调用
	 * 
	 * @see redis.clients.jedis.Jedis#
	 */
	public void offer(String methodName, Object... p) {
		rtbQueue.offer(new RedisModel(methodName, p));

	}

	/*
	 * 请参考jedis中的方法调用
	 * 
	 * @see redis.clients.jedis.Jedis#
	 */
	public void offer(SimpleMethodName methodName, Object... p) {
		rtbQueue.offer(new RedisModel(methodName.getName(), p));

	}

	/**
	 * 程序关闭时写入缓存数据
	 */
	public static void shutdown() {
		isExit = true;
	}

	public enum SimpleMethodName {
		SET("set"), DECRBY("decrBy"), DECR("decr"), INCRBY("incrBy"), INCRBYFLOAT("incrByFloat"), INCR("incr"), HDEL("hdel"), DEL("del"), HINCRBY("hincrBy"), HINCRBYFLOAT(
				"hincrByFloat"), ZINCRBY("zincrby"), HSET("hset"), ZADD("zadd");

		private String name;

		SimpleMethodName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}

		public String getName() {
			return this.name;
		}
	}

}

package common.utils.bee;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.utils.json.JsonUtils;
import common.utils.redis.RedisSupport;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年8月11日 下午3:35:39
 * @description:通用的bee模式-redis 列队
 * @version :0.1
 */

public abstract class ThreadBeeRedisQueue<T> {

	private Logger LOG = LoggerFactory.getLogger(ThreadBeeRedisQueue.class);

	private static int poolsize = 1;
	private volatile boolean exit = false;
	private String redisKey = null;

	private List<Future> threadFuture = new ArrayList<Future>();

	private static volatile ThreadBeeRedisQueue context;

	public ThreadBeeRedisQueue(String key) {
		this(poolsize, key);
	}

	public ThreadBeeRedisQueue(int size, String key) {
		LOG.info("\n\t================{}启动ThreadBee,size:{}============\n", this.getClass().getName(), size);
		this.poolsize = size;

		this.redisKey = key;

		executor();

	}

	/**
	 * 执行任务
	 */
	public void executor() {
		Type type = this.getClass().getGenericSuperclass();

		Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];

		Class clazz = (Class) trueType;

		for (int i = 0; i < poolsize; i++) {
			Future future = ThreadPoolManager.submitDaemon(new Bee(clazz), "ThreadBee-bee-redis" + this.getClass().getName() + "-" + i);
			threadFuture.add(future);
		}
	}

	/**
	 * 对线程池进行扩容
	 * 
	 * @param newsize
	 */
	public void newPlan(int newsize) {
		LOG.info("修改ThreadBee的数目size:{}", newsize);
		for (Future future : threadFuture) {
			future.cancel(true);
		}
		this.poolsize = newsize;
		executor();
	}

	public boolean isExit() {
		return exit;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
	}

	public void runEmpty() {

	}

	public abstract void runBee(T t);

	public void push(T t) {
		if (t != null) {
			RedisSupport redis = RedisSupport.getInstance();
			redis.rpush(redisKey, JsonUtils.toJson(t));
		}

	}

	private class Bee extends Thread {
		private Class tclazz;

		public Bee(Class clazz) {
			this.tclazz = clazz;
		}

		public void run() {
			RedisSupport redis = RedisSupport.getInstance();

			while (!exit) {

				try {

					List<String> dataList = redis.brpop(5, redisKey);

					if (null != dataList && !dataList.isEmpty() && redisKey.equals(dataList.get(0))) {
						String json = dataList.get(1);

						Object obj = JsonUtils.toBean(json, tclazz);
						if (obj != null) {
							runBee((T) obj);
						}
					}else{
						LOG.info("{}",dataList);
					}

				} catch (Exception e) {
					LOG.error("{}", e);
				}

			}
		}
	}

}

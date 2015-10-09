package common.utils.bee;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年8月11日 下午3:35:39
 * @description:通用的bee模式
 * @version :0.1
 */

public abstract class ThreadBee<T> {

	private Logger LOG = LoggerFactory.getLogger(ThreadBee.class);

	protected Queue<T> queue = new ConcurrentLinkedQueue<T>();// 时间序列上的更新库操作语句

	private static int poolsize = 1;
	private volatile boolean exit = false;

	private List<Future> threadFuture = new ArrayList<Future>();

	private static volatile ThreadBee context;

	public ThreadBee() {
		this(poolsize);
	}

	public ThreadBee(int size) {
		LOG.info("\n\t================{}启动ThreadBee,size:{}============\n", this.getClass().getName(),size);
		this.poolsize = size;
		executor();
	}

	/**
	 * 执行任务
	 */
	public void executor() {

		for (int i = 0; i < poolsize; i++) {
			Future future = ThreadPoolManager.submitDaemon(new Bee(), "ThreadBee-bee-" + this.getClass().getName() + "-" + i);
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

	public void offer(T t) {
		queue.offer(t);
	}

	private class Bee extends Thread {

		public void run() {

			while (!exit) {
				// offer 添加一个元素并返回true 如果队列已满，则返回false
				// poll 移除并返问队列头部的元素 如果队列为空，则返回null
				try {
					T t = queue.poll();
					if (t != null) {
						runBee(t);
					} else {
						runEmpty();
						this.sleep(3000);
					}
				} catch (Exception e) {
					LOG.error("{}", e);
				}

			}
		}
	}

}

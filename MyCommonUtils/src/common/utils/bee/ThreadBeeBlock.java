package common.utils.bee;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年8月11日 下午3:35:39
 * @description:通用的bee模式-阻塞模式
 * @version :0.1
 */

public abstract class ThreadBeeBlock<T> {

	private Logger LOG = LoggerFactory.getLogger(ThreadBeeBlock.class);

	protected LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<T>();// 时间序列上的更新库操作语句

	private static int poolsize = 1;
	private volatile boolean exit = false;

	private List<Future> threadFuture = new ArrayList<Future>();

	private static volatile ThreadBeeBlock context;

	public ThreadBeeBlock() {
		this(poolsize);
	}

	public ThreadBeeBlock(int size) {
		LOG.info("\n\t================{}启动ThreadBee,size:{}============\n", this.getClass().getName(), size);
		this.poolsize = size;
		executor();
	}

	/**
	 * 执行任务
	 */
	public void executor() {
		// Executors.newFixedThreadPool(1).execute(new Bee());

		for (int i = 0; i < poolsize; i++) {
			Future future = ThreadPoolManager.getExecutorService().submit(new Bee(), "ThreadBee-bee-" + this.getClass().getName() + "-" + i);
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

	public abstract void runBee(T t);

	public void put(T t) {
		try {
			queue.put(t);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOG.error("{}", t);
			LOG.error("{}", e);
		}
	}

	private class Bee extends Thread {

		public void run() {

			while (!exit) {
				// put 添加一个元素 如果队列满，则阻塞
				// take 移除并返回队列头部的元素 如果队列为空，则阻塞
				try {
					T t = queue.take();
					runBee(t);

				} catch (Exception e) {
					LOG.error("{}", e);
				}

			}
		}
	}

}

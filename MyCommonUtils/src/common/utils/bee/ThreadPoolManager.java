package common.utils.bee;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年8月11日 下午2:38:38
 * @description:线程池管理
 * @version :0.1
 */

public class ThreadPoolManager {
	private static ExecutorService executor;
	static {
		executor = Executors.newCachedThreadPool();
		// 注册关闭钩子
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if (executor != null) {
					executor.shutdown();
				}
			}
		});

	}

	public static ExecutorService getExecutorService() {
		return executor;
	}

	public static <T> Future<T> submitDaemon(Thread task, T result) {
		task.setDaemon(true);
		return executor.submit(task, result);
	}

	public static Future<?> submitDaemon(Thread task, String name) {
		task.setName(name);
		task.setDaemon(true);
		return executor.submit(task);
	}

	public static Future<?> submitDaemon(Thread task) {
		task.setDaemon(true);
		return executor.submit(task);
	}

	public static void executeDaemon(Thread command) {
		command.setDaemon(true);
		executor.execute(command);
	}
}

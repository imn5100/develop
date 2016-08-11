package com.shaw.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public enum ThreadPoolManager {
	INSTANCE;// 唯一的实例
	private int POOL_SIZE_MIN = 60; // 线程池最小数量
	private int POOL_SIZE_MAX = 200;// 线程池最大数量
	private int TIME_KEEP_ALIVE = 180;// 线程允许空闲时间
	private int SIZE_WORK_QUEUE = 40;// 线程池缓存队列大小
	// 线程执行类 属于google Guava包下，在Future基础上对线程池的封装，
	ListeningExecutorService executorService;
	public static final boolean OPEN_LOGGER = true;

	private Logger logger = LoggerFactory.getLogger(ThreadPoolManager.class);

	// 通过构造函数完成 线程池初始化和装饰。
	ThreadPoolManager() {
		// 构造线程池执行器
		final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(POOL_SIZE_MIN, POOL_SIZE_MAX, TIME_KEEP_ALIVE,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(SIZE_WORK_QUEUE), new RejectedExecution());
		// 包装线程池执行器 为 :ListeningExecutorService
		executorService = MoreExecutors.listeningDecorator(threadPool);

		// 如果打开线程池日志，将适用房定时任务线程定时记录线程池状态到日志中
		if (OPEN_LOGGER) {
			Runnable mAccessBufferThread = new Runnable() {
				@Override
				public void run() {
					logger.debug(String.format(
							"business_treadPool thread message activeCount: %s,CompletedTaskCount: %s,taskCount: %s, queue_size: %s",
							threadPool.getActiveCount(), threadPool.getCompletedTaskCount(), threadPool.getTaskCount(),
							threadPool.getQueue().size()));
				}
			};
			// 初始化一个线程池大小为1的定时执行器。
			final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
			// 将日志记录runnable 加入线程中0 延迟 每一秒执行一次
			scheduler.scheduleAtFixedRate(mAccessBufferThread, 0, 1, TimeUnit.SECONDS);
		}
	}

	/** 向线程池中添加任务方法 */
	public <T> ListenableFuture<T> addExecuteTask(Callable<T> task) {
		return executorService.submit(task);
	}

	// 线程池拒绝任务处理策略
	public class RejectedExecution implements RejectedExecutionHandler {
		public RejectedExecution() {
		}
		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			logger.info("Reject msg: {} {}", r.toString(), executor.getActiveCount());
		}
	}
}
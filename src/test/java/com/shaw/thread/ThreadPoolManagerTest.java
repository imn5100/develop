package com.shaw.thread;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.shaw.thread.AsynTask.Execute;

public class ThreadPoolManagerTest {
	public static void main(String[] args) throws Exception {
		ListenableFuture<ReturnEntity> result = ThreadPoolManager.INSTANCE.addExecuteTask(new Callable<ReturnEntity>() {
			@Override
			public ReturnEntity call() throws Exception {
				ReturnEntity result = new ReturnEntity();
				try {
					Thread.sleep(1000 * 7);
					// 如果抛出任何一次，将会终止同一LIST中所有 线程执行
					// result.setException(new Exception("sss"));
					result.setResult("exe1");
				} catch (Exception e) {
					result.setException(e);
				}
				return result;

			}
		});
		ListenableFuture<ReturnEntity> result2 = ThreadPoolManager.INSTANCE
				.addExecuteTask(AsynTask.newTask("test2").registExecute(new Execute() {
					@Override
					public Object execute() throws Exception {
						Thread.sleep(1000 * 5);
						return "asynTask  result";
					}
				}));

		/*
		 * 对多个ListenableFuture的合并，返回一个当所有Future成功时返回多个Future返回值组成的List对象
		 * 对于失败或取消的Future返回值用null代替。不会进入失败或者取消流程
		 */
		@SuppressWarnings("unchecked")
		ListenableFuture<List<ReturnEntity>> allFutures = Futures.successfulAsList(result, result2);
		/*
		 * 返回一个ListenableFuture ，该ListenableFuture
		 * 返回的result是一个List，List中的值是每个ListenableFuture的返回值，
		 * 假如传入的其中之一fails或者cancel，这个Future fails 或者canceled
		 */
		// ListenableFuture<List<ReturnEntity>> allFutures2 = Futures.allAsList(result, result2);

		// 检查所有Future 不为空，空时抛出空指针异常
		Preconditions.checkNotNull(allFutures);
		// 设置定时获取Future结果，获取失败则取消所有 Future 并抛出异常
		List<ReturnEntity> allReturnEntity = catchFutureGetException(allFutures, 20, TimeUnit.SECONDS);
		Preconditions.checkNotNull(allReturnEntity);

		for (ReturnEntity entity : allReturnEntity) {
			if (null != entity) {
				// 尝试抛出异常，如果Future执行中出现异常将会抛出。
				entity.throwException();
				// 没有异常获取结果
				if (entity.hasResult()) {
					// 有任何一个结果返回则 立即结束
					allFutures.cancel(true);
					// 对result执行操作
					System.out.println(entity.getResult());
				}
			}
		}

	}

	// 设置定时获取Future结果，获取失败则取消所有 Future 并抛出异常
	private static List<ReturnEntity> catchFutureGetException(ListenableFuture<List<ReturnEntity>> allFutures,
			long timeout, TimeUnit unit) throws Exception {
		try {
			return allFutures.get(timeout, unit);
		} catch (Exception e) {
			cancle(allFutures);
			throw e;
		}
	}

	// 取消所有执行
	private static void cancle(ListenableFuture<List<ReturnEntity>> allFutures) {
		if (allFutures != null) {
			allFutures.cancel(true);
		}
	}

	// 通过callBack 完成Future 的结果获取和失败调用
	private static void callBack(ListenableFuture<List<ReturnEntity>> explosion) {
		Futures.addCallback(explosion, new FutureCallback<Object>() {
			@Override
			public void onSuccess(Object result) {
			}

			@Override
			public void onFailure(Throwable t) {
			}
		});
	}
}

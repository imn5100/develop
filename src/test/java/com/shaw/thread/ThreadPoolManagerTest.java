package com.shaw.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.ListenableFuture;

public class ThreadPoolManagerTest {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ListenableFuture<String> result = ThreadPoolManager.INSTANCE.addExecuteTask(new Callable<String>() {
			@Override
			public String call() throws Exception {
				Thread.sleep(1000 * 5);
				return "waitting";
			}
		});
	      //使用get方法是，当前线程发送阻塞。直到取到结果。
		System.out.println(result.get());
	}
}

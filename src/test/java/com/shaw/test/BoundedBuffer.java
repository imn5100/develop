package com.shaw.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 实现容器，put和take方法，容器为空时take方法等待put，容器满时put方法等待take取走元素。容器需支持泛型。
 * put，take分两种，stack  先进后出，queue 先进先出
 * **/
public class BoundedBuffer<T> {
	public static void main(String[] args) {
		final BoundedBuffer<Integer> boundedBuffer = new BoundedBuffer<Integer>();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 101; i++) {
					try {
						boundedBuffer.sPut(i);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 101; i++) {
					try {
						boundedBuffer.sTake();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		t1.start();
		t2.start();

		/**
		 * 存取顺序，无论线程如何运行，一定是先 put 再take。put满时，调用take，从0或低位开始。take到空，等待put操作。
		 **/
	}

	Lock lock = new ReentrantLock();
	final Condition notEmpty = lock.newCondition();
	final Condition notFull = lock.newCondition();
	final Object[] items = new Object[10];
	int putptr, takeptr, count;

	// stack版本 先进后出 只用count控制数据存取。
	public T sTake() throws InterruptedException {
		lock.lock();
		try {
			while (count == 0) {
				System.out.println("waiting  put value");
				notEmpty.await();
			}
			@SuppressWarnings("unchecked")
			T object = (T) items[(count - 1)];
			--count;
			notFull.signal();
			System.out.println("take:" + object);
			return object;
		} finally {
			lock.unlock();
		}
	}

	public void sPut(T t) throws InterruptedException {
		lock.lock();
		try {
			while (count == 10) {
				System.out.println("waiting for take value");
				notFull.await();
			}
			items[count] = t;
			++count;
			notEmpty.signal();
			System.out.println("put:" + t);
		} finally {
			lock.unlock();
		}
	}

	/*
	 * queue版本先进先出 需要存取角标计数 takptr putprt
	 */
	public T qTake() throws InterruptedException {
		lock.lock();
		try {
			while (count == 0) {
				System.out.println("waiting  put value");
				notEmpty.await();
			}
			@SuppressWarnings("unchecked")
			T object = (T) items[takeptr];
			if (++takeptr == items.length) {
				takeptr = 0;
			}
			--count;
			notFull.signal();
			System.out.println("take:" + object);
			return object;
		} finally {
			lock.unlock();
		}
	}

	public void qPut(T t) throws InterruptedException {
		lock.lock();
		try {
			while (count == 10) {
				System.out.println("waiting for take value");
				notFull.await();
			}
			items[putptr] = t;
			if (++putptr == items.length) {
				putptr = 0;
			}
			++count;
			notEmpty.signal();
			System.out.println("put:" + t);
		} finally {
			lock.unlock();
		}
	}
}

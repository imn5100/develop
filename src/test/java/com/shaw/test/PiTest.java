package com.shaw.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PiTest {
	public static void main(String[] args) {
		new PiTest().run();
	}

	private Lock lock = new ReentrantLock();
	final Condition flag1 = lock.newCondition();
	final Condition flag2 = lock.newCondition();
	int i = 1;

	class Thread1 implements Runnable {
		@Override
		public void run() {
			System.out.println("thred1 start");
			lock.lock();
			System.out.println("thred1 start after lock");
			try {
				System.out.println("start 1-->3");
				while (i <= 3) {
					System.out.println("Thread1:" + i);
					i++;
				}
				flag1.signal();
				// 等待flag2变量通知
				flag2.await();
				System.out.println("start 6-->9");
				while (i <= 9) {
					System.out.println("Thread1:" + i);
					i++;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}

	class Thread2 implements Runnable {
		@Override
		public void run() {
			System.out.println("thred2 start");
			lock.lock();
			System.out.println("thred2 start after lock");
			try {
				while (i <= 3) {
					// 当i小于等于3时，等待flag1变量通知。
					flag1.await();
				}
				System.out.println("start 4-->6");
				// flag1变量通知，运行输出4-6
				while (i <= 6) {
					System.out.println("Thread2:" + i);
					i++;
				}
				flag2.signal();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}

	public void run() {
		Thread thread1 = new Thread(new Thread1());
		Thread thread2 = new Thread(new Thread2());
		thread1.start();
		thread2.start();
	}

}

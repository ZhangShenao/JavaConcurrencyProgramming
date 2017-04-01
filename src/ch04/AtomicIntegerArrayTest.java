package ch04;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 
 * <p>Description:AtomicIntegerArray,无锁的并发安全的数组，本质上是对int[]的封装，内部采用CAS策略控制int[]在多线程下的安全性</p>
 * @author ZhangShenao
 * @date 2017年4月1日
 */
public class AtomicIntegerArrayTest {
	/**
	 * 数组的长度
	 */
	private static final int ARRAY_LEN = 10;
	
	/**
	 * 线程数
	 */
	private static final int THREAD_NUM = Runtime.getRuntime().availableProcessors() * 2;
	
	/**
	 * 无锁的并发数组
	 */
	private static final AtomicIntegerArray arr = new AtomicIntegerArray(ARRAY_LEN);
	
	/**
	 * 倒计时器
	 */
	private static final CountDownLatch latch = new CountDownLatch(THREAD_NUM);
	
	private static class AddArrTask implements Runnable{

		@Override
		public void run() {
			//增加数组中元素的值
			for (int i = 0;i < 100000;i++){
				arr.incrementAndGet(i % ARRAY_LEN);
			}
			
			latch.countDown();
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		for (int i = 0;i < THREAD_NUM;i++){
			new Thread(new AddArrTask()).start();
		}
		latch.await();
		
		System.out.println(arr);
	}
	
}

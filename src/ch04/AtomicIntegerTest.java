package ch04;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * <p>Description: AtomicInteger原子类，采用CAS算法保证了线程安全</p>
 * @author ZhangShenao
 * @date 2017年4月1日
 */
public class AtomicIntegerTest {
	//使用AtomicInteger计数,可以保证线程安全
	private static AtomicInteger count = new AtomicInteger(0);
	
	private static class AddTask implements Runnable{

		@Override
		public void run() {
			for (int i = 0;i < 10000;i++){
				//使count自增1,为线程安全的++count
				//底层通过unsafe.compareAndSwapInt(this, valueOffset, expect, update)方法实现
				//unsafe类封装了一些类似指针的不安全的操作，只能由JDK系统的内部类使用，如果应用程序直接使用Unsafe类则会抛出异常
				count.incrementAndGet();
			}
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		int threadNum = 10;
		Thread[] threads = new Thread[threadNum];
		
		for (int i = 0;i < threadNum;i++){
			threads[i] = new Thread(new AddTask());
		}
		
		for (int i = 0;i < threadNum;i++){
			threads[i].start();
			threads[i].join();
		}
		
		System.out.println("count:" + count.get());
	}
}

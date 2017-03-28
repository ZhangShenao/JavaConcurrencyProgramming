package ch03;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * <p>Description: ReentrantLock可入重入锁。可重入，指一个线程可以多次获得同一把锁，但是要确保加锁和释放锁得操作一一对应</p>
 * @author ZhangShenao
 * @date 2017年3月28日
 */
public class LockTest {
	private static int count = 0;
	
	/**
	 * 创建锁对象
	 */
	private static final Lock lock = new ReentrantLock();
	
	private static class AddTask implements Runnable{

		@Override
		public void run() {
			try {
				//手动加锁
				lock.lock();
				for (int i = 0;i < 100000;i++){
					count++;
				}
			}finally {
				//手动释放锁
				lock.unlock();
			}
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new AddTask());
		Thread t2 = new Thread(new AddTask());
		
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
		
		System.out.println("count: " + count);
	}
}

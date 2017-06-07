package ch05;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

import org.junit.Test;

import ch04.SleepUtil;

/**
 * 
 * <p>Description:测试自定义同步组件TwinsLock</p>
 * @author ZhangShenao
 * @date 2017年6月7日
 */
public class TwinsLockTest {
	/**
	 * 自定义同步组件
	 */
	private static final Lock twinsLock = new TwinsLock();
	
	/**
	 * 线程数
	 */
	private static final int THREAD_NUM = 10;
	
	/**
	 * 等待所有线程结束的倒计时器
	 */
	private static final CountDownLatch waitAllThread = new CountDownLatch(THREAD_NUM);
	
	@Test
	public void test() throws InterruptedException{
		Worker worker = null;
		for (int i = 0;i < THREAD_NUM;i++){
			worker = new Worker("Worker-" + i);
			worker.start();
		}
		waitAllThread.await();
	}
	
	private static class Worker extends Thread{
		public Worker(String name){
			super(name);
		}
		@Override
		public void run() {
			twinsLock.lock();
			try {
				SleepUtil.sleep(1);
				System.out.println("Thread Name:" + getName() + " Complete!!");
			}finally {
				if (null != twinsLock){
					twinsLock.unlock();
				}
				waitAllThread.countDown();
			}
		}
	}

}

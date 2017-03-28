package ch03;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * <p>Description:  Lock的tryLock()方法可以限时等待锁，避免的因无法得到锁而永久等待的情况</p>
 * @author ZhangShenao
 * @date 2017年3月28日 下午10:48:35
 */
public class TimeLock implements Runnable{
	private static ReentrantLock lock = new ReentrantLock();

	@Override
	public void run() {
		try {
			//尝试获取锁，最大等待时长为5s
			if (lock.tryLock(5, TimeUnit.SECONDS)){
				Thread.sleep(6000);
			}else {
				//等待超时，请求锁失败，线程继续向下执行
				System.err.println("wait for lock time out!!");
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if (lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
	}
	
	public static void main(String[] args) {
		Thread t1 = new Thread(new TimeLock());
		Thread t2 = new Thread(new TimeLock());
		t1.start();
		t2.start();
	}
}

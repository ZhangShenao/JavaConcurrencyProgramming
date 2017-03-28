package ch03;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * <p>Description:  构造公平锁。实现公平锁需要系统根据时间的先后顺序，维护一个有序队列，因此公平锁实现成本较高，且性能较低。
 * 默认采用非公平锁</p>
 * @author ZhangShenao
 * @date 2017年3月28日 下午10:59:43
 */
public class FairLock implements Runnable{
	//构造一个公平锁
	//如果采用非公平锁策略，根据系统的调度，一个线程会倾向于再次获取已经持有的锁，这种分配方式比较高效
	private static final ReentrantLock fairLock = new ReentrantLock(true);
	
	@Override
	public void run() {
		while (true){
			try {
				fairLock.lock();
				System.out.println("线程: " + Thread.currentThread().getName() + " 获得到锁");
				Thread.sleep(500);
			}
			catch (Exception e){
				e.printStackTrace();
			}
			finally {
				if (fairLock.isHeldByCurrentThread()){
					fairLock.unlock();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Thread t1 = new Thread(new FairLock(), "T1");
		Thread t2 = new Thread(new FairLock(), "T2");
		
		//采用公平锁策略时，两个线程轮流获取锁
		t1.start();
		t2.start();
	}
}

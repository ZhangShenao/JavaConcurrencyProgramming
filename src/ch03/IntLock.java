package ch03;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * <p>Description:  Lock的lockInterruptibly方法，线程在等待锁的过程中可以响应中断，在一定程度上减少了死锁的产生</p>
 * @author ZhangShenao
 * @date 2017年3月28日 下午10:33:15
 */
public class IntLock implements Runnable{
	private static ReentrantLock lock1 = new ReentrantLock();
	private static ReentrantLock lock2 = new ReentrantLock();
	
	//控制加锁的顺序，方便构造死锁
	private int lockNum = 0;

	public IntLock(int lockNum) {
		this.lockNum = lockNum;
	}

	@Override
	public void run() {
		try {
			if (lockNum == 1){
				try {
					//在等待锁的过程中可以响应中断
					lock1.lockInterruptibly();
					Thread.sleep(500);
					lock2.lockInterruptibly();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {
				try {
					lock2.lockInterruptibly();
					Thread.sleep(500);
					lock1.lockInterruptibly();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}finally {
			//按照固定的顺序释放锁
			if (lock1.isHeldByCurrentThread()){
				lock1.unlock();
			}
			if (lock2.isHeldByCurrentThread()){
				lock2.unlock();
			}
			System.out.println(Thread.currentThread().getName() + ",线程退出");
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new IntLock(1));
		Thread t2 = new Thread(new IntLock(2));
		
		//两个线程互相持有对方的锁，两个线程相互等待对方释放锁，会导致死锁
		t1.start();
		t2.start();
		
		Thread.sleep(2000);
		
		//中断其中一个线程
		t1.interrupt();
	}
	
}

package ch03;

import java.util.concurrent.locks.LockSupport;

/**
 * 
 * <p>Description:  LockSupport,线程阻塞工具,可以在线程任意位置让线程阻塞,不需先获得锁,也不会抛出InterruptedException</p>
 * @author ZhangShenao
 * @date 2017年3月29日 下午10:24:50
 */
public class LockSupportTest {
	//同步监视器
	private static final Object obj = new Object();
	
	private static class ChangeObjectThread extends Thread{
		public ChangeObjectThread(String name){
			super(name);
		}
		@Override
		public void run() {
			synchronized (obj) {
				System.out.println("Thread: " + getName());
				
				//使用LockSupport阻塞当前线程
				//LockSupport.park();
				
				//可以为当前线程设置一个阻塞对象
				LockSupport.park(this);
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		ChangeObjectThread t1 = new ChangeObjectThread("t1");
		ChangeObjectThread t2 = new ChangeObjectThread("t2");
		
		t1.start();
		Thread.sleep(Long.MAX_VALUE);
		t2.start();
		
		//唤醒阻塞的线程
		//LockSupport内部使用了类似信号量的机制,有一个许可,如果许可可用,park方法将立即返回而不等待;unpark将许可设置为可用
		//不会出现因为unpark()方法在park方法之前执行而导致线程一直等待的情况
		LockSupport.unpark(t1);
		LockSupport.unpark(t2);
		
		t1.join();
		t2.join();
	}
}


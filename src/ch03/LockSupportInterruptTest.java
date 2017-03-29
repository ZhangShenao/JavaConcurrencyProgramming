package ch03;

import java.util.concurrent.locks.LockSupport;

/**
 * 
 * <p>Description:  LockSupport可以响应中断,但是并不会抛出InterruptException,而只是默默地返回</p>
 * @author ZhangShenao
 * @date 2017年3月29日 下午10:24:50
 */
public class LockSupportInterruptTest {
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
				LockSupport.park();
				
				if (isInterrupted()){
					System.out.println("Thread: " + getName() + " 被中断了");
				}
				
				System.out.println("Thread: " + getName() + " 执行结束");
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		ChangeObjectThread t1 = new ChangeObjectThread("t1");
		ChangeObjectThread t2 = new ChangeObjectThread("t2");
		
		t1.start();
		Thread.sleep(200);
		t2.start();
		
		//中断线程t1
		t1.interrupt();
		
		//t1中断后,t1执行结束释放锁,t2获得锁对象,执行park等待,并由unpark唤醒,执行结束
		LockSupport.unpark(t2);
	}
}


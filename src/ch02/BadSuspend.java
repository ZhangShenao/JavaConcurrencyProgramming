package ch02;

/**
 * 
 * Title: BadSuspend
 * Description:  线程挂起可能出现的问题
 * @author ZhangShenao
 * @date 2017年3月25日 上午9:36:39
 */
public class BadSuspend {
	private static final Object obj = new Object();
	public static class ChangeObjectThread extends Thread{
		public ChangeObjectThread(String name){
			super(name);
		}
		@Override
		public void run() {
			synchronized (obj) {
				System.out.println("线程:" + getName() + "进入临界区");
				
				//挂起线程，等待resume
				//如果resume在suspend之前被执行，则线程会永远挂起，并始终不会释放对象锁，可能导致线程死锁
				suspend();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		ChangeObjectThread t1 = new ChangeObjectThread("t1");
		t1.start();
		Thread.sleep(100);
		ChangeObjectThread t2 = new ChangeObjectThread("t2");
		t2.start();
		
		//resume线程
		t1.resume();
		t2.resume();
	}
}

package ch02;

public class GoodSuspend {
	/**
	 * 对象锁
	 */
	private static final Object obj = new Object();
	
	public static class ChangeObjectThread extends Thread{
		/**
		 * 线程是否被挂起的标志
		 */
		private volatile boolean isSuspend = false;
		
		/**
		 * 安全地挂起线程
		 */
		public void safeSuspend(){
			isSuspend = true;
		}
		
		/**
		 * 安全地恢复线程
		 */
		public void safeResume(){
			isSuspend = false;
			
			synchronized (this) {
				//唤醒由于挂起而等待的线程
				this.notify();
			}
		}
		
		@Override
		public void run() {
			while (true){
				synchronized (this) {
					//如果当前线程被挂起，则等待
					while (isSuspend){
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
				//如果果当前线程未被挂起，则正常进行操作
				synchronized (obj) {
					System.out.println("ChangeObjectThread is working");
				}
				
				Thread.yield();
			}
		}
	}
	
	public static class ReadObjectThread extends Thread{
		@Override
		public void run() {
			while (true){
				synchronized (obj) {
					System.out.println("ReadObjectThread is working");
				}
				Thread.yield();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		ChangeObjectThread t1 = new ChangeObjectThread();
		ReadObjectThread t2 = new ReadObjectThread();
		t1.start();
		t2.start();
		Thread.sleep(1000);
		t1.safeSuspend();
		System.out.println("suspend t1");
		Thread.sleep(2000);
		t1.safeResume();
		System.out.println("resume t1");
	}
}

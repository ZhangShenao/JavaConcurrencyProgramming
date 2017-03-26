package ch02;

/**
 * Title: SimpleWaitAndNotify
 * Description: 简单测试wait()和notify方法
 * @author ZhangShenao
 * @date 2017年3月25日 上午9:14:39
 */
public class SimpleWaitAndNotify {
	/**
	 * 同步监视器对象
	 */
	private static Object obj = new Object();
	
	public static class T1 extends Thread{
		@Override
		public void run() {
			//在执行wait方法之前，要先获得同步监视器
			synchronized (obj) {
				System.out.println(System.currentTimeMillis() + ": T1 start");
				
				//执行wait方法，线程等待
				try {
					System.out.println(System.currentTimeMillis() + ": T1 wait for obj");
					obj.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//等待的线程被唤醒后，要先获得同步监视器，才能继续执行
				System.out.println(System.currentTimeMillis() + ": T1 stop");
			}
		}
	}
	
	public static class T2 extends Thread{
		@Override
		public void run() {
			//在执行notify方法之前，要先获得同步监视器
			synchronized (obj) {
				System.out.println(System.currentTimeMillis() + ": T2 start");
				
				//唤醒等待的线程
				obj.notify();
				
				System.out.println(System.currentTimeMillis() + ": T2 stop");
				
				try {
					//线程休眠，并不释放同步监视器
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//在此处，线程释放同步监视器，等待的线程才能获取同步监视器继续执行
		}
	}
	
	public static void main(String[] args) {
		T1 t1 = new T1();
		T2 t2 = new T2();
		t1.start();
		t2.start();
	}
}

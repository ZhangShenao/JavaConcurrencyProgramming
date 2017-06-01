package ch04;

/**
 * 
 * <p>Description:使用wait/notify进行线程通信</p>
 * @author ZhangShenao
 * @date 2017年6月1日
 */
public class WaitNotifyTest {
	/**
	 * 是否准备就绪的标志
	 */
	private static volatile boolean isReady = false;
	
	/**
	 * 同步监视器对象
	 */
	private static Object lock = new Object();
	
	public static void main(String[] args) {
		Thread waitThread = new Thread(new WaitTask(), "Wait-Thread");
		waitThread.start();
		
		Thread notifyThread = new Thread(new NotifyTask(), "Notify-Thread");
		notifyThread.start();
	}
	
	//等待的线程
	private static class WaitTask implements Runnable{

		@Override
		public void run() {
			//加锁
			synchronized (lock) {
				System.out.println(Thread.currentThread().getName() + "获取锁");
				//如果未准备就绪,则线程等待在wait方法上,并释放锁
				while (!isReady){
					System.out.println(Thread.currentThread().getName() + "线程等待");
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				//准备就绪后,线程从wait方法恢复,等待重新获取锁后继续执行
				System.out.println(Thread.currentThread().getName() + "线程继续执行");
			}
		}
	}
	
	//唤醒的线程
	private static class NotifyTask implements Runnable{
		@Override
		public void run() {
			//加锁
			synchronized (lock) {
				System.out.println(Thread.currentThread().getName() + "获取锁");
				
				//修改标记位并唤醒等待的线程
				isReady = true;
				lock.notifyAll();
				System.out.println(Thread.currentThread().getName() + "唤醒其他线程");
				
				//再次获取锁
				synchronized (lock) {
					System.out.println(Thread.currentThread().getName() + "再次获取锁获取锁");
					SleepUtil.sleep(2);
				}
			}
			
			//唤醒的线程释放锁对象,等待中的线程才能获取锁继续执行
			System.out.println(Thread.currentThread().getName() + "执行完毕");
		}
	}
}

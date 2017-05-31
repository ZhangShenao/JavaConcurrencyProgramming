package ch04;

/**
 * 
 * <p>Description:  线程的状态:New、Runnable、Waiting、Timed_Waiting、Blocked、Terminated</p>
 * @author ZhangShenao
 * @date 2017年6月1日 上午7:38:13
 */
public class ThreadState {
	public static void main(String[] args) {
		Thread timedWaitingThread = new Thread(new TimedWaitingTask(), "Timed_Waiting Thread");
		timedWaitingThread.start();
		
		Thread waitingThread = new Thread(new WaitingTask(), "Waiting Thread");
		waitingThread.start();
		
		Thread blockedThread1 = new Thread(new BlockedTask(), "Blocked Thread-1");
		blockedThread1.start();
		
		Thread blockedThread2 = new Thread(new BlockedTask(), "Blocked Thread-2");
		blockedThread2.start();
	}
	
	//该线程不断地进行睡眠,处于Timed_Waiting状态
	private static class TimedWaitingTask implements Runnable {

		@Override
		public void run() {
			for (;;){
				SleepUtil.sleep(100);
			}
		}
	}
	
	//该线程在WaitingTask.class等待被唤醒,处于Waiting状态
	private static class WaitingTask implements Runnable {
		@Override
		public void run() {
			for (;;){
				synchronized (WaitingTask.class) {
					try {
						WaitingTask.class.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	//该线程在BlockedTask.class加锁后,不会释放锁,等待锁的线程处于Blocked状态
	private static class BlockedTask implements Runnable {
		@Override
		public void run() {
			synchronized (BlockedTask.class) {
				for (;;){
					SleepUtil.sleep(100);
				}
			}
		}
	}
	
	
}

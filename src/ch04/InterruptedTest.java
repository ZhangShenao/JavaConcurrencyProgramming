package ch04;

/**
 * 
 * <p>Description:线程的中断</p>
 * <p>有许多声明抛出InterruptedException的方法,这些方法在抛出InterruptedException之前,会先将该线程的中断标志位清除</p>
 * @author ZhangShenao
 * @date 2017年6月1日
 */
public class InterruptedTest {
	public static void main(String[] args) {
		Thread sleepThread = new Thread(new SleepTask(), "Sleep Thread");
		sleepThread.setDaemon(true);
		
		Thread busyThread = new Thread(new BusyTask(), "Busy Thread");
		busyThread.setDaemon(true);
		
		sleepThread.start();
		busyThread.start();
		
		//休眠,让线程充分运行
		SleepUtil.sleep(5);
		
		//中断两个线程
		sleepThread.interrupt();
		busyThread.interrupt();
		
		System.out.println("Sleep Thread是否被中断: " + sleepThread.isInterrupted());
		System.out.println("Busy Thread是否被中断: " + busyThread.isInterrupted());
		
		//防止被中断的线程立即退出
		SleepUtil.sleep(2);
		
	}
	
	private static class SleepTask implements Runnable{
		@Override
		public void run() {
			for (;;){
				SleepUtil.sleep(10);
			}
		}
	}
	
	private static class BusyTask implements Runnable{
		@Override
		public void run() {
			for (;;){
			}
		}
	}
}

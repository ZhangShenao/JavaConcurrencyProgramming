package ch02;

/**
 * 
 * <p>Description:  线程优先级 Java支持1~10的线程优先级。
 * 高优先级的线程更有可能先抢占到共享资源，但也不是绝对的</p>
 * @author ZhangShenao
 * @date 2017年3月26日 下午8:37:26
 */
public class PriorityTest {
	//高优先级的线程
	private static class HighPriorityThread extends Thread{
		@Override
		public void run() {
			int i = 0;
			while (true){
				synchronized (PriorityTest.class) {
					if (i++ >= 10000){
						System.out.println("HighPriorityThread is complete!!");
						break;
					}
				}
			}
		}
	}
	
	//低优先级的线程
	private static class LowPriorityThread extends Thread{
		@Override
		public void run() {
			int i = 0;
			while (true){
				synchronized (PriorityTest.class) {
					if (i++ >= 10000){
						System.out.println("LowPriorityThread is complete!!");
						break;
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		HighPriorityThread highPriorityThread = new HighPriorityThread();
		LowPriorityThread lowPriorityThread = new LowPriorityThread();
		highPriorityThread.setPriority(Thread.MAX_PRIORITY);
		lowPriorityThread.setPriority(Thread.MIN_PRIORITY);
		
		highPriorityThread.start();
		lowPriorityThread.start();
	}
}

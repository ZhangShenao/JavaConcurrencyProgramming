package ch04;

/**
 * 
 * <p>Description:安全地终止线程</p>
 * @author ZhangShenao
 * @date 2017年6月1日
 */
public class ShutdownTest {
	public static void main(String[] args) {
		Counter c1 = new Counter();
		Counter c2 = new Counter();
		Thread counter1 = new Thread(c1, "Counter-Thread-1");
		Thread counter2 = new Thread(c2, "Counter-Thread-2");
		
		counter1.start();
		counter2.start();
		
		SleepUtil.sleep(5);
		
		//通过修改volatile标记来终止任务
		c1.cancel();
		c2.cancel();
		
	}
	
	private static class Counter implements Runnable{
		private long value;
		
		/**
		 * 使用一个volatile变量控制线程的结束
		 */
		private volatile boolean on = true;
		
		@Override
		public void run() {
			while (on && !Thread.currentThread().isInterrupted()){
				++value;
			}
			
			System.out.println("value= " + value);
		}
		
		public void cancel(){
			on = false;
		}
	}
}

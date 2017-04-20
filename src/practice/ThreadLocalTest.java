package practice;

/**
 * 
 * <p>Description:使用ThreadLocal,每个线程都持有一个副本</p>
 * @author ZhangShenao
 * @date 2017年4月19日
 */
public class ThreadLocalTest {
	private static ThreadLocal<String> msg = new ThreadLocal<String>();
	
	private static class MyTask extends Thread{
		@Override
		public void run() {
			for (int i = 0;i < 10;i++){
				if (i == 5){
					msg.set("MyTask");
				}
				System.out.println("MyTask-->msg: " + msg.get());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		MyTask myTask = new MyTask();
		myTask.start();
		for (int i = 0;i < 10;i++){
			if (i == 5){
				msg.set("Main");
			}
			System.err.println("Main-->msg: " + msg.get());
			Thread.sleep(1000);
		}
		
	}
}

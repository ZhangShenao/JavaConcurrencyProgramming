package ch04;

/**
 * 
 * <p>Description:join方法,等待线程结束</p>
 * @author ZhangShenao
 * @date 2017年6月1日
 */
public class JoinTest {
	public static void main(String[] args) {
		Thread previous = Thread.currentThread();
		for (int i = 1;i < 11;i++){
			Thread t = new Thread(new JoinTask(previous), "Thread-" + i);
			t.start();
			previous = t;
		}
	}
	
	private static class JoinTask implements Runnable{
		/**
		 * 保存前置线程的引用
		 */
		private Thread previous;
		
		
		public JoinTask(Thread previous) {
			this.previous = previous;
		}

		@Override
		public void run() {
			try {
				//等待前置线程执行完毕
				if (null != previous){
					previous.join();
				}
				
				//执行当前线程
				System.out.println(Thread.currentThread().getName() + " 执行完毕");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}

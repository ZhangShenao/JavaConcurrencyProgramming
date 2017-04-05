package ch04;

/**
 * 
 * <p>Description:使用哲学家就餐问题模拟多线程的死锁</p>
 * @author ZhangShenao
 * @date 2017年4月5日
 */
public class DeadLockTest {
	//两把叉子
	private static final Object fork1 = new Object();
	private static final Object fork2 = new Object();
	
	//模拟哲学家吃饭的线程
	private static class EatTask implements Runnable{
		private Object tool;
		
		public EatTask(Object tool) {
			this.tool = tool;
		}

		@Override
		public void run() {
			//第一位哲学家，先拿第一个叉子，再拿第二个叉子
			if (tool == fork1){
				synchronized (fork1) {
					System.out.println("哲学家1拿到第一把叉子");
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					synchronized (fork2) {
						System.out.println("哲学家1拿到第一把叉子");
						System.out.println("哲学家1开始吃饭");
					}
				}
			}
			
			//第二位哲学家，先拿第二个叉子，再拿第一个叉子
			else{
				synchronized (fork2) {
					System.out.println("哲学家2拿到第二把叉子");
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					synchronized (fork1) {
						System.out.println("哲学家2拿到第一把叉子");
						System.out.println("哲学家2开始吃饭");
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Thread t1 = new Thread(new EatTask(fork1));
		Thread t2 = new Thread(new EatTask(fork2));
		t1.start();
		t2.start();
	}
}

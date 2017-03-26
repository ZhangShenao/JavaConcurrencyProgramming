package ch02;

/**
 * 
 * <p>Description:  volatile关键字可以保证变量在多个线程间的可见性，但无法保证复合操作的原子性</p>
 * @author ZhangShenao
 * @date 2017年3月26日 上午10:27:11
 */
public class VolatileTest {
	private static volatile int count = 0;
	
	private static class AddTask implements Runnable{
		@Override
		public void run() {
			for (int i = 0;i < 10000;i++){
				++count;
			}
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread[] threads = new Thread[10];
		for (int i = 0;i < 10;i++){
			threads[i] = new Thread(new AddTask());
			threads[i].start();
		}
		for (int i = 0;i < 10;i++){
			threads[i].join();
		}
		
		//理论上,count的值应该为100000,但由于++操作并不是原子性的，导致实际值小于100000
		System.out.println("count = " + count);
	}
}

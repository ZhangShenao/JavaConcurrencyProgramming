package ch03;

import java.util.concurrent.Semaphore;

/**
 * 
 * <p>Description:  信号量，可以指定多个线程同时访问共享资源</p>
 * @author ZhangShenao
 * @date 2017年3月29日 上午7:29:19
 */
public class SemaphoreTest implements Runnable{
	//构造Semaphore对象，指定准入线程数
	private static final Semaphore semaphore = new Semaphore(5);

	@Override
	public void run() {
		try {
			//获得准入许可
			semaphore.acquire();
			Thread.sleep(2000);
			
			System.out.println(Thread.currentThread().getName() + " complete!!");
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			//释放准入许可
			semaphore.release();
		}
	}
	
	public static void main(String[] args) {
		Thread t = null;
		for (int i = 1;i <= 20;i++){
			t = new Thread(new SemaphoreTest(), "T" + i);
			t.start();
		}
	}
}

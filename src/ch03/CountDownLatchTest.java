package ch03;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * <p>Description: CountDownLatch倒计时器，可以控制当前线程等待其他线程都执行结束后再执行</p>
 * @author ZhangShenao
 * @date 2017年3月29日
 */
public class CountDownLatchTest implements Runnable{
	private static final CountDownLatchTest task = new CountDownLatchTest();
	
	//创建CountDownLatch对象，指定线程计数的数量
	private static final CountDownLatch countDownLatch = new CountDownLatch(10);
	
	private static final Random random = new Random();
	
	@Override
	public void run() {
		try {
			//模拟耗时操作
			Thread.sleep(random.nextInt(5) * 1000);
			System.out.println(Thread.currentThread().getName() + " complete!!");
			
			//线程执行完毕，倒计时器计数-1
			countDownLatch.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
		
		for (int i = 1;i < 11;i++){
			pool.submit(task);
		}
		
		//等待倒计时器计数完毕，当前线程再执行
		countDownLatch.await();
		
		System.out.println("All complete!!");
		
		pool.shutdown();
	}

}

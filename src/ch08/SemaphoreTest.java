package ch08;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import ch04.SleepUtil;

/**
 * 
 * <p>Description:信号量,用来控制同时访问特定资源的线程数量</p>
 * @author ZhangShenao
 * @date 2017年6月7日
 */
public class SemaphoreTest {
	/**
	 * 线程数
	 */
	private static final int THREAD_NUM = 30;
	
	/**
	 * 线程池
	 */
	private static final ExecutorService pool = Executors.newFixedThreadPool(THREAD_NUM);
	
	/**
	 * 信号量
	 */
	private static final Semaphore semaphore = new Semaphore(10);
	
	public static void main(String[] args) {
		for (int i = 0;i < THREAD_NUM;i++){
			pool.execute(new Runnable(){

				@Override
				public void run() {
					try {
						//获取信号量
						semaphore.acquire();
						System.out.println(Thread.currentThread().getName());
						SleepUtil.sleep(2);
					}catch (Exception e){
						e.printStackTrace();
					}finally {
						//释放信号量
						semaphore.release();
					}
				}
				
			});
		}
		
		pool.shutdown();
	}
}

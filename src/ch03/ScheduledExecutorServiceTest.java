package ch03;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * <p>Description: ScheduledExecutorService,可调度的线程池，可以控制任务按照指定的周期或者延迟执行</p>
 * @author ZhangShenao
 * @date 2017年3月30日
 */
public class ScheduledExecutorServiceTest {
	public static void main(String[] args) throws InterruptedException {
		//创建一个ScheduledExecutorService
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2);
		
		//线程执行周期为2s
		//如果线程的执行时间超过执行周期，则下一个线程会在上一个线程执行完毕后立即执行
		//如果为scheduleWithFixedDelay方法，则两个线程的时间间隔为线程的执行时间+delay
		pool.scheduleAtFixedRate(new Runnable(){

			@Override
			public void run() {
				System.out.println(System.currentTimeMillis() / 1000 + ": " + Thread.currentThread().getId());
				
				//每个线程的执行时间为1s
				try {
					Thread.sleep(8000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}, 0L, 2000L, TimeUnit.MILLISECONDS);
		
		//如果某个线程抛出异常,则后续所有子任务都会停止调度,因此必须做好异常处理
		
		//为了演示线程调度,这里不关闭线程池
		/*pool.awaitTermination(5000, TimeUnit.MILLISECONDS);
		pool.shutdown();*/
		
	}
}

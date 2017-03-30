package ch03;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * <p>Description: 使用JDK提供的线程池ExecutorService</p>
 * @author ZhangShenao
 * @date 2017年3月30日
 */
public class ThreadPoolTest {
	private static class MyTask implements Runnable{
		private String name;
		
		public MyTask(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			System.out.println(System.currentTimeMillis() + "-->" + "Task Name: " + name);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		//开启线程数固定的线程池
		int threadNum = Runtime.getRuntime().availableProcessors() * 2;
		System.err.println("Thread Num: " + threadNum);
		ExecutorService pool = Executors.newFixedThreadPool(threadNum);
		
		//将任务提交到线程池中执行
		for (int i = 0;i < 10;i++){
			//使用固定大小n的线程池，可以同时执行n个线程
			pool.submit(new MyTask("Task" + (i + 1)));
			
		}
		
		//等待线程池中的任务执行完毕
		pool.awaitTermination(5, TimeUnit.SECONDS);
		
		//关闭线程池
		pool.shutdown();
	}
}

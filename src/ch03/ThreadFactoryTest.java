package ch03;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * <p>Description: 实现ThreadFactory接口,可以自定义创建线程的逻辑</p>
 * @author ZhangShenao
 * @date 2017年3月30日
 */
public class ThreadFactoryTest {
	//任务线程
	private static class MyTask implements Runnable{
		private String name;
		
		public MyTask(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(200);
				System.out.println("task: " + name + " is complete!!");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		//创建自定义的线程池，实现自定义创建线程的方法和拒绝策略
		ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10), 
				new ThreadFactory() {
					@Override
					public Thread newThread(Runnable r) {
						//自定义创建线程
						Thread t = new Thread(r);
						t.setDaemon(true);
						System.out.println("Create Thread: " + t.getName());
						return t;
					}
				}, new RejectedExecutionHandler() {
					@Override
					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
						//打印被拒绝的线程的日志
						System.err.println(r + ": is rejected");
					}
				});
		
		//向线程池中提交线程
		for (int i = 1;i < 31;i++){
			pool.submit(new MyTask("Task" + i));
			Thread.sleep(10);
		}
		
		pool.awaitTermination(2000L, TimeUnit.MILLISECONDS);
		pool.shutdown();
	}
}

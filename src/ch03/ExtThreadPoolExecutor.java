package ch03;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * <p>Description: 扩展ThreadPoolExecutor,可在线程执行前、执行后和线程池关闭后自定义逻辑</p>
 * @author ZhangShenao
 * @date 2017年3月30日
 */
public class ExtThreadPoolExecutor {
	//任务线程
	private static class MyTask implements Runnable{
		private String name;
		
		public MyTask(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}



		@Override
		public void run() {
			try {
				Thread.sleep(200);
				System.out.println(name + ": 正在执行!!");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//扩展ThreadPoolExecutor
	private static class MyThreadPoolExecutor extends ThreadPoolExecutor{
		//自定义构造器
		public MyThreadPoolExecutor() {
			super(5, 10, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10),new RejectedExecutionHandler() {
				@Override
				public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
					//打印被拒绝的线程的日志
					System.err.println(r + ": is rejected");
				}
			});
		}
		
		//线程执行前调用
		protected void beforeExecute(Thread t, Runnable r) {
			System.out.println(r + ": 准备执行!!");
		}
		
		//线程执行完成后调用
		protected void afterExecute(Runnable r, Throwable t) {
			System.out.println(r + ": 执行完毕!!");
		}
		
		//线程池关闭后调用
		protected void terminated() {
			System.out.println("线程池关闭!!");
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		//创建扩展的线程池
		MyThreadPoolExecutor pool = new MyThreadPoolExecutor();
		
		//向线程池中提交线程
		for (int i = 1;i < 10;i++){
			pool.submit(new MyTask("Task" + i));
			Thread.sleep(10);
		}
		
//		pool.awaitTermination(2000L, TimeUnit.MILLISECONDS);
		//关闭线程池
		//该方法并不会暴力终止所有任务,而是等待所有任务执行完毕后再关闭线程池
		pool.shutdown();
	}
}

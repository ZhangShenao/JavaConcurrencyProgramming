package ch03;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * <p>Description: RejectedExecutionHandler,线程池的拒绝策略</p>
 * @author ZhangShenao
 * @date 2017年3月30日
 */
public class RejectThreadTest {
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
		//创建自定义的线程池,并自定义拒绝策略
		ThreadPoolExecutor pool = new ThreadPoolExecutor(5,10,0L,TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10)
				, new RejectedExecutionHandler() {
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

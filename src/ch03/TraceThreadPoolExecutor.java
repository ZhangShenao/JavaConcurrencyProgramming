package ch03;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * <p>Description: 通过ThreadPoolExecutor查看线程堆栈信息</p>
 * @author ZhangShenao
 * @date 2017年3月30日
 */
public class TraceThreadPoolExecutor {
	//计算两个数除法的线程
	private static class DivTask implements Runnable{
		private int a;
		private int b;
		
		public DivTask(int a, int b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public void run() {
			System.out.println("result: " + (a / b));
		}
		
	}
	
	//自定义线程池,可以打印线程的堆栈
	private static class ShowTraceThreadPoolExecutor extends ThreadPoolExecutor{

		public ShowTraceThreadPoolExecutor(int corePoolSize,
				int maximumPoolSize, long keepAliveTime, TimeUnit unit,
				BlockingQueue<Runnable> workQueue) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		}
		
		private Exception clientTrace(){
			return new Exception("Client Stack Trace");
		}
		
		/**
		 * 将Runnable对象进行包装，可以获取打印异常信息
		 */
		private Runnable wrap(final Runnable task,final Exception clientStack){
			return new Runnable() {
				@Override
				public void run() {
					try {
						task.run();
					}catch (Exception e){
						clientStack.printStackTrace();
						throw new RuntimeException(e);
					}
					
				}
			};
		}
		
		@Override
		public void execute(Runnable command) {
			super.execute(wrap(command, clientTrace()));
		}
		
		@Override
		public Future<?> submit(Runnable task) {
			return super.submit(wrap(task, clientTrace()));
		}
		
	}
	
	public static void main(String[] args) {
		//创建线程池
		ShowTraceThreadPoolExecutor pool = new ShowTraceThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10));
		
		//执行任务
		for (int i = 0;i < 5;i++){
			pool.execute(new DivTask(100, i));
		}
		
		//关闭线程池
		pool.shutdown();
	}
}

package ch05.jdkfuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 
 * <p>Description:使用JDK的Future模式</p>
 * @author ZhangShenao
 * @date 2017年4月5日
 */
public class FutureMain {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		/**
		 * 创建FutureTask对象，并指定Callable任务
		 * FutureTask即实现了Runnable接口，又实现了Future接口，可以提交到线程池中执行，也可以获取任务的返回结果
		 */
		FutureTask<String> task = new FutureTask<String>(new RealData("name"));
		
		//创建线程池
		ExecutorService pool = Executors.newCachedThreadPool();
		
		//将FutureTask交给线程池执行
		pool.submit(task);
		
		System.out.println("请求完毕");
		
		//模拟其他操作
		Thread.sleep(2000);
		System.out.println("执行其他操作");
		
		//获取实际的结果
		System.out.println("真实的结果:" + task.get());
		
		//关闭线程池
		pool.shutdown();
	}
}

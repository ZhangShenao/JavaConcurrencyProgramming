package ch09;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * <p>Description:监控器线程池使用ThreadPoolExecutor实现自定义的线程池,监控线程运行状态</p>
 * @author ZhangShenao
 * @date 2017年6月8日
 */
public class MonitorThreadPool extends ThreadPoolExecutor{
	
	/**
	 * 创建监控器线程池
	 * 核心线程数:10
	 * 最大线程数:20
	 * 空闲线程最大存活时间:5s
	 * 任务队列容量:50
	 */
	public MonitorThreadPool() {
		super(10, 20, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(50));
	}
	
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		System.out.println("线程:" + t.getName() + "即将执行");
	}
	
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		System.out.println("线程:" + Thread.currentThread().getName() + "执行结束");
	}
	
	@Override
	protected void terminated() {
		super.terminated();
		System.out.println("所有线程执行完毕");
		System.out.println("需要执行的任务数量: " + getTaskCount());
		System.out.println("已完成的任务数量: " + getCompletedTaskCount());
		System.out.println("曾经创建过的最大线程数量: " + getLargestPoolSize());
		System.out.println("线程池内的线程数量: " + getPoolSize());
		System.out.println("活动的线程数: " + getActiveCount());
	}
	
	public static void main(String[] args) {
		MonitorThreadPool pool = new MonitorThreadPool();
		for (int i = 1;i <= 30;i++){
			pool.execute(new Runnable(){
				@Override
				public void run() {
					System.out.println("线程:" + Thread.currentThread().getName() + "执行结束");
				}
			});
		}
		
		pool.shutdown();
	}

}

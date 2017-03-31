package ch03;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * <p>Description: ThreadLocal.ThreadLocalMap采用了类似WeakHashMap的实现，当key的引用为null时，会自动回收该key-value</p>
 * @author ZhangShenao
 * @date 2017年3月31日
 */
public class ThreadLocalGCTest {
	private static CountDownLatch latch = new CountDownLatch(10);
	
	private static ThreadLocal<SimpleDateFormat> localFormat = new ThreadLocal<SimpleDateFormat>(){
		protected void finalize() throws Throwable {
			System.err.println(this + " is GC!!");
		};
	};
	
	//打印日期的线程
	private static class PrintDateTask implements Runnable{
		//日期的秒数
		private int second;
		
		public PrintDateTask(int second) {
			this.second = second;
		}

		@Override
		public void run() {
			try {
				//从ThreadLocal容器里面获取对象,如果没有就创建新的
				SimpleDateFormat format = localFormat.get();
				if (null == format){
					format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"){
						protected void finalize() throws Throwable {
							System.err.println(this + " is GC!!");
						};
					};
					localFormat.set(format);
					System.out.println("创建了SimpleDateFormat: " + format);
				}
				
				Date date = format.parse("2017-03-31 18:14:" + second);
				System.out.println("Date: " + date);
			}catch (Exception e){
				e.printStackTrace();
			}finally {
				latch.countDown();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		//创建线程池
		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
		
		//第一次执行任务
		for (int i = 0;i < 10;i++){
			pool.submit(new PrintDateTask(i));
		}
		latch.await();
		System.err.println("First Mission Completed!!");
		
		//执行第一次GC
		localFormat = null;
		System.gc();
		System.err.println("First GC Completed!!");
		
		//创建新的ThreadLocal实例,会向ThreadLocalMap中插入数据,此时ThreadLocalMap可能会将无效的对象清除
		localFormat = new ThreadLocal<SimpleDateFormat>(){
			protected void finalize() throws Throwable {
				System.err.println(this + " is GC!!");
			};
		};
		
		latch = new CountDownLatch(10);
		//第二次执行任务
		for (int i = 0;i < 10;i++){
			pool.submit(new PrintDateTask(i));
		}
		latch.await();
		System.err.println("Second Mission Completed!!");
		
		//执行第二次GC
		localFormat = null;
		System.gc();
		System.err.println("Second GC Completed!!");
		
		pool.shutdown();
	}
}

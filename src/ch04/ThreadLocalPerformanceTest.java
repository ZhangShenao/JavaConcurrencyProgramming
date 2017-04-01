package ch04;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * <p>Description: ThreadLocal的性能测试,比较多线程共享变量和使用ThreadLocal变量的执行时间</p>
 * @author ZhangShenao
 * @date 2017年3月31日
 */
public class ThreadLocalPerformanceTest {
	/**
	 * 开启的线程数
	 */
	private static final int THREAD_NUM = Runtime.getRuntime().availableProcessors() * 2;
	
	/**
	 * 线程池
	 */
	private static final ExecutorService pool = Executors.newFixedThreadPool(THREAD_NUM);
	
	/**
	 * 每个线程生成随机数的个数
	 */
	private static final int RANDOM_NUM = 10000000;
	
	/**
	 * 被多线程共享的Random
	 */
	private static final Random sharedRandom = new Random(123);
	
	/**
	 * 使用ThreadLocal,为每个线程分配一个独立的Random
	 */
	private static final ThreadLocal<Random> localRandom = new ThreadLocal<Random>(){
		protected Random initialValue() {
			return new Random(123);
		};
	};
	
	//产生随机数的任务
	private static class CreateRandomTask implements Callable<Long>{
		/**
		 * 产生随机数的策略
		 * 0:使用多线程共享的Random
		 * 1:使用ThreadLocal<Random>
		 */
		private int type = 0;
		
		public CreateRandomTask(int type) {
			this.type = type;
		}


		@Override
		public Long call() throws Exception {
			Long startTime = System.currentTimeMillis();
			
			//生成指定数量的随机数
			Random random = getRandom();
			for (int i = 0;i < RANDOM_NUM;i++){
				random.nextInt();
			}
			Long endTime = System.currentTimeMillis();
			
			//返回每个线程的执行时间
			return (endTime - startTime);
		}
		
		/**
		 * 根据不同的策略,获取不同的Random对象
		 */
		private Random getRandom(){
			if (type == 0){
				return sharedRandom;
			}
			if (type == 1){
				return localRandom.get();
			}
			return null;
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		ArrayList<Future<Long>> futures = new ArrayList<Future<Long>>(THREAD_NUM);
		
		//使用多线程共享的方式
		long totalTime = 0L;
		for (int i = 0;i < THREAD_NUM;i++){
			futures.add(pool.submit(new CreateRandomTask(0)));
		}
		for (Future<Long> future : futures) {
			totalTime += future.get();
		}
		System.err.println("使用多线程变量共享的方式,执行时间: " + totalTime);
		
		//ThreadLocal的方式
		futures.clear();
		totalTime = 0L;
		for (int i = 0;i < THREAD_NUM;i++){
			futures.add(pool.submit(new CreateRandomTask(1)));
		}
		for (Future<Long> future : futures) {
			totalTime += future.get();
		}
		System.err.println("使用ThreadLocal的方式的方式,执行时间: " + totalTime);
		
		pool.shutdown();
	}
}

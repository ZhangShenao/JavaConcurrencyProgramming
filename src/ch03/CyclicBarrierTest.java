package ch03;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 * 
 * <p>Description: CyclicBarrier循环栅栏，比CountDownLatch功能更强大，当计数器归零后可反复使用，且可在倒计时完成后执行指定任务</p>
 * @author ZhangShenao
 * @date 2017年3月29日
 */
public class CyclicBarrierTest {
	//士兵线程，可完成集合和执行任务的操作
	private static class Soldier implements Runnable{
		private CyclicBarrier cyclicBarrier;
		private String name;
		
		private static final Random random = new Random();
		
		public Soldier(CyclicBarrier cyclicBarrier, String name) {
			this.cyclicBarrier = cyclicBarrier;
			this.name = name;
		}

		@Override
		public void run() {
			//等待所有士兵集合完毕
			try {
				cyclicBarrier.await();
			
				//执行任务
				doWork();
				
				//等待所有士兵完成任务
				//当CyclicBarrier的计数器清零后，可再次使用
				cyclicBarrier.await();
				
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		
		/**
		 * 士兵执行任务的方法
		 */
		private void doWork() throws InterruptedException {
			//模拟耗时操作
			Thread.sleep(1000 * random.nextInt(10));
			
			System.out.println("士兵" + name + ":执行任务完毕");
		}
	}
	
	//当CyclicBarrier倒计时结束后，要执行的程序
	private static class BarrierRun implements Runnable{
		private final int N;
		
		//flag为false,表示集合完毕;flag为true,表示任务执行完毕
		private boolean flag;
		
		public BarrierRun(final int N, boolean flag) {
			this.N = N;
			this.flag = flag;
		}

		@Override
		public void run() {
			if (flag){
				System.out.println(N + "个士兵执行任务完毕!!");
			}
			else {
				System.out.println(N + "个士兵集合完毕!!");
				flag = true;
			}
		}
	}
	
	public static void main(String[] args) {
		//创建CyclicBarrier循环栅栏，指定计数的线程数和倒计时结束后要结束的任务
		final int N = 10;
		CyclicBarrier cyclicBarrier = new CyclicBarrier(N, new BarrierRun(N, false));
		
		//如果采用线程池的方式执行线程，则CyclicBarrier无法生效
		//ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
		
		System.out.println("队伍开始集合!!");
		
		Thread t = null;
		//创建士兵线程
		for (int i = 1;i < 21;i++){
			String name = "士兵" + i;
			System.out.println(name + "报到");
			t = new Thread(new Soldier(cyclicBarrier, "士兵" + i));
			t.start();
		}
	}
}

package ch04;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 
 * <p>Description:  线程优先级</p>
 * <p>程序的正确性不能依赖于线程优先级高低,因为操作系统可以完全不用理会Java线程对应优先级的设定</p>
 * @author ZhangShenao
 * @date 2017年6月1日 上午7:19:42
 */
public class PriorityTest {
	private static volatile boolean notStart = true;
	private static volatile boolean notEnd = true;
	
	private static class Job implements Runnable{
		private int priority;
		private long time;
		
		public Job(int priority) {
			this.priority = priority;
		}

		@Override
		public void run() {
			while (notStart){
				Thread.yield();
			}
			
			while (notEnd){
				Thread.yield();
				time = System.currentTimeMillis();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		List<Job> jobs = new ArrayList<>();
		
		Job job = null;
		Thread t = null;
		for (int i = 1;i <= 10;i++){
			int priority = i < 6 ? Thread.MIN_PRIORITY : Thread.MAX_PRIORITY;
			job = new Job(priority);
			jobs.add(job);
			t = new Thread(job, "T" + i);
			t.setPriority(priority);
			t.start();
		}
		
		notStart = false;
		TimeUnit.SECONDS.sleep(10);
		notEnd = false;
		
		for (Job j : jobs){
			System.out.println("Job Priority: " + j.priority + " , Time: " + j.time);
		}
	}
}

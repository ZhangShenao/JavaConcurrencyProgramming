package ch04;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 
 * <p>Description: AtomicIntegerFieldUpdater可以将普通类型的变量包装成原子变量，进行CAS操作，保证线程安全 </p>
 * @author ZhangShenao
 * @date 2017年4月5日 上午7:42:37
 */
public class AtomicIntegerFieldUpdaterTest {
	//候选人类
	public static class Candidate{
		//候选人id
		int id;
		
		//候选人总得票数
		volatile int score;
		
	}
	
	/**
	 * 获取Candidate类socre字段的原子类包装
	 * AtomicIntegerFieldUpdater只能修改可见范围内的变量，因为内部使用反射，用private修饰的变量无法进行包装
	 * 为了确保变量正确的读取，必须用volatile修饰
	 * AtomicIntegerFieldUpdater不支持static类型的变量
	 */
	private static final AtomicIntegerFieldUpdater<Candidate> scoreUpdater =
			AtomicIntegerFieldUpdater.newUpdater(Candidate.class, "score");
	
	/**
	 * 用于检测AtomicIntegerFieldUpdater是否工作正确
	 */
	private static final AtomicInteger allScore = new AtomicInteger(0);
	
	public static void main(String[] args) throws InterruptedException {
		final Candidate candidate = new Candidate();
		Thread[] threads = new Thread[1000];
		final Random random = new Random();
		
		for (int i = 0;i < 1000;i++){
			threads[i] = new Thread(){
				@Override
				public void run() {
					if (random.nextInt(10) > 4){
						scoreUpdater.incrementAndGet(candidate);
						allScore.incrementAndGet();
					}
				}
			};
			threads[i].start();
		}
		
		for (int i = 0;i < 1000;i++){
			threads[i].join();
		}
		
		//candidate.score与allScore值完全相等，说明AtomicIntegerFieldUpdater很好地保证了Candidate.score的线程安全
		System.out.println("candidate score: " + candidate.score);
		System.out.println("allScore: " + allScore.get());
	}
}

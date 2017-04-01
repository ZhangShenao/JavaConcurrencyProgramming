package ch04;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 
 * <p>AtomicStampedReference,带有时间戳的原子对象引用,内部维护了一个时间戳，修改引用变量时还要修改时间戳</p>
 * @author ZhangShenao
 * @date 2017年4月1日
 */
public class AtomicStampedReferenceTest {
	/**
	 * 账户
	 * 使用带有时间戳的原子对象引用，引用初始值为19，时间戳初始值为0
	 */
	private static AtomicStampedReference<Integer> account = new AtomicStampedReference<Integer>(19,0);
	
	//负责充值的线程
	private static class ChargeTask implements Runnable{

		@Override
		public void run() {
			try {
				//如果账户余额小于20,则赠送20，但要求一个账户只能赠送一次
				while (true){
					//获取当前引用的时间戳
					int stamp = account.getStamp();
					Integer money = account.getReference();
					
					if (money < 20){
						//修该引用变量的值，并修改时间戳
						account.compareAndSet(money,money + 20,stamp,stamp + 1);
						System.out.println("账户余额不足20，赠送20元");
					}
					else{
						System.out.println("账户余额超过20，没有赠送活动");
					}
					Thread.sleep(200);
				}
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		
	}
	
	//账户消费的线程
	private static class ConsumerTask implements Runnable{

		@Override
		public void run() {
			try {
				for (int i = 0;i < 5;i++){
					//如果账户余额不小于10元，则消费10元
					while (true){
						int stamp = account.getStamp();
						Integer money = account.getReference();
						if (money >= 20){
							if (account.compareAndSet(money, money - 10,stamp,stamp + 1)){
								System.out.println("账户余额不小于10元，消费10元");
							}
						}
						else{
							System.out.println("账户余额不足，无法消费");
						}
						Thread.sleep(200);
					}
				}
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for (int i = 0;i < 3;i++){
			pool.submit(new ChargeTask());
		}
		
		pool.submit(new ConsumerTask());
		pool.shutdown();
	}
	
}

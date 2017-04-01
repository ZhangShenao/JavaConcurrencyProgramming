package ch04;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 
 * <p>Description:AtomicReference,线程安全的引用类型,在修改对象引用时可以保证操作的原子性
 * 		但是有一个问题，如果变量经过多次修改被改回原值，则其他线程无法判断该变量是否曾经被修改
 * </p>
 * @author ZhangShenao
 * @date 2017年4月1日
 */
public class AtomicReferenceTest {
	/**
	 * 账户,初始值为19
	 */
	private static AtomicReference<Integer> account = new AtomicReference<Integer>(19);
	
	//负责充值的线程
	private static class ChargeTask implements Runnable{

		@Override
		public void run() {
			try {
				while (true){
					//如果账户余额小于20,则赠送20，但要求一个账户只能赠送一次
					Integer money = account.get();
					if (money < 20){
						account.compareAndSet(money, money + 20);
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
					while (true){
						//如果账户余额不小于10元，则消费10元
						Integer money = account.get();
						if (money >= 20){
							account.compareAndSet(money, money - 10);
							System.out.println("账户余额不小于10元，消费10元");
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
		
		//可能出现这样的结果:赠送一次后,由于进行了消费，又可以赠送，即变量经过多次修改回到原值，但其他线程无法辨别是否修改过
		pool.submit(new ConsumerTask());
		pool.shutdown();
	}
	
}

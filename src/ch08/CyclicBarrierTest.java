package ch08;

import java.util.concurrent.CyclicBarrier;

/**
 * 
 * <p>Description:CylicBarrier同步屏障,等待所有线程都到达一个屏障(同步点),然后共同执行</p>
 * @author ZhangShenao
 * @date 2017年6月7日
 */
public class CyclicBarrierTest {
	public static void main(String[] args) {
		//创建同步屏障,在所有线程到达屏障后,优先执行barrierAction
		final CyclicBarrier cyclicBarrier = new CyclicBarrier(2,new Runnable() {
			
			@Override
			public void run() {
				System.out.println("3");
			}
		});
		
		Thread t = new Thread(){
			public void run() {
				try {
					cyclicBarrier.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		};
		
		try {
			t.start();
			t.interrupt();
			//所有线程阻塞在屏障处
			cyclicBarrier.await();
		}catch (Exception e){
			
		}
		
		//查看阻塞的线程是否被中断
		System.out.println(cyclicBarrier.isBroken());
	}
}

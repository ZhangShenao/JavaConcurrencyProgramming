package ch08;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * <p>Description:Exchanger,线程间协作的工具类,用于进行线程间的数据交换</p>
 * @author ZhangShenao
 * @date 2017年6月7日
 */
public class ExchangerTest {
	private static final Exchanger<String> exchenger = new Exchanger<>();
	
	/**
	 * 线程池
	 */
	private static final ExecutorService pool = Executors.newFixedThreadPool(2);
	
	public static void main(String[] args) {
		//A录入银行流水数据
		pool.execute(new Runnable(){
			@Override
			public void run() {
				String strA = "银行流水A";
				
				try {
					//等待交换数据
					exchenger.exchange(strA);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});
		
		//B录入银行流水数据
		pool.execute(new Runnable(){
			@Override
			public void run() {
				String strB = "银行流水B";
				try {
					//等待交换数据
					String strA = exchenger.exchange(strB);
					
					System.out.println("A银行录入的数据: " + strA);
					System.out.println("B银行录入的数据: " + strB);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});
		
		pool.shutdown();
	}
}

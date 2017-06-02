package ch04;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * <p>Description:测试数据库连接池</p>
 * @author ZhangShenao
 * @date 2017年6月2日
 */
public class ConnectionPoolTest {
	/**
	 * 连接池,默认有10个连接
	 */
	private static ConnectionPool pool = new ConnectionPool(10);
	
	/**
	 * 每个线程需要获取的连接数
	 */
	private static final int CONNECTION_NUM = 20;
	
	/**
	 * 进行测试的总线程数
	 */
	private static final int THREAD_NUM = 40;
	
	/**
	 * 控制所有线程同时开始的倒计时器
	 */
	private static final CountDownLatch START_TOGETHER = new CountDownLatch(1);
	
	/**
	 * 等待所有线程结束的倒计时器
	 */
	private static final CountDownLatch WAIT_FINISH = new CountDownLatch(THREAD_NUM);
	
	public static void main(String[] args) throws InterruptedException {
		ConnectionRunner runner = null;
		AtomicInteger totalGotNum = new AtomicInteger(0);
		AtomicInteger totalNotGotNum = new AtomicInteger(0);
		
		for (int i = 0;i < THREAD_NUM;i++){
			runner = new ConnectionRunner(totalGotNum,totalNotGotNum);
			new Thread(runner, "Thread-" + (i + 1)).start();
		}
		
		//控制所有线程同时启动
		START_TOGETHER.countDown();
		
		//等待所有线程结束
		WAIT_FINISH.await();
		
		//统计
		System.out.println("总共获取的次数: " + CONNECTION_NUM * THREAD_NUM);
		System.out.println("获取成功的次数: " + totalGotNum.get());
		System.out.println("获取失败的次数: " + totalGotNum.get());
	}
	
	/**
	 * 
	 * <p>Description: 测试从连接池中获取连接的线程</p>
	 * @author ZhangShenao
	 * @date 2017年6月2日
	 */
	private static class ConnectionRunner implements Runnable{
		/**
		 * 成功获取的连接数
		 */
		private AtomicInteger gotNum;
		
		/**
		 * 未成功获取的连接数
		 */
		private AtomicInteger notGotNum;
		
		public ConnectionRunner(AtomicInteger gotNum, AtomicInteger notGotNum) {
			this.gotNum = gotNum;
			this.notGotNum = notGotNum;
		}


		@Override
		public void run() {
			Connection conn = null;
			int i = 0;
			while (i < CONNECTION_NUM){
				try {
					START_TOGETHER.await();
					
					//尝试从连接池中获取连接,超时时间500ms
					conn = pool.fetchConenction(1000);
					
					if (null != conn){
						try {
							conn.createStatement();
							conn.commit();
						}finally {
							//释放连接
							pool.releaseConnection(conn);
							gotNum.incrementAndGet();
						}
					}
					else {
						notGotNum.incrementAndGet();
					}
				}catch (Exception e){
					e.printStackTrace();
					
				}finally {
					i++;
				}
			}
			WAIT_FINISH.countDown();
		}
	}
}

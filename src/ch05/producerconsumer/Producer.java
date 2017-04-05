package ch05.producerconsumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * <p>Description: 生产者线程</p>
 * @author ZhangShenao
 * @date 2017年4月5日
 */
public class Producer implements Runnable{
	/**
	 * 生产者和消费者共享的阻塞队列
	 */
	private BlockingQueue<PCData> queue;
	
	/**
	 * 线程睡眠时间
	 */
	private static final int SLEEP_TIME = 1000;
	
	/**
	 * 计数值
	 */
	private static AtomicInteger count = new AtomicInteger(0);
	
	private static final Random random = new Random();
	
	private volatile boolean isRunning = true;
	
	public Producer(BlockingQueue<PCData> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			PCData pcData = null;
			while (isRunning){
				int intData = count.incrementAndGet();
				pcData = new PCData(intData);
				
				//将数据放入阻塞队列
				queue.put(pcData);
				System.err.println("数据:" + pcData + " 被放入阻塞队列中");
				Thread.sleep(random.nextInt(SLEEP_TIME));
			}
		}catch (Exception e){
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * 停止当前生产者
	 */
	public void stop(){
		this.isRunning = false;
	}

}

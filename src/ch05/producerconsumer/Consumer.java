package ch05.producerconsumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable{
	/**
	 * 生产者和消费者共享的阻塞队列
	 */
	private BlockingQueue<PCData> queue;
	
	/**
	 * 线程睡眠时间
	 */
	private static final int SLEEP_TIME = 1000;
	
	private static final Random random = new Random();
	
	private volatile boolean isRunning = true;

	public Consumer(BlockingQueue<PCData> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			while (isRunning){
				//从阻塞队列中获取数据
				PCData pcData = queue.poll();
				if (null != pcData){
					System.err.println("从阻塞队列中获取数据: " + pcData);
					//计算结果
					int intData = pcData.getIntData();
					System.out.println(intData + " * " + intData + " = " + intData * intData);
					
					Thread.sleep(random.nextInt(SLEEP_TIME));
				}
				
			}
		}catch (Exception e){
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * 停止当前消费者
	 */
	public void stop(){
		this.isRunning = false;
	}
	
}

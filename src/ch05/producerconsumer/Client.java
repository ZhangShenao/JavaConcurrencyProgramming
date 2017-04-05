package ch05.producerconsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * <p>Description: 测试基于阻塞队列实现的生产者消费者模式</p>
 * @author ZhangShenao
 * @date 2017年4月5日
 */
public class Client {
	public static void main(String[] args) throws InterruptedException {
		//创建共享的缓冲区(阻塞队列)
		BlockingQueue<PCData> queue = new ArrayBlockingQueue<PCData>(10);
		
		//创建线程池
		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
		
		//创建生产者
		Producer p1 = new Producer(queue);
		Producer p2 = new Producer(queue);
		Producer p3 = new Producer(queue);
		
		//创建消费者
		Consumer c1 = new Consumer(queue);
		Consumer c2 = new Consumer(queue);
		Consumer c3 = new Consumer(queue);
		
		//开启生产者和消费者
		pool.execute(p1);
		pool.execute(p2);
		pool.execute(p3);
		pool.execute(c1);
		pool.execute(c2);
		pool.execute(c3);
		
		Thread.sleep(5000);
		
		//停止生产者
		p1.stop();
		p2.stop();
		p3.stop();
		Thread.sleep(5000);
		
		//停止消费者
		c1.stop();
		c2.stop();
		c3.stop();
		Thread.sleep(5000);
		
		//关闭线程池
		pool.shutdown();
		
	}
	
}

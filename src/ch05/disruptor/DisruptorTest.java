package ch05.disruptor;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 
 * <p>Description:  使用Disruptor无锁缓存框架，实现高性能的生产者-消费者模型</p>
 * @author ZhangShenao
 * @date 2017年4月5日 下午11:14:03
 */
public class DisruptorTest {
	public static void main(String[] args) throws InterruptedException {
		//创建线程池
		ExecutorService pool = Executors.newCachedThreadPool();
		
		//创建数据工厂
		PCDataFactory factory = new PCDataFactory();
		
		//指定环形缓冲区的大小，必须为2的整数次方
		int ringBufferSize = 1024;
		
		//创建Disruptor对象
		Disruptor<PCData> disruptor = new Disruptor<PCData>(factory, ringBufferSize, pool, 
				ProducerType.MULTI, new BlockingWaitStrategy());
		
		//设置4个消费者实例，系统会将每个消费者实例映射到一个线程中
		disruptor.handleEventsWithWorkerPool(new Consumer(),new Consumer(),new Consumer(),new Consumer());
		
		//启动并初始化Disruptor系统
		disruptor.start();
		
		//创建环形缓冲区
		RingBuffer<PCData> ringBuffer = disruptor.getRingBuffer();
		
		//创建一个生产者
		Producer producer = new Producer(ringBuffer);
		ByteBuffer byteBuf = ByteBuffer.allocate(8);
		
		//由生产者向缓冲区中写入数据
		for (long i = 0;;i++){
			byteBuf.putLong(0, i);
			producer.pushData(byteBuf);
			Thread.sleep(100);
			System.out.println("add data :" + i);
		}
		//关闭线程池
//		pool.shutdown();
	}
}

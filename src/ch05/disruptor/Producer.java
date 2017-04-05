package ch05.disruptor;

import java.nio.ByteBuffer;

import com.lmax.disruptor.RingBuffer;

/**
 * 
 * <p>Description:  生产者</p>
 * @author ZhangShenao
 * @date 2017年4月5日 下午11:05:37
 */
public class Producer {
	/**
	 * 内部维护一个RingBuffer对象,也就是环形缓冲区
	 */
	private final RingBuffer<PCData> ringBuffer;
	
	/**
	 * 在构造器中传入RingBuffer对象
	 */
	public Producer(RingBuffer<PCData> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}
	
	/**
	 * 将传入ButeBuffer中的数据提取出来，装载到环形缓冲区中
	 */
	public void pushData(ByteBuffer byteBuf){
		//获取环形缓冲区中下一个可用的序列号
		long sequence = ringBuffer.next();
		
		try {
			//通过序列号，获取环形缓冲区中下一个空闲的PCData
			PCData event = ringBuffer.get(sequence);
			
			//将PCData的数据设置为期望值
			event.setValue(byteBuf.getLong(0));
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//发布数据，只有在数据发布后，才能被消费者看到
			ringBuffer.publish(sequence);
			
		}
	}
	
}

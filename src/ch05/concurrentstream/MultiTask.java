package ch05.concurrentstream;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * <p>Description: 乘法流水线</p>
 * @author ZhangShenao
 * @date 2017年4月6日
 */
public class MultiTask implements Runnable{
	/**
	 * 乘法流水线的消息队列
	 */
	public static final BlockingQueue<Msg> queue = new LinkedBlockingQueue<Msg>();
	
	private volatile boolean isRun = true;
	
	@Override
	public void run() {
		try {
			while (isRun){
				//从消息队列中取出数据
				Msg msg = queue.take();
				
				//计算乘法
				msg.setI(msg.getI() + msg.getJ());
				
				//将处理好的消息发送给除法流水线
				DivTask.queue.put(msg);
			}
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void stop(){
		isRun = false;
	}
}

package ch05.concurrentstream;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * <p>Description: 加法流水线</p>
 * @author ZhangShenao
 * @date 2017年4月6日
 */
public class PlusTask implements Runnable{
	/**
	 * 加法流水线的消息队列
	 */
	public static final BlockingQueue<Msg> queue = new LinkedBlockingQueue<Msg>();
	
	private volatile boolean isRun = true;
	
	@Override
	public void run() {
		try {
			while (isRun){
				//从消息队列中取出数据
				Msg msg = queue.take();
				
				//计算加法
				msg.setJ(msg.getI() + msg.getJ());
				
				//将处理好的消息发送给乘法流水线
				MultiTask.queue.put(msg);
			}
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void stop(){
		isRun = false;
	}
}

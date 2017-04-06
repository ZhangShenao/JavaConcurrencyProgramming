package ch05.concurrentstream;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * <p>Description: 除法流水线</p>
 * @author ZhangShenao
 * @date 2017年4月6日
 */
public class DivTask implements Runnable{
	/**
	 * 除法流水线的消息队列
	 */
	public static final BlockingQueue<Msg> queue = new LinkedBlockingQueue<Msg>();
	
	private volatile boolean isRun = true;
	
	@Override
	public void run() {
		try {
			while (isRun){
				//从消息队列中取出数据
				Msg msg = queue.take();
				
				//计算除法
				msg.setJ(msg.getJ() / 2D);
				
				//打印最终结果
				System.err.println(msg.getExp() + " : " + msg.getI());
			}
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void stop(){
		isRun = false;
	}
}

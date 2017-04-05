package ch05.future;

/**
 * 
 * <p>Description: Future模式的Data类，对ReadData进行了包装，实现异步操作</p>
 * @author ZhangShenao
 * @date 2017年4月5日
 */
public class FutureData implements Data{
	/**
	 * 内部维护一个真实的RealData对象
	 */
	private RealData realData;
	
	/**
	 * 是否已完成注入的标志
	 */
	private volatile boolean isReady = false;
	
	/**
	 * 注入RealData实例
	 */
	public synchronized void setRealData(RealData realData) {
		//如果已经注入完毕，则直接返回
		if (isReady){
			return;
		}
		
		//注入RealData实例
		this.realData = realData;
		isReady = true;
		
		//唤醒等待的线程
		this.notifyAll();
		
	}

	@Override
	public synchronized String getResult() {
		//如果还没有注入，则线程等待
		while (!isReady){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//返回RealData实例的getResult方法
		return realData.getResult();
	}

}

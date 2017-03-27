package ch02;

/**
 * 
 * <p>Description:  使用synchronized关键字，保证线程安全，被synchronized限制的多个线程是串行执行的</p>
 * @author ZhangShenao
 * @date 2017年3月26日 下午9:00:32
 */
public class SyncTest implements Runnable{
	/**
	 * 对象锁
	 */
	private static final SyncTest instance = new SyncTest();
	
	private static int count = 0;
	
	/**
	 * 同步方法
	 */
	private synchronized void add(){
		count++;
	}
	
	@Override
	public void run() {
		for (int i = 0;i < 10000;i++){
			synchronized (instance) {
				count++;
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new SyncTest());
		Thread t2 = new Thread(new SyncTest());
		
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		
		System.out.println("count: " + count);
	}
}


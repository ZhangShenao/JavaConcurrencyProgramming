package ch02;

/**
 * 
 * <p>Description: 线程组 </p>
 * @author ZhangShenao
 * @date 2017年3月26日 下午8:15:58
 */
public class ThreadGroupTest implements Runnable{

	@Override
	public void run() {
		//获取线程组名和线程名
		String groupAndName = Thread.currentThread().getThreadGroup().getName() 
							+ "-->" + Thread.currentThread().getName();
		
		System.out.println("Name: " + groupAndName);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//创建一个线程组
		ThreadGroup group = new ThreadGroup("MyGroup");
		
		//创建两个线程，在构造器中指定线程组
		Thread t1 = new Thread(group, new ThreadGroupTest(), "T1");
		Thread t2 = new Thread(group, new ThreadGroupTest(), "T2");
		
		t1.start();
		t2.start();
		
		//查看线程组内活动的线程数，只是一个估计值
		System.out.println("active count: " + group.activeCount());
		
		//查看线程组中所有线程的信息
		group.list();
	}

}

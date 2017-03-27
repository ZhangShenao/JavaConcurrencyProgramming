package ch02;

/**
 * 
 * <p>Description: Daemon守护线程(后台线程) </p>
 * @author ZhangShenao
 * @date 2017年3月26日 下午8:27:16
 */
public class DaemonThreadTest extends Thread{
	public static void main(String[] args) throws InterruptedException {
		DaemonThreadTest t = new DaemonThreadTest();
		
		//将线程设置为daemon线程,必须在线程start之前设置
		t.setDaemon(true);
		
		t.start();
		
		Thread.sleep(2000);
	}
	
	@Override
	public void run() {
		int i = 0;
		while (true){
			System.out.println(i++);
		}
	}
}

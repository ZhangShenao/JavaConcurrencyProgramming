package ch02;

/**
 * 
 * Title: JoinTest
 * Description:  join方法，当前线程等待目标线程执行完毕再执行
 * @author ZhangShenao
 * @date 2017年3月26日 上午9:39:46
 */
public class JoinTest {
	private static volatile int count;
	
	public static class AddThread extends Thread{
		@Override
		public void run() {
			for (count = 0;count < 1000000;count++)	{
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		AddThread t = new AddThread();
		t.start();
		
		//调用线程(主线程)在当前线程对象(AddThread对象)上进行等待
		t.join();
		
		System.out.println(count);
	}
}

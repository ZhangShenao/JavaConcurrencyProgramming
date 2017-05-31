package ch01;

/**
 * 
 * <p>Description:由于存在线程的创建与销毁、资源调度和上下文切换等因素造成的开销,多线程的执行效率并不一定比单线程高</p>
 * @author ZhangShenao
 * @date 2017年5月24日
 */
public class ConcurrencyTest {
	private static final long COUNT = 1000000000L;
	
	/**
	 * 并发执行
	 */
	private static void concurrent() throws InterruptedException{
		long startTime = System.currentTimeMillis();
		
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				int a = 0;
				for (long i = 0;i < COUNT;i++){
					a += 5;
				}
				
			}
		});
		t.start();
		
		int b = 0;
		for (long i = 0;i < COUNT;i++){
			--b;
		}
		
		t.join();
		
		long time = System.currentTimeMillis() - startTime;
		System.out.println("并发程序耗时: " + time);
	}
	
	/**
	 * 串行执行
	 */
	private static void serial(){
		long startTime = System.currentTimeMillis();
		int a = 0;
		int b = 0;
		for (int i = 0;i < COUNT;i++){
			a += 5;
		}
		
		for (int i = 0;i < COUNT;i++){
			--b;
		}
		long time = System.currentTimeMillis() - startTime;
		System.out.println("串行程序耗时: " + time);
	}
	
	public static void main(String[] args) throws InterruptedException {
		concurrent();
		serial();
	}
}

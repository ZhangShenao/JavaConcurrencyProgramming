package ch03;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * <p>Description: ThreadLocal,线程局部变量，每一个线程都保存一份变量的副本，可以避免并发访问的线程安全问题</p>
 * @author ZhangShenao
 * @date 2017年3月31日
 */
public class ThreadLocalTest {
	/**
	 * 创建ThreadLocal容器,里面保存SimpleDateFormat
	 * 由于SimpleDateFormat是线程不安全的,因此每个线程保存一个副本，避免并发访问
	 * 所有的数据保存在ThreadLocal.ThreadLocalMap这个内部类中,它类似与一个WeakHashMap,
	 * 以当前ThreadLocal对象为key,value为存放的对象,当key的引用为null时,GC自动回收该key-value
	 */
	private static final ThreadLocal<SimpleDateFormat> localFormat = new ThreadLocal<SimpleDateFormat>();
	
	//打印日期的线程
	private static class PrintDateTask implements Runnable{
		//日期的秒数
		private int second;
		
		public PrintDateTask(int second) {
			this.second = second;
		}

		@Override
		public void run() {
			try {
				//从ThreadLocal容器里面获取对象,如果没有就创建新的
				SimpleDateFormat format = localFormat.get();
				if (null == format){
					format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					localFormat.set(format);
				}
				
				Date date = format.parse("2017-03-31 18:14:" + second);
				System.out.println("Date: " + date);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
		
		for (int i = 0;i < 10;i++){
			pool.submit(new PrintDateTask(i));
		}
		
		pool.shutdown();
	}
}

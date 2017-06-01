package ch04;

/**
 * 
 * <p>Description:该类用于统计方法的调用耗时,使用ThreadLocal实现</p>
 * @author ZhangShenao
 * @date 2017年6月1日
 */
public class Profier {
	//第一次调用ThreadLocal的get()方法时,会进行初始化。每个线程会调用一次
	private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<Long>(){
		protected Long initialValue() {
			return System.currentTimeMillis();
		};
	};
	
	/**
	 * 开始统计
	 */
	public static final void begin(){
		TIME_THREADLOCAL.set(System.currentTimeMillis());
	}
	
	/**
	 * 统计结束
	 * @return 返回方法调用的耗时
	 */
	public static final long end(){
		return System.currentTimeMillis() - TIME_THREADLOCAL.get();
	}
	
	public static void main(String[] args) {
		for (int i = 1;i < 11;i++){
			new Thread(new Runnable(){

				@Override
				public void run() {
					Profier.begin();
					SleepUtil.sleep(1);
					System.out.println(Thread.currentThread().getName() + " 耗时: " + Profier.end() + "ms");
				}
				
			},"Thread-" + i).start();
		}
	}
}

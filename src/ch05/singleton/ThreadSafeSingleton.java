package ch05.singleton;

/**
 * 
 * <p>Description: 线程安全的单例模式</p>
 * @author ZhangShenao
 * @date 2017年4月5日
 */
public class ThreadSafeSingleton {
	/**
	 * 私有化构造器，防止客户端随意创建对象
	 */
	private ThreadSafeSingleton(){
		System.out.println("ThreadSafeSingleton实例被创建了");
	}
	
	//使用静态内部类，维护单例类的唯一实例
	private static class SingletonHolder{
		/**
		 * 利用JVM的类初始化机制,该实例在第一次加载SingletonHolder这个类时被初始化，可以延迟加载
		 * JVM的类加载机制是天然线程安全的
		 */
		private static ThreadSafeSingleton instance = new ThreadSafeSingleton();
	}
	
	/**
	 * 对外公开的获取唯一实例的方法
	 */
	public static ThreadSafeSingleton getInstance(){
		return SingletonHolder.instance;
	}
	
	public static void main(String[] args) {
		ThreadSafeSingleton singleton1 = ThreadSafeSingleton.getInstance();
		ThreadSafeSingleton singleton2 = ThreadSafeSingleton.getInstance();
		System.out.println(singleton1 == singleton2);
	}
}

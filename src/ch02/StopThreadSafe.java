package ch02;

import java.util.Random;


/**
 * 通过自定义变量来结束线程
 * @author ZhangShenao
 *
 */
public class StopThreadSafe {
	private static User user = new User();
	private static final Random random = new Random();
	
	/**
	 * 控制线程结束的标志
	 */
	private static volatile boolean isStop = false;
	
	/**
	 * 用户类，当id和name属性相同时，表示数据是一致的
	 * @author ZhangShenao
	 */
	public static class User{
		private int id = 0;
		private String name = "0";
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + "]";
		}
		
		
		
	}
	
	/**
	 * 修改user对象的线程
	 * @author ZhangShenao
	 *
	 */
	public static class ChangeUserThread extends Thread{

		@Override
		public void run() {
			while (!isStop){
				synchronized(user){
					int id = random.nextInt(100);
					user.setId(id);
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					user.setName(String.valueOf(id));
				}
				
				Thread.yield();
			}
		}
		
		/**
		 * 安全终止线程的方法
		 */
		public void safeStop(){
			isStop = true;
		}
	}
	
	/**
	 * 读取用户的线程
	 * @author ZhangShenao
	 *
	 */
	public static class ReadUserThread extends Thread{

		@Override
		public void run() {
			while (true){
				synchronized (user) {
					//打印数据不一致的user
					if (user.getId() != Integer.parseInt(user.getName())){
						System.out.println(user);
					}
				}
				Thread.yield();
			}
			
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		//开启读线程
		ReadUserThread readThread = new ReadUserThread();
		readThread.start();
		
		while (true){
			//开启写线程
			ChangeUserThread changeThread = new ChangeUserThread();
			
			changeThread.start();
			Thread.sleep(150);
			
			//调用自定义方法结束线程
			changeThread.safeStop();
			
		}
	}
}

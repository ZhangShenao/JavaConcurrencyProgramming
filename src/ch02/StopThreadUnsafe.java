package ch02;

import java.util.Random;

/**
 * 调用线程的stop方法，会立即释放线程锁持有的锁，并结束线程，可能出现数据不一致的情况
 * @author ZhangShenao
 *
 */
public class StopThreadUnsafe {
	private static User user = new User();
	private static final Random random = new Random();
	
	/**
	 * 用户类，当id和name属性相同时，表示数据是一致的
	 * @author ZhangShenao
	 *
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
	public static class ChangeUserTask implements Runnable{

		@Override
		public void run() {
			while (true){
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
	}
	
	/**
	 * 读取用户的线程
	 * @author ZhangShenao
	 *
	 */
	public static class ReadUserTask implements Runnable{

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
		Thread readThread = new Thread(new ReadUserTask());
		readThread.start();
		
		while (true){
			//开启写线程
			Thread changeThread = new Thread(new ChangeUserTask());
			
			changeThread.start();
			Thread.sleep(150);
			
			//调用stop方法结束线程
			changeThread.stop();
			
		}
	}
}

package ch02;

import java.util.HashMap;

/**
 * 
 * <p>Description:  多线程环境下使用HashMap,可能出现线程安全问题,可使用ConcurrentHashMap</p>
 * @author ZhangShenao
 * @date 2017年3月27日 上午8:20:29
 */
public class HashMapMultiThread {
	private static final HashMap<String,String> map = new HashMap<>();


	private static class AddTask implements Runnable{
		private int start;
		
		public AddTask(int start) {
			this.start = start;
		}
		
		@Override
		public void run() {
			for (int i = start;i < 100000;i += 2){
				map.put(Integer.toString(i), Integer.toBinaryString(i));
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new AddTask(0));
		Thread t2 = new Thread(new AddTask(1));
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		
		System.out.println("size: " + map.size());
	}
}

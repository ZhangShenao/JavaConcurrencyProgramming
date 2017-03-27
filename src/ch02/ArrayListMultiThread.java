package ch02;

import java.util.ArrayList;

/**
 * 
 * <p>Description:  在多线程环境下使用ArrayList,可能会出现线程安全问题</p>
 * @author ZhangShenao
 * @date 2017年3月27日 上午8:06:53
 */
public class ArrayListMultiThread {
	private static final ArrayList<Integer> nums = new ArrayList<>(10000);
	
	private static class AddTask implements Runnable{
		@Override
		public void run() {
			for (int i = 0;i < 100000;i++){
				//ArrayList在扩容过程中，内部一致性被破坏，但由于没有锁的保护，另一个线程访问到了不一致的内部状态，导致出现越界问题
				//可将ArayList改为Vector
				nums.add(i);
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new AddTask());
		Thread t2 = new Thread(new AddTask());
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		
		System.out.println("size: " + nums.size());
	}
}

package ch02;

import java.util.ArrayList;

public class ArrayListMultiThread {
	private static final ArrayList<Integer> nums = new ArrayList<Integer>(10000);
	
	private static class AddTask implements Runnable{
		@Override
		public void run() {
			for (int i = 0;i < 100000;i++){
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

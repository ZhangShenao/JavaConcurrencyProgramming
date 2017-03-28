package ch02;

import java.util.HashMap;

public class HashMapMultiThread {
	private static final HashMap<String,String> map = new HashMap<String,String>();


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

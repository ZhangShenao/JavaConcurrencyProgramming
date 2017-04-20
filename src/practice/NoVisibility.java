package practice;

import annotation.NotThreadSafe;

/**
 * 
 * <p>Description:不可见性,在没有足够的同步机制的情况下,一个线程对共享变量的修改,对其他线程可能是不可见的</p>
 * @author ZhangShenao
 * @date 2017年4月19日
 */
@NotThreadSafe
public class NoVisibility {
	private static int num;
	private static boolean isStop;
	
	private static class ReaderThread extends Thread{
		@Override
		public void run() {
			while (!isStop){
				Thread.yield();
			}
			System.out.println("num= " + num);
		}
	}
	
	public static void main(String[] args) {
		ReaderThread readerThread = new ReaderThread();
		readerThread.start();
		num = 100;
		isStop = true;
	}
}

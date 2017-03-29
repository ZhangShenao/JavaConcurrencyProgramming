package ch03;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 
 * <p>Description: 读写锁ReadWriteLock,在多个线程读时可以非阻塞执行，在读多写少的程序中可以大大提高效率</p>
 * @author ZhangShenao
 * @date 2017年3月29日
 */
public class ReadWriteLockTest {
	/**
	 * 普通重入锁，所有操作都互斥
	 */
	private static final Lock lock = new ReentrantLock();
	
	/**
	 * 读写锁，对写-写操作和读-写操作互斥，读-读操作可以非阻塞进行
	 */
	private static final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	
	/**
	 * 获取写锁
	 */
	private static final Lock writeLock = readWriteLock.writeLock();
	
	/**
	 * 获取读锁
	 */
	private static final Lock readLock = readWriteLock.readLock();
	
	/**
	 * 临界区
	 */
	private int value = 0;
	
	private static final Random random = new Random();
	
	/**
	 * 处理读操作
	 * @param lock 要加的锁
	 */
	public int handleRead(Lock lock){
		lock.lock();
		try {
			//模拟耗时操作
			Thread.sleep(1000);
			return value;
		}catch (Exception e){
			throw new RuntimeException(e);
		}finally {
			lock.unlock();
		}
	}
	
	/**
	 * 处理写操作
	 * @param lock 要加的锁
	 * @param value 修改后的值
	 */
	public void handleWrite(Lock lock,int value){
		lock.lock();
		try {
			//模拟耗时操作
			Thread.sleep(1000);
			this.value = value;
		}catch (Exception e){
			throw new RuntimeException(e);
		}finally {
			lock.unlock();
		}
	}
	
	public static void main(String[] args) {
		final ReadWriteLockTest test = new ReadWriteLockTest();
		Thread t = null;
		//开启18个读线程
		for (int i = 0;i < 18;i++){
			t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					//使用普通重入锁,读-读操作也互斥
					//System.out.println("value= " + test.handleRead(lock));
					
					//使用读写锁，读-读操作不阻塞
					System.out.println("value= " + test.handleRead(readLock));
					
				}
			});
			t.start();
		}
		
		//开启2个读线程
		for (int i = 0;i < 2;i++){
			t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					//使用写锁
					test.handleWrite(writeLock, random.nextInt());
				}
			});
			t.start();
		}
	}
	
}

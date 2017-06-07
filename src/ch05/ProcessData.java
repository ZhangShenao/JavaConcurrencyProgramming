package ch05;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * 
 * <p>Description:演示读写锁的锁降级</p>
 * <p>锁降级:1.持有写锁; 2.获取读锁; 3.释放写锁</p>
 * @author ZhangShenao
 * @date 2017年6月7日
 */
public class ProcessData {
	/**
	 * 读写锁,非公平,吞吐量优先
	 */
	private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);
	
	/**
	 * 读锁
	 */
	private static final ReadLock readLock = readWriteLock.readLock();
	
	/**
	 * 写锁
	 */
	private static final WriteLock writeLock = readWriteLock.writeLock();
	
	/**
	 * 当前是否为最新数据
	 */
	private volatile boolean update = false; 
	
	/**
	 * 处理数据
	 */
	public void processData(){
		//加读锁,读取数据
		readLock.lock();
		
		//读数据
		readData();
		
		//如果当前数据不是最新,则需要更新
		if (!update){
			//必须先释放读锁
			readLock.unlock();
			
			//1.获取写锁,锁降级开始
			writeLock.lock();
			
			try {
				//更新数据
				if (!update){
					updateData();
					update = true;
				}
				
				//2.获取读锁,防止直接释放写锁导致其他线程修改了共享数据而当前线程无法感知
				readLock.lock();
			}finally {
				//3.释放写锁,锁降级完成,写锁降级为读锁
				writeLock.unlock();
			}
			
			
		}
		
		try {
			readData();
		}finally {
			readLock.unlock();
		}
	}
	
	/**
	 * 读取数据
	 */
	public void readData(){
		
	}
	
	/**
	 * 更新数据
	 */
	public void updateData(){
		
	}
}

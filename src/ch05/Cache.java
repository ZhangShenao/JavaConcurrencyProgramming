package ch05;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * 
 * <p>Description:使用读-写锁机制自定义缓存实现</p>
 * @author ZhangShenao
 * @date 2017年6月7日
 */
public class Cache {
	/**
	 * 使用非线程安全的Map作为缓存的实现
	 */
	private static final Map<String,Object> map = new HashMap<>();
	
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
	 * 向缓存中存入数据
	 */
	public static final void put(String key,Object value){
		try {
			//加写锁
			writeLock.lockInterruptibly();
			map.put(key, value);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if (writeLock.isHeldByCurrentThread()){
				writeLock.unlock();
			}
		}
	}
	
	/**
	 * 从缓存中获取数据
	 * @param key
	 * @return
	 */
	public static final Object get(String key){
		try {
			//加读锁,并发读操作时该线程不会阻塞
			readLock.lockInterruptibly();
			return map.get(key);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}finally {
			readLock.unlock();
		}
	}
	
	/**
	 * 清空缓存
	 */
	public static final void clear(){
		try {
			//加写锁
			map.clear();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if (writeLock.isHeldByCurrentThread()){
				writeLock.unlock();
			}
		}
	}
}

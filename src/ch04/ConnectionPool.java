package ch04;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * 
 * <p>Description:  数据库连接池</p>
 * @author ZhangShenao
 * @date 2017年6月2日 上午7:19:13
 */
public class ConnectionPool {
	/**
	 * 使用双端队列保存数据库连接
	 */
	private LinkedList<Connection> pool = new LinkedList<>();
	
	/**
	 * 初始化数据库连接池
	 * @param initCapacity 初始连接数,默认为10
	 */
	public ConnectionPool(int initCapacity){
		if (initCapacity <= 0){
			initCapacity = 10;
		}
		for (int i = 0;i < initCapacity;i++){
			pool.addLast(ConnectionDriver.getConnection());
		}
	}
	
	/**
	 * 释放数据库连接
	 */
	public void releaseConnection(Connection connection){
		if (null != connection){
			synchronized (pool) {
				pool.addLast(connection);
				
				//释放连接后通知消费者,这样其他消费者就可以感知到连接池中已经归还了一个连接
				pool.notifyAll();
			}
		}
	}
	
	/**
	 * 获取数据库连接池
	 * @param mills 超时等待时间。如果在mills毫秒内仍未获取到连接,则返回null
	 */
	public Connection fetchConenction(long mills) throws InterruptedException{
		synchronized (pool) {
			//无超时等待
			if (mills <= 0){
				while (pool.isEmpty()){
					pool.wait();
				}
				return pool.removeFirst();
			}
				
			//超时等待模式
			long future = System.currentTimeMillis() + mills;
			long remaining = mills;
			
			while (pool.isEmpty() && remaining > 0){
				pool.wait(remaining);
				remaining = future - System.currentTimeMillis();
			}
			
			Connection connection = null;
			if (!pool.isEmpty()){
				connection = pool.removeFirst();
			}
			return connection;
		}
		
	}
	
}

package ch05;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * <p>Description:使用Lock和Condition实现有界队列</p>
 * @author ZhangShenao
 * @date 2017年6月7日
 */
public class BoundedQueue<T> {
	/**
	 * 使用数组维护队列元素
	 */
	private Object[] items;
	
	/**
	 * 添加的下标
	 */
	private int addIndex;
	
	/**
	 * 删除的下标
	 */
	private int removeIndex;
	
	/**
	 * 当前队列内的元素数量
	 */
	private int count;
	
	/**
	 * 锁对象
	 */
	private Lock lock = new ReentrantLock();
	
	/**
	 * 队列非空的条件对象
	 */
	private Condition notEmpty = lock.newCondition();
	
	/**
	 * 队列未满的条件对象
	 */
	private Condition notFull = lock.newCondition();
	
	/**
	 * 将元素添加到队列末尾,如果队列已满,则添加线程进入等待状态,直到队列中有元素
	 */
	public void add(T value){
		try {
			lock.lockInterruptibly();
			
			while (addIndex == items.length){
				notFull.await();
			}
			items[addIndex] = value;
			
			if (++addIndex == items.length){
				addIndex = 0;
			}
			++count;
			
			//唤醒等待中的删除线程
			notEmpty.signal();
			
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	
	/**
	 * 删除队头的元素,如果队列为空,则删除线程进入等待状态,直到队列中有新元素
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T remove(){
		try {
			lock.lockInterruptibly();
			while (count == 0){
				notEmpty.await();
			}
			Object removed = items[removeIndex];
			if (++removeIndex == items.length){
				removeIndex = 0;
			}
			--count;
			
			//唤醒等待中的添加线程
			notFull.signal();
			
			return (T) removed;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}finally {
			lock.unlock();
		}
	}
}

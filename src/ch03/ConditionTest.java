package ch03;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * <p>Description:  Condition条件对象，与一个ReentrantLock对象绑定，可以协调多线程间的协作</p>
 * @author ZhangShenao
 * @date 2017年3月28日 下午11:10:11
 */
public class ConditionTest implements Runnable{
	//创建锁对象
	private static final ReentrantLock lock = new ReentrantLock();
	
	//创建与锁对象绑定的条件对象
	private static final Condition condition = lock.newCondition();
	
	@Override
	public void run() {
		try {
			//使用条件对象的前提是，当前线程一定要先持有锁
			lock.lock();
			
			System.out.println(Thread.currentThread().getName() + "获取到锁，准备执行");
			
			//使用条件对象，让当前线程等待
			System.out.println(Thread.currentThread().getName() + "等待");
			//线程await(),会释放锁
			condition.await();
			
			//线程被唤醒，继续执行
			System.out.println(Thread.currentThread().getName() + "被唤醒，继续执行");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			if (lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(new ConditionTest(), "T1");
		t.start();
		
		Thread.sleep(3000);
		
		//唤醒等待的线程,同样要先持有锁
		//被唤醒的线程要重新获取到锁，才能继续执行
		lock.lock();
		condition.signalAll();
		lock.unlock();
	}
}

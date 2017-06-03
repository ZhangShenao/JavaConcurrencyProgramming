package ch05;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 
 * <p>Description:自定义同步组件——独占锁,使用AbstractQueuedSynchronizer队列同步器,只能以独占的形式进行同步,不支持共享</p>
 * @author ZhangShenao
 * @date 2017年6月3日
 */
public class Mutex implements Lock{
	/**
	 * 
	 * <p>Description:自定义同步器</p>
	 * @author ZhangShenao
	 * @date 2017年6月3日
	 */
	private static class Sync extends AbstractQueuedSynchronizer{
		
		/**
		 * 判断当前同步状态是否被线程独占
		 */
		@Override
		protected boolean isHeldExclusively() {
			//当前状态为1时,表示被线程独占
			return getState() == 1;
		}
		
		/**
		 * 以独占的方式获取同步状态,该方法需要查询当前同步状态并判断同步状态是否符合预期,然后再进行CAS设置同步状态
		 */
		@Override
		protected boolean tryAcquire(int arg) {
			//在当前状态为0的时候获取锁,并将状态设置为1,独占状态
			if (compareAndSetState(0, 1)){
				//设置当前线程为独占锁的线程
				setExclusiveOwnerThread(Thread.currentThread());
				return true;
			}
			return false;
		}
		
		/**
		 * 以独占的方式释放同步状态,等待获取同步状态的线程将有机会获取到同步状态
		 */
		@Override
		protected boolean tryRelease(int arg) {
			if (getState() == 0){
				throw new IllegalMonitorStateException();
			}
			
			//将独占同步状态的线程置空
			setExclusiveOwnerThread(null);
			
			//设置当前同步状态为0
			setState(0);
			return true;
		}
		
		/**
		 * 获取一个Condition对象,每个Condition对象都包含了一个Condition队列
		 */
		protected Condition newCondition(){
			return new ConditionObject();
		}
	}
	
	/**
	 * 同步器实例,关于锁的操作,都委托给同步器执行
	 */
	private final Sync sync = new Sync();
	
	@Override
	public void lock() {
		sync.acquire(1);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		sync.acquireInterruptibly(1);
	}

	@Override
	public boolean tryLock() {
		return sync.tryAcquire(1);
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit)
			throws InterruptedException {
		return sync.tryAcquireNanos(1, unit.toNanos(time));
	}

	@Override
	public void unlock() {
		sync.release(1);
	}

	@Override
	public Condition newCondition() {
		return sync.newCondition();
	}

}

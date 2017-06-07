package ch05;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 
 * <p>Description:基于AbstractQueuedSynchronizer队列同步器实现的自定义同步组件,在同一时刻只允许至多两个线程同时访问,超过两个线程的访问将被阻塞</p>
 * @author ZhangShenao
 * @date 2017年6月7日
 */
public class TwinsLock implements Lock{
	/**
	 * 
	 * <p>Description:自定义同步器,被定义为自定义同步组件的内部类</p>
	 * <p>以共享模式访问,同一时刻最多允许两个线程共享访问,同步资源数为2</p>
	 * @author ZhangShenao
	 * @date 2017年6月7日
	 */
	private static final class Sync extends AbstractQueuedSynchronizer{
		private static final long serialVersionUID = 1L;

		Sync(int count){
			if (count <= 0){
				throw new IllegalArgumentException("count must large than 0!!");
			}
			setState(count);
		}
		
		/**
		 * 尝试已共享模式获取当前状态
		 * @param reduceCount 每次获取当前状态时,要将当前状态值-reduceCount
		 * @return 返回成功更新后的当前状态值。如果为正数则表示获取成功,负数表示获取失败
		 */
		@Override
		protected int tryAcquireShared(int reduceCount) {
			for (;;){
				//获取当前状态
				int current = getState();
				
				//更新后的同步状态
				int newCount = current - reduceCount;
				
				//通过CAS确保将当前状态设置为更新后的状态,并返回更新后的状态
				if (newCount < 0 || compareAndSetState(current, newCount)){
					return newCount;
				}
			}
		}
		
		/**
		 * 尝试以共享模式释放当前状态
		 * @param returnCount 每次释放当前状态,要将当前状态值+returnCount
		 * @return 是否成功释放当前状态
		 */
		@Override
		protected boolean tryReleaseShared(int returnCount) {
			for (;;){
				int current = getState();
				int newCount = current + returnCount;
				if (compareAndSetState(current, newCount)){
					return true;
				}
			}
		}
		
		/**
		 * 获取条件对象
		 */
		protected ConditionObject newCondition(){
			return new ConditionObject();
		}
	}
	
	/**
	 * 自定义同步器的实例,自定义同步组件的操作都委托给自定义同步器来实现
	 */
	private final Sync sync = new Sync(2);
	
	@Override
	public void lock() {
		sync.acquireShared(1);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		sync.acquireSharedInterruptibly(1);
	}

	@Override
	public boolean tryLock() {
		return sync.tryAcquireShared(1) >= 0;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit)
			throws InterruptedException {
		return sync.tryAcquireSharedNanos(1, unit.toNanos(time));
	}

	@Override
	public void unlock() {
		sync.releaseShared(1);
	}

	@Override
	public Condition newCondition() {
		return sync.newCondition();
	}
	
}

package ch01;

/**
 * 
 * <p>Description:测试死锁,处于死锁的线程都处于Blocked状态等待锁对象</p>
 * @author ZhangShenao
 * @date 2017年5月24日
 */
public class DeadLockTest {
	private String A = "A";
	private String B = "B";
	
	private void deadLock(){
		//线程1,先拿锁A再拿锁B
		Thread t1 = new Thread(new Runnable(){
			@Override
			public void run() {
				synchronized (A) {
					try {
						Thread.sleep(1000);
						synchronized (B) {
							System.err.println("1");
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		},"T1");
		
		//线程2,先拿锁B再拿锁A
		Thread t2 = new Thread(new Runnable(){
			@Override
			public void run() {
				synchronized (B) {
					synchronized (A) {
						System.err.println("2");
					}
				}
			}
		},"T2");
		
		t1.start();
		t2.start();
	}
	
	public static void main(String[] args) {
		DeadLockTest deadLockTest = new DeadLockTest();
		deadLockTest.deadLock();
	}
}

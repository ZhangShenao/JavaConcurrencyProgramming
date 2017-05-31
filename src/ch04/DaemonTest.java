package ch04;

/**
 * 
 * <p>Description:  Deamon后台线程。当一个JVM中不存在非Daemon线程时,JVM就会退出</p>
 * <p>在构建Daemon线程时,不能依靠finally块中的内容来确保执行关闭和清理的逻辑</p>
 * @author ZhangShenao
 * @date 2017年6月1日 上午7:51:42
 */
public class DaemonTest {
	public static void main(String[] args) {
		Thread t = new Thread(new DeamonTask());
		t.setDaemon(true);
		t.start();
	}
	
	private static class DeamonTask implements Runnable{
		@Override
		public void run() {
			try {
				SleepUtil.sleep(10);
			}finally {
				//当所有非Daemon线程执行完毕后,JVM关闭,Daemon线程的finally块不会被执行到
				System.err.println("DeamonTask 执行结束");
			}
		}
	}
}

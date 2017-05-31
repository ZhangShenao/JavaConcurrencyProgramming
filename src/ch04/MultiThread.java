package ch04;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * 
 * <p>Description: 查看一个普通Java程序所包含的线程 </p>
 * @author ZhangShenao
 * @date 2017年6月1日 上午7:04:02
 */
public class MultiThread {
	public static void main(String[] args) {
		//获取Java管理MXBean
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		
		//不需要获取同步的monitor和synchronizer信息,仅获取线程和线程堆栈信息
		ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
		
		//遍历线程信息,进打印线程ID和线程名称
		for (ThreadInfo threadInfo : threadInfos){
			System.out.println("[" + threadInfo.getThreadId() + "] " + threadInfo.getThreadName());
			
			/**
			 *  [5] Attach Listener
			    [4] Signal Dispatcher 分发处理发送给JVM信号的线程
				[3] Finalizer		  调用对象finalize方法的线程
				[2] Reference Handler	清除Reference的线程
				[1] main 			main线程,应用程序入口
			 */
		}
	}
}

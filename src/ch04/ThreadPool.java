package ch04;

/**
 * 
 * <p>Description:线程池接口</p>
 * @author ZhangShenao
 * @date 2017年6月2日
 */
public interface ThreadPool<Job extends Runnable> {
	/**
	 * 执行一个Job,这个Job需要实现Runnable接口
	 */
	public void execute(Job job);
	
	/**
	 * 关闭线程池
	 */
	public void shutdown();
	
	/**
	 * 增加工作者线程
	 */
	public void addWorkers(int num);
	
	/**
	 * 减少工作者线程
	 */
	public void removeWorker(int num);
	
	/**
	 * 获取正在等待执行的任务数
	 */
	public int getJobSize();
}

package ch04;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * <p>Description:线程池接口的默认实现</p>
 * @author ZhangShenao
 * @date 2017年6月2日
 */
public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {
	/**
	 * 限制的最大工作线程数
	 */
	private static final int MAX_WORKER_NUM = 10;
	
	/**
	 * 默认工作线程数
	 */
	private static final int DEAFULT_WORKER_NUM = 5;
	
	/**
	 * 最小工作线程数
	 */
	private static final int MIN_WORKER_NUM = 1;
	
	/**
	 * 存放所有工作线程的集合
	 */
	private final ArrayList<Worker> WORKER_QUEUE;
	
	/**
	 * 存放所有Job任务的队列
	 */
	private final LinkedList<Job> JOB_QUEUE = new LinkedList<Job>();
	
	/**
	 * 线程池内的工作线程数
	 */
	private int workerNum; 
	
	/**
	 * Worker线程的id生成器
	 */
	private final AtomicLong WORKER_ID_GENERATOR = new AtomicLong(0L);
	
	
	/**
	 * 构造指定线程数的线程池
	 */
	public DefaultThreadPool(int initSize){
		int workerSize = ensureSize(initSize);
		WORKER_QUEUE = new ArrayList<Worker>(workerSize);
		initWorkerThread(workerSize);
	}
	
	/**
	 * 构造默认线程数的线程池
	 */
	public DefaultThreadPool() {
		this(DEAFULT_WORKER_NUM);
	}
	
	/**
	 * 初始化并启动Worker线程
	 */
	private void initWorkerThread(int threadNum){
		Worker worker = null;
		for (int i = 1;i <= threadNum;i++){
			worker = new Worker();
			WORKER_QUEUE.add(worker);
			new Thread(worker,"Worker-Thread-" + WORKER_ID_GENERATOR.incrementAndGet()).start();
		}
		
		workerNum += threadNum;
	}
	
	
	@Override
	public void execute(Job job) {
		if (null != job){
			//将Job任务放入任务队列中
			synchronized (JOB_QUEUE) {
				JOB_QUEUE.addLast(job);
				
				//唤醒等待的线程,notify会比notifyAll方法获得更小的开销,避免把等待队列中的线程全部移动到阻塞队列中
				JOB_QUEUE.notify();
			}
		}
	}

	@Override
	public void shutdown() {
		//停止所有工作线程
		for (Worker worker : WORKER_QUEUE){
			worker.stop();
		}
	}

	@Override
	public void addWorkers(int num) {
		//校验工作线程数是否超过上限
		if (num > MAX_WORKER_NUM - workerNum){
			throw new RuntimeException("工作线程数量超过上限!!");
		}
		synchronized (JOB_QUEUE) {
			initWorkerThread(num);
		}
	}

	@Override
	public void removeWorker(int num) {
		//保留最少的工作线程数
		if (workerNum - num < MIN_WORKER_NUM){
			throw new RuntimeException("没有足够数量的工作线程!!");
		}
		
		//停止并删除指定数量的工作线程
		synchronized (JOB_QUEUE) {
			for (int i = 0;i < num;i++){
				try {
					Worker worker = WORKER_QUEUE.get(i);
					if (null != worker){
						worker.stop();
					}
					WORKER_QUEUE.remove(i);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		workerNum -= num;
	}

	@Override
	public int getJobSize() {
		synchronized (JOB_QUEUE) {
			return JOB_QUEUE.size();
		}
	}
	
	/**
	 * 设置工作线程数量
	 * @param initSize 初始数量
	 */
	private int ensureSize(int initSize){
		if (initSize <= 0){
			return MIN_WORKER_NUM;
		}
		if (initSize > MAX_WORKER_NUM){
			return MAX_WORKER_NUM;
		}
		return initSize;
	}
	
	/**
	 * 
	 * <p>Description:工作线程,负责执行提交到线程池中的Job</p>
	 * @author ZhangShenao
	 * @date 2017年6月2日
	 */
	class Worker implements Runnable{
		/**
		 * 停止工作线程的标志
		 */
		private volatile boolean isStop = false;
		@Override
		public void run() {
			while (!isStop){
				Job job = null;
				try {
					synchronized (JOB_QUEUE) {
						//如果任务队列为空,则等待
						while (JOB_QUEUE.isEmpty()){
							JOB_QUEUE.wait();
						}
						job = JOB_QUEUE.removeFirst();
						if (null != job){
							job.run();
						}
					}
				}catch (InterruptedException e){
					e.printStackTrace();
					
					//感知到外部的中断请求,中断线程
					Thread.currentThread().interrupt();
				}
			}
		}
		
		/**
		 * 停止工作线程
		 */
		private void stop(){
			isStop = true;
		}
	}
	
}

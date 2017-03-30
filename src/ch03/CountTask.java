package ch03;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 
 * <p>Description:  Fork/Join框架 ForkJoinTask有两个子类,RecursiveTask表示带返回值的任务,
 * RecursiveAction表示不带返回值的任务</p>
 * @author ZhangShenao
 * @date 2017年3月30日 下午10:39:44
 */
public class CountTask extends RecursiveTask<Long>{
	private static final long serialVersionUID = 1L;

	/**
	 * 是否需要fork的临界值
	 */
	private static final long THRESHHOLD = 10000L;
	
	/**
	 * fork的子任务数
	 */
	private static final int FORK_TASK_NUM = 100;
	
	/**
	 * 子任务的起始值
	 */
	private long start;
	
	/**
	 * 子任务的结束值
	 */
	private long end;
	
	/**
	 * 创建一个CountTask
	 */
	public CountTask(long start, long end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected Long compute() {
		long sum = 0L;
		boolean needFork = (end - start) >= THRESHHOLD;
		
		//如果计算范围较小,则无需fork,直接计算即可
		if (!needFork){
			for (long i = start;i <= end;i++){
				sum += i;
			}
		}
		
		//计算量较大时，将任务fork成多个子任务
		else {
			//保存子任务的List
			ArrayList<CountTask> subTasks = new ArrayList<>();
			
			long step = (end - start) / FORK_TASK_NUM;
			long pos = start;
			for (int i = 0;i < FORK_TASK_NUM;i++){
				long endPos = pos + step;
				if (endPos > end){
					endPos = end;
				}
				
				//创建子任务
				CountTask subTask = new CountTask(pos, endPos);
				subTasks.add(subTask);
				pos += step + 1;
				
				//fork
				subTask.fork();
			}
			
			//join
			for (CountTask task : subTasks){
				sum += task.join();
			}
		}
		return sum;
	}
	
	public static void main(String[] args) throws Exception{
		//创建Fork/Join线程池
		ForkJoinPool pool = new ForkJoinPool();
		
		//提交Fork/Join任务
		ForkJoinTask<Long> result = pool.submit(new CountTask(0L, 200000L));
		
		//获取结果,同步等待
		System.out.println("sum = " + result.get());
		
	}
	
}

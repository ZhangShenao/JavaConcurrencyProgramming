package ch06;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * 
 * <p>Description:使用Fork/Join框架,计算加法</p>
 * <p>ForkJoinTask有两个子类: RecursiveTask用于执行有返回结果的任务; RecursiveAction用于执行没有返回结果的任务</p>
 * @author ZhangShenao
 * @date 2017年6月7日
 */
public class CountTask extends RecursiveTask<Integer>{
	private static final long serialVersionUID = 1L;

	/**
	 * 相加数量的临界值,如果计算超过临界值个数相加,则Fork子任务
	 */
	private static final int THRESHOLD = 5;
	
	/**
	 * 最小值
	 */
	private int start;
	
	/**
	 * 最大值
	 */
	private int end;
	
	public CountTask(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		//如果加数的数量没有超过临界值,则直接计算
		if (start - end <= THRESHOLD){
			int sum = 0;
			for (int i = start;i <= end;i++){
				sum += i;
			}
			return sum;
		}
		
		//如果超过临界值,则Fork成两个子任务
		else {
			int mid = (start + end) / 2;
			CountTask leftTask = new CountTask(start, mid);
			CountTask rightTask = new CountTask(mid + 1, end);
			
			//执行子任务
			leftTask.fork();
			rightTask.fork();
			
			//等待子任务执行完毕,获取结果
			int leftSum = leftTask.join();
			int rightSum = rightTask.join();
			return leftSum + rightSum;
		}
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		//创建ForkJoinPool用于执行ForkJoinTask
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		CountTask countTask = new CountTask(1, 1000);
		
		Future<Integer> future = forkJoinPool.submit(countTask);
		System.out.println("sum= " + future.get());
	}

}

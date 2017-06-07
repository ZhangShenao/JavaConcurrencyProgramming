package ch08;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * <p>Description:银行流水处理服务类</p>
 * @author ZhangShenao
 * @date 2017年6月7日
 */
public class BankWaterService implements Runnable{
	/**
	 * 任务数
	 */
	private static final int TASK_NUM = 4;
	
	/**
	 * 创建同步屏障,处理完之后执行当前对象的run方法
	 */
	private final CyclicBarrier cyclicBarrier = new CyclicBarrier(TASK_NUM,this);
	
	/**
	 * 线程池
	 */
	private final ExecutorService pool = Executors.newFixedThreadPool(TASK_NUM);
	
	/**
	 * 保存每个sheet计算出的银行流水结果
	 */
	private final ConcurrentHashMap<String,Integer> sheetBankWaterCount = new ConcurrentHashMap<String, Integer>(TASK_NUM);
	
	/**
	 * 计算每个银行的流水
	 */
	public void count(){
		for (int i = 0;i < TASK_NUM;i++){
			pool.execute(new Runnable(){
				@Override
				public void run() {
					//保存每个银行的统计结果
					sheetBankWaterCount.put(Thread.currentThread().getName(), 1);
					
					//统计完成,设置一个屏障,等待汇总
					try {
						cyclicBarrier.await();
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			});
		}
	}
	
	@Override
	public void run() {
		//汇总每个sheet计算出的结果
		int result = 0;
		for (int value : sheetBankWaterCount.values()){
			result += value;
		}
		
		//保存汇总结果并输出
		sheetBankWaterCount.put("result", result);
		System.out.println("银行流水汇总结果: " + result);
		
		//关闭线程池
		pool.shutdown();
	}
	
	public static void main(String[] args) {
		BankWaterService service = new BankWaterService();
		service.count();
		
	}
}

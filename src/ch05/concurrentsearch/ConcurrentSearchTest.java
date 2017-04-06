package ch05.concurrentsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * <p>Description: 并行搜索，将待搜索数组按照线程数分段，每个线程搜索自己所属的一段，使用原子类在多个线程之间共享搜索结果</p>
 * @author ZhangShenao
 * @date 2017年4月6日
 */
public class ConcurrentSearchTest {
	public static final int THREAD_NUM = Runtime.getRuntime().availableProcessors() * 2;
	
	/**
	 * 进行并行搜索
	 * @param arr 待搜索的数组
	 * @param key 待搜索的关键字
	 * @return 关键字所在的下标，搜索失败返回-1
	 */
	private static int pSearch(int[] arr,int key) throws Exception{
		//创建线程池
		ExecutorService pool = Executors.newFixedThreadPool(THREAD_NUM);
		
		//按照线程数，对数组进行分段
		int len = arr.length;
		int step = len / THREAD_NUM + 1;
		
		//保存Future结果的List
		List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();
		
		//开启线程进行搜索
		int start = 0;
		int end = 0;
		for (int i = 0;i < THREAD_NUM;i++){
			//计算起始位置
			end = start + step;
			if (end >= len){
				end = len;
			}
			
			//保存搜索结果
			futureList.add(pool.submit(new SearchTask(key, start, end, arr)));
			
			start = end + 1;
		}
		
		//获取搜索结果
		int result = -1;
		for (Future<Integer> future : futureList){
			if (future.get() >= 0){
				result = future.get();
				break;
			}
		}
		 
		//关闭线程池
		pool.shutdown();
		
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		int[] arr = {1,35,47,658,-325,658,34,-2352,-5476,-45756,324};
		System.out.println("搜索结果: " + pSearch(arr, -45756));
	}
}

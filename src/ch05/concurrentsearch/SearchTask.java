package ch05.concurrentsearch;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * <p>Description: 进行并行搜索的线程</p>
 * @author ZhangShenao
 * @date 2017年4月6日
 */
public class SearchTask implements Callable<Integer>{
	/**
	 * 待搜索的关键字
	 */
	private int key;
	
	/**
	 * 搜索的起始下标
	 */
	private int start;
	
	/**
	 * 搜索的结束下标
	 */
	private int end;
	
	/**
	 * 待搜索的数组
	 */
	private int[] arr;
	
	/**
	 * 多个线程共享的搜索结果，使用原子类
	 */
	private static AtomicInteger index = new AtomicInteger(-1);
	
	public SearchTask(int key, int start, int end, int[] arr) {
		this.key = key;
		this.start = start;
		this.end = end;
		this.arr = arr;
	}

	@Override
	public Integer call() throws Exception {
		return search();
	}
	
	/**
	 * 在当前线程所属分段进行搜索
	 */
	public int search(){
		//如果其他线程已经找到结果，则直接返回
		if (index.get() >= 0){
			return index.get();
		}
		
		//在本线程所属段进行搜索
		for (int i = start;i < end;i++){
			if (arr[i] == key){
				//搜索成功，设置结果
				if (index.compareAndSet(-1, i)){
					return i;
				}
				
				//如果设置失败,表示其他线程已经先一步搜索成功，则返回其他线程的结果
				return index.get();
			}
		}
		
		//本线程搜索失败，返回-1
		return -1;
	}

}

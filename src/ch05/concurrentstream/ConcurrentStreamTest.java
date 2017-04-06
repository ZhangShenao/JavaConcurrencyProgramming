package ch05.concurrentstream;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * <p>Description: 使用并行流水线,可以有效地将有依赖关系的操作分配在不同的线程中执行，尽可能地利用多核的优势</p>
 * @author ZhangShenao
 * @date 2017年4月6日
 */
public class ConcurrentStreamTest {
	public static void main(String[] args) throws InterruptedException {
		//创建线程池
		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
		
		//创建流水线线程
		PlusTask plusTask = new PlusTask();
		MultiTask multiTask = new MultiTask();
		DivTask divTask = new DivTask();
		
		//开启流水线
		pool.submit(plusTask);
		pool.submit(multiTask);
		pool.submit(divTask);
		
		//创建任务
		for (int i = 1;i < 10000;i++){
			for (int j = 1;j < 10000;j++){
				Msg msg = new Msg();
				msg.setI(i);
				msg.setJ(j);
				msg.setExp("((" + i + "+" + j + ")*" + i + ")/2");
				
				//将任务交给第一个流水线开始执行
				PlusTask.queue.put(msg);
			}
		}
		
		Thread.sleep(5000);
		
		//关闭流水线
		plusTask.stop();
		multiTask.stop();
		divTask.stop();
		
		Thread.sleep(2000);
		//关闭线程池
		pool.shutdown();
	}
}

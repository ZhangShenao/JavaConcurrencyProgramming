package ch05.disruptor;

import com.lmax.disruptor.WorkHandler;

/**
 * <p>Description:  消费者，实现WorkHandler接口</p>
 * @author ZhangShenao
 * @date 2017年4月5日 下午11:00:55
 */
public class Consumer implements WorkHandler<PCData>{

	@Override
	public void onEvent(PCData event) throws Exception {
		long value = event.getValue();
		System.out.println("Thread:" + Thread.currentThread().getId() + "-->" + value + " * " + value + " = " + (value * value ));
	}

}

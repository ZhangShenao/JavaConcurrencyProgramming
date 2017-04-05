package ch05.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * 
 * <p>Description:  PCData的工厂类。它会在Disruptor系统初始化时，构造所有缓冲区中的对象</p>
 * @author ZhangShenao
 * @date 2017年4月5日 下午11:03:43
 */
public class PCDataFactory implements EventFactory<PCData>{

	public PCData newInstance() {
		//创建PCData对象
		return new PCData();
	}

}

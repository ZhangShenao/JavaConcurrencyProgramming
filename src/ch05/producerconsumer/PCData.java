package ch05.producerconsumer;

/**
 * 
 * <p>Description: 生产者和消费者之间的共享数据模型,采用不变模式</p>
 * @author ZhangShenao
 * @date 2017年4月5日
 */
public final class PCData {
	private final int intData;
	
	public PCData(int intData) {
		this.intData = intData;
	}
	
	public PCData(String data) {
		this.intData = Integer.parseInt(data);
	}

	public int getIntData() {
		return intData;
	}

	@Override
	public String toString() {
		return "PCData [intData=" + intData + "]";
	}
	
	
}

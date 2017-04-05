package ch05.disruptor;

/**
 * 
 * <p>Description: 生产者和消费者之间的共享数据模型</p>
 * @author ZhangShenao
 * @date 2017年4月5日
 */
public final class PCData {
	private long value;

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
}

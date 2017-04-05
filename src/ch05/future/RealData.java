package ch05.future;

/**
 * 
 * <p>Description:真实的数据</p>
 * @author ZhangShenao
 * @date 2017年4月5日
 */
public class RealData implements Data{
	private String result;
	
	/**
	 * RealData表示一个重量级对象，构造比较耗时
	 */
	public RealData(String result) {
		//模拟耗时操作
		StringBuffer buffer = new StringBuffer();
		for (int i = 0;i < 10;i++){
			buffer.append(result);
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		this.result = buffer.toString();
	}

	@Override
	public String getResult() {
		return this.result;
	}

}

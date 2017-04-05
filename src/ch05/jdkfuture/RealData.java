package ch05.jdkfuture;

import java.util.concurrent.Callable;

public class RealData implements Callable<String>{
	private String para;
	
	public RealData(String para) {
		this.para = para;
	}
	
	@Override
	public String call() throws Exception {
		//模拟耗时操作
		StringBuffer buffer = new StringBuffer();
		for (int i = 0;i < 10;i++){
			buffer.append(para);
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

}

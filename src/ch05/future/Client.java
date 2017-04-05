package ch05.future;

/**
 * 
 * <p>Description: 用于获取Data实例，调用后立即返回FutureData对象，并在内部开启ClientThread线程装配RealData</p>
 * @author ZhangShenao
 * @date 2017年4月5日
 */
public class Client {
	public static Data request(final String query){
		//立即创建FutureData对象并返回
		final FutureData future = new FutureData();
		
		//内部开启ClientThread线程，装配RealData
		Thread clientThread = new Thread(){
			@Override
			public void run() {
				RealData realData = new RealData(query);
				future.setRealData(realData);
			}
		};
		
		clientThread.start();
		return future;
	}
	
}

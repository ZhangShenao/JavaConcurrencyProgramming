package ch05.future;

/**
 * 
 * <p>Description:测试Future模式</p>
 * @author ZhangShenao
 * @date 2017年4月5日
 */
public class Main {
	public static void main(String[] args) throws InterruptedException {
		//通过Client类获取异步Data对象,request方法立即返回
		Data data = Client.request("name");
		
		//模拟其他操作，可立即进行，无需等待
		System.out.println("继续执行其他操作");
		Thread.sleep(2000);
		
		//使用真实的数据
		System.out.println("真实的数据: " + data.getResult());
	}
}

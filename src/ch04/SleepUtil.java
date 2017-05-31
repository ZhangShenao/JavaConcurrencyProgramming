package ch04;

import java.util.concurrent.TimeUnit;

/**
 * 
 * <p>Description:  线程休眠的工具类</p>
 * @author ZhangShenao
 * @date 2017年6月1日 上午7:49:12
 */
public class SleepUtil {
	/**
	 * 线程休眠
	 * @param seconds 单位秒
	 */
	public static final void sleep(long seconds){
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

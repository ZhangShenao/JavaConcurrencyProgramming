package ch04;

import java.io.Closeable;

/**
 * 
 * <p>Description:关闭资源的工具类</p>
 * @author ZhangShenao
 * @date 2017年6月3日
 */
public class CloseUtil {
	/**
	 * 关闭所有资源
	 */
	public static final void closeAll(Closeable... resources){
		for (Closeable resource : resources){
			try {
				resource.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}

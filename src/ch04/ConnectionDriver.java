package ch04;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;

/**
 * 
 * <p>Description:  数据库连接池驱动</p>
 * @author ZhangShenao
 * @date 2017年6月2日 上午7:14:18
 */
public class ConnectionDriver {
	private static class DriverHandler implements InvocationHandler{
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			if ("commit".equals(method.getName())){
				TimeUnit.MILLISECONDS.sleep(50);
			}
			return null;
		}
		
	}
	
	/**
	 * 创建数据库连接
	 */
	public static final Connection getConnection(){
		return (Connection) Proxy.newProxyInstance(ConnectionDriver.class.getClassLoader(), new Class[]{Connection.class}, 
				new DriverHandler());
	}
}

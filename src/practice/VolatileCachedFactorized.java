package practice;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 
 * <p>Description:使用指向不可变容器对象的volatile类型引用以缓存最新的结果,在没有显式锁的情况下仍然能保证线程安全</p>
 * @author ZhangShenao
 * @date 2017年4月20日
 */
public class VolatileCachedFactorized extends GenericServlet{
	private static final long serialVersionUID = 1L;
	
	//上一次因式分解的缓存,使用volatile保证其可见性
	private volatile OneValueCache cache;

	@Override
	public void service(ServletRequest req, ServletResponse resp)
			throws ServletException, IOException {
		BigInteger i = extractFromRequest(req);
		BigInteger[] factors = null;
		
		//从缓存对象中获取结果,由于缓存对象是不可变的,因此无需进行同步操作
		if (null != cache){
			factors = cache.getCachedFactors(i);
			factors = factor(i);
		}
		
		//无法从缓存对象中获取结果,则重新计算,并创建新的缓存对象
		if (null == factors){
			factors = factor(i);
			cache = new OneValueCache(i, factors);
		}
		
		encodeIntoResponse(factors, resp);
		
	}
	
	/**
	 * 从request中获取待分解的整数
	 */
	private BigInteger extractFromRequest(ServletRequest req){
		return new BigInteger("7");
	}
	
	/**
	 * 执行因数分解(模拟)
	 */
	private BigInteger[] factor(BigInteger i){
		return new BigInteger[]{i};
	}
	
	/**
	 * 将分解后的所有因此封装成ServletResponse
	 */
	private void encodeIntoResponse(BigInteger[] factors,ServletResponse resp){
		
	}

}

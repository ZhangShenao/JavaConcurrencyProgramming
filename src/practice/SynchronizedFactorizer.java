package practice;

import java.io.IOException;
import java.math.BigInteger;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import annotation.GuardedBy;
import annotation.ThreadSafe;

/**
 * 
 * <p>Description:使用内置锁synchronized保证线程安全,但是并发效率很低</p>
 * @author ZhangShenao
 * @date 2017年4月19日
 */
@ThreadSafe
public class SynchronizedFactorizer extends GenericServlet{
	private static final long serialVersionUID = 1L;
	
	@GuardedBy("this")
	/**
	 * 缓存上一次待分解的整数
	 */
	private BigInteger lastNumber;
	
	@GuardedBy("this")
	/**
	 * 缓存上一次分解后的因子
	 */
	private BigInteger[] lastFactors;
	
	@Override
	public synchronized void service(ServletRequest req, ServletResponse resp)
			throws ServletException, IOException {
		BigInteger i = extractFromRequest(req);
		if (i.equals(lastNumber)){
			encodeIntoResponse(lastFactors, resp);
		}
		else {
			lastNumber = i;
			lastFactors = factor(i);
			encodeIntoResponse(lastFactors, resp);
		}
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

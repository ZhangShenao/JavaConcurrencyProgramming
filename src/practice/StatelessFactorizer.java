package practice;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import annotation.ThreadSafe;

/**
 * 
 * <p>Description:一个无状态的Servlet,模拟因数分解</p>
 * <p>StatelessFactorizer不包含任何状态,计算的中间结果仅保存在各自线程栈上,多个线程之间不会共享状态,就好像在访问不同的实例</p>
 * <p>无状态的对象一定是线程安全的</p>
 * @author ZhangShenao
 * @date 2017年4月19日
 */
@ThreadSafe
public class StatelessFactorizer extends GenericServlet{
	private static final long serialVersionUID = 1L;

	@Override
	public void service(ServletRequest req, ServletResponse resp)
			throws ServletException, IOException {
		BigInteger i = extractFromRequest(req);
		BigInteger[] factors = factor(i);
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

package practice;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import annotation.ThreadSafe;

/**
 * 
 * <p>Description:带计数器的CountingFactorizer,使用AtomicInteger原子对象进行计数,模拟因数分解</p>
 * <p>当在无状态的类中添加一个状态时,如果该状态完全由线程安全的对象来管理,那么这个类仍然是线程安全的</p>
 * @author ZhangShenao
 * @date 2017年4月19日
 */
@ThreadSafe
public class CountingFactorizer extends GenericServlet{
	private static final long serialVersionUID = 1L;
	
	private final AtomicInteger count = new AtomicInteger(0);

	@Override
	public void service(ServletRequest req, ServletResponse resp)
			throws ServletException, IOException {
		//AtomicInteger能确保对其的操作是原子性的
		count.incrementAndGet();
		
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

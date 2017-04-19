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
 * <p>Description:带缓存的因数分解器,增加缓存命中计数器。使用syncronized代码块减小锁粒度</p>
 * <p>获取和释放锁同样需要一定的开销,因此也不要将锁粒度分解地过细</p>
 * <p>当执行时间较长的计算或者无法快速完成的操作(如I/O)时,一定不要持有锁</p>
 * @author ZhangShenao
 * @date 2017年4月19日
 */
@ThreadSafe
public class CachedFactorizer extends GenericServlet{
	private static final long serialVersionUID = 1L;
	
	@GuardedBy("this")
	private BigInteger lastNumber;	// 缓存上一次待分解的整数
	
	@GuardedBy("this")
	private BigInteger[] lastFactors;	//缓存上一次分解后的因子
	
	@GuardedBy("this")
	private int hits;	//记录总共分解因子的次数
	
	@GuardedBy("this")
	private int cachedHits;		//记录缓存的命中次数
	
	/**
	 * 计算缓存命中率
	 */
	private synchronized double getHitRatio(){
		return (double)cachedHits / hits;
	}
	
	@Override
	public synchronized void service(ServletRequest req, ServletResponse resp)
			throws ServletException, IOException {
		BigInteger[] factors = null;
		BigInteger i = extractFromRequest(req);
		
		//使用内置锁保护状态
		synchronized (this) {
			++hits;
			//缓存命中
			if (i.equals(lastNumber)){
				++cachedHits;
				factors = lastFactors.clone();
			}
		}
		
		//缓存未命中
		if (null == factors){
			//将耗时的操作放在锁的外面执行
			factors = factor(i);
			
			//使用锁保持状态的一致性
			synchronized (this) {
				lastNumber = i;
				lastFactors = factors;
			}
		}
		
		encodeIntoResponse(factors, resp);
		
		System.out.println("缓存命中率: " + getHitRatio());
		
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

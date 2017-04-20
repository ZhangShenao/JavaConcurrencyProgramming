package practice;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * 
 * <p>Description:创建一个不可变类的缓存,保存上一次的数据。由于实例一旦创建就不可修改,因此无需进行同步操作就可保证原子性</p>
 * @author ZhangShenao
 * @date 2017年4月20日
 */
public class OneValueCache {
	private final BigInteger i;
	private final BigInteger[] factors;
	
	public OneValueCache(BigInteger i, BigInteger[] factors) {
		this.i = i;
		this.factors = factors;
	}
	
	/**
	 * 获取缓存中的因子
	 */
	public BigInteger[] getCachedFactors(BigInteger i){
		if (i.equals(this.i) && null != factors){
			return Arrays.copyOf(factors,factors.length);
		}
		return null;
	}
}

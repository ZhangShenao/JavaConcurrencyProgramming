package ch07;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 
 * <p>Description:原子更新数组</p>
 * @author ZhangShenao
 * @date 2017年6月7日
 */
public class AtomicIntegerArrayTest {
	private static int[] value = {1,2};
	
	//构造AtomicIntegerArray时,会将院数组复制一份,对内部数组修改时并不修改原数组
	private static AtomicIntegerArray arr = new AtomicIntegerArray(value);
	
	public static void main(String[] args) {
		arr.getAndSet(0, 3);
		System.out.println(arr.get(0));
		System.out.println(value[0]);
	}
}

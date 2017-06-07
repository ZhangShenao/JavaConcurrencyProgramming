package ch07;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 
 * <p>Description:原子更新引用类型</p>
 * @author ZhangShenao
 * @date 2017年6月7日
 */
public class AtomicReferenceTest {
	public static void main(String[] args) {
		//初始化一个引用
		AtomicReference<User> atomicReference = new AtomicReference<>(new User("Tom",20));
		System.out.println(atomicReference.get());
		
		//原子更新引用
		atomicReference.set(new User("Jerry",18));
		System.out.println(atomicReference.get());
	}
}

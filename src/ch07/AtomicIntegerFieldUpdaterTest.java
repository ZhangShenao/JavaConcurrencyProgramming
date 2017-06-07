package ch07;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 
 * <p>Description:原子更新字段类</p>
 * @author ZhangShenao
 * @date 2017年6月7日
 */
public class AtomicIntegerFieldUpdaterTest {
	public static void main(String[] args) {
		//创建原子更新器,并设置要更新的对象的所属类和属性,要更新的字段必须使用public volatile修饰
		AtomicIntegerFieldUpdater<User> updater = AtomicIntegerFieldUpdater.newUpdater(User.class, "age");
		
		//更新对象的字段
		User user = new User("Tom", 20);
		updater.getAndIncrement(user);
		
		//更新后,原对象的对应字段发生了改变
		System.out.println(updater.get(user));
		System.out.println(user);
	}
}

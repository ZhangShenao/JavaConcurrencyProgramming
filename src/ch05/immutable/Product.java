package ch05.immutable;

/**
 * 
 * <p>Description: 不变模式，一个对象一旦被创建，其内部状态将永远不可改变，在非同步操作下也可以保证线程安全</p>
 * @author ZhangShenao
 * @date 2017年4月5日
 */
public final class Product {	//使用final修饰，确保没有子类可以重写不可变类
	//使用private final修饰属性，防止外部对其修改
	private final String NO;
	private final String name;
	private final float price;
	
	//去除所有setter及修改内部属性的方法，只提供getter
	public String getNO() {
		return NO;
	}
	public String getName() {
		return name;
	}
	public float getPrice() {
		return price;
	}
	
	//提供一个可以创建完整对象的构造器
	public Product(String nO, String name, float price) {
		NO = nO;
		this.name = name;
		this.price = price;
	}
	
}

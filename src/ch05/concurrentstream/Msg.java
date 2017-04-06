package ch05.concurrentstream;

/**
 * 
 * <p>Description: 并行流水线中传递数据的载体</p>
 * @author ZhangShenao
 * @date 2017年4月6日
 */
public class Msg {
	private double i;
	private double j;
	private String exp;
	public double getI() {
		return i;
	}
	public void setI(double i) {
		this.i = i;
	}
	public double getJ() {
		return j;
	}
	public void setJ(double j) {
		this.j = j;
	}
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
	
	
}

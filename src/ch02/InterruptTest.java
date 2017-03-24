package ch02;

/**
 * 线程中断
 * @author ZhangShenao
 *
 */
public class InterruptTest {
	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(){
			public void run() {
				while (true){
					//如果当前线程中断标志位被置位，则退出线程
					if (isInterrupted()){
						System.err.println("Interrupted");
						break;
					}
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						System.err.println("Interrupted when sleep!!");
						
						//如果线程在sleep过程中被中断，则抛出InterruptedException,并清除中断标志位。
						//如果不加处理，在下一次循环开始时，就无法捕获这个中断，故此处再次设置中断标志位
						interrupt();
						
					}
				}
				
				Thread.yield();
			};
		};
		
		//启动线程
		t.start();
		Thread.sleep(2000);
		t.interrupt();
	}
	
}

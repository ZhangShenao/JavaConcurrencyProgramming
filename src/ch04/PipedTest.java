package ch04;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * 
 * <p>Description:管道输入/输出流,用于线程之间的数据传输,传输媒介为内存</p>
 * @author ZhangShenao
 * @date 2017年6月1日
 */
public class PipedTest {
	public static void main(String[] args) {
		PipedWriter out = null;
		PipedReader in = null;
		
		try {
			//创建管道输入/输出流
			out = new PipedWriter();
			in = new PipedReader();
			
			//将输入输出流进行连接,否则使用时会抛出IOException
			out.connect(in);
			
			Thread printThread = new Thread(new Printer(in), "Print-Thread");
			printThread.start();
			
			//获取控制台输入,并写入输出流
			int received = -1;
			while ((received = System.in.read()) != -1){
				out.write(received);
			}
		}catch (IOException e){
			e.printStackTrace();
		}finally {
			if (null != in){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//打印线程,将PipeReader中的内容打印到控制台
	private static class Printer implements Runnable{
		private PipedReader in;
		
		public Printer(PipedReader in) {
			this.in = in;
		}

		@Override
		public void run() {
			try {
				if (null != in){
					int received = -1;
					
					while ((received = in.read()) != -1){
						System.out.print((char)received);
					}
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		
	}
}

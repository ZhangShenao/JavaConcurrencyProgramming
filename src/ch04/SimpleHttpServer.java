package ch04;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * <p>Description:基于线程池技术的简单Web服务器</p>
 * @author ZhangShenao
 * @date 2017年6月3日
 */
public class SimpleHttpServer {
	/**
	 * 线程池
	 */
	private ThreadPool<HttpRequestHandler> threadPool;
	
	/**
	 * 服务器监听的端口号
	 */
	private int port = 8080;
	
	/**
	 * 服务器端ServerSocket
	 */
	private ServerSocket serverSocket;
	
	/**
	 * 服务器停止的标志
	 */
	private volatile boolean stop = false;
	
	/**
	 * SimpleHttpServer的根路径
	 */
	private static String basePath = "/";
	
	/**
	 * 服务器启动
	 * @param port 监听的端口号
	 * @param threadPoolSize 服务器线程池中的线程数
	 */
	public void start(int port,int threadPoolSize){
		try {
			//服务器启动
			setPort(port);
			serverSocket = new ServerSocket(this.port);
			
			//开启线程池
			threadPool = new DefaultThreadPool<HttpRequestHandler>(threadPoolSize);
			System.err.println("服务器启动,端口:" + this.port);
			
			//监听客户端请求
			Socket socket = null;
			while (!stop){
				try {
					//将客户端请求提交到线程池中执行
					socket = serverSocket.accept();
					if (null != socket){
						threadPool.execute(new HttpRequestHandler(socket));
					}
				}catch (Exception e){
					e.printStackTrace();
					if (null != socket){
						CloseUtil.closeAll(socket);
					}
				}
			}
			
			if (null != serverSocket){
				CloseUtil.closeAll(serverSocket);
			}
		}catch (Exception e){
			e.printStackTrace();
			
			shutdown();
		}
	}
	
	/**
	 * 关闭服务器
	 */
	public void shutdown(){
		stop = true;
	}
	
	/**
	 * 设置服务器端口
	 */
	private void setPort(int port){
		if (port > 0 && port < 65535){
			this.port = port;
		}
	}
	
	/**
	 * 设置服务器根路径
	 */
	public void setBasePath(String basePath){
		if (null != basePath){
			File file = new File(basePath);
			if (file.exists() && file.isDirectory()){
				SimpleHttpServer.basePath = basePath;
			}
		}
	}
	
	/**
	 * 
	 * <p>Description:解析Http请求的处理器</p>
	 * @author ZhangShenao
	 * @date 2017年6月3日
	 */
	private static class HttpRequestHandler implements Runnable{
		/**
		 * 与客户端通信的Socket
		 */
		private Socket socket;
		
		public HttpRequestHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			String line = null;
            BufferedReader br = null;
            BufferedReader reader = null;
            PrintWriter out = null;
            InputStream in = null;
            
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String header = reader.readLine();
                // 由相对路径计算出绝对路径
                String filePath = basePath + header.split(" ")[1];
                out = new PrintWriter(socket.getOutputStream());
                // 如果请求资源的后缀为jpg或者ico，则读取资源并输出
                if (filePath.endsWith("jpg") || filePath.endsWith("ico")) {
                    in = new FileInputStream(filePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int i = 0;
                    while ((i = in.read()) != -1) {
                        baos.write(i);
                    }

                    byte[] array = baos.toByteArray();
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: image/jpeg");
                    out.println("Content-Length: " + array.length);
                    out.println("");
                    socket.getOutputStream().write(array, 0, array.length);
                } else {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
                    out = new PrintWriter(socket.getOutputStream());
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: text/html; charset=UTF-8");
                    out.println("");
                    while ((line = br.readLine()) != null) {
                        out.println(line);
                    }
                }
                out.flush();
			}catch (Exception e){
				e.printStackTrace();
				out.println("HTTP/1.1 500");
                out.println("");
                out.flush();
			}finally {
				if (null != socket){
					CloseUtil.closeAll(socket);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		SimpleHttpServer server = new SimpleHttpServer();
		server.start(8080, 10);
	}
}

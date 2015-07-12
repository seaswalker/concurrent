package chapter6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * P95 6-2
 * 为每一个请求单独创建一个请求
 * 缺点在于创建线程的成本
 * @author skywalker
 *
 */
public class ThreadPerTaskServer {

	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(8080);
		Thread thread = null;
		while (true) {
			final Socket socket = server.accept();
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					handleRequest(socket);
				}
			});
			thread.start();
		}
	}
	
	private static void handleRequest(Socket socket) {
		
	}
	
}

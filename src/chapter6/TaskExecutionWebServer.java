package chapter6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * P97 6-4 使用线程池
 * @author skywalker
 *
 */
public class TaskExecutionWebServer {
	
	private static final int THREADS = 100;
	private static final Executor EXECUTOR = Executors.newFixedThreadPool(THREADS);

	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(8080);
		while (true) {
			final Socket socket = server.accept();
			Runnable task = new Thread(new Runnable() {
				@Override
				public void run() {
					handleRequest(socket);
				}
			});
			//线程是不变的，线程执行的任务是可变的
			EXECUTOR.execute(task);
		}
	}
	
	private static void handleRequest(Socket socket) {
		
	}
	
}

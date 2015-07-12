package chapter6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * P100 6-8
 * 为服务器加上生命周期管理
 * @author skywalker
 *
 */
public class LifecycleWebServer {

	private static final int THREADS = 100;
	private final ExecutorService service = Executors.newFixedThreadPool(THREADS);
	
	/**
	 * 启动服务器
	 */
	public void start() throws IOException {
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(80);
		while (!service.isShutdown()) {
			try {
				final Socket socket = server.accept();
				service.execute(new Runnable() {
					@Override
					public void run() {
						handleRequest(socket);
					}
				});
			} catch (RejectedExecutionException e) {
				if (!service.isShutdown()) {
					//记录日志
				}
			}
		}
	}
	
	public void stop() {
		service.shutdown();
	}
	
	private void handleRequest(Socket connection) {
		if (isShutDownOrder(connection)) {
			stop();
		} else {
			dispather(connection);
		}
	}

	//实际处理请求
	private void dispather(Socket connection) {
	}

	/**
	 * 判断是否是关闭服务器命令
	 */
	private boolean isShutDownOrder(Socket connection) {
		return false;
	}
	
}

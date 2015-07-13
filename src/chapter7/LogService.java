package chapter7;

import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * P126 7-15 最终版日志服务
 * @author skywalker
 *
 */
public class LogService {

	private final BlockingQueue<String> queue;
	private final PrintWriter writer;
	private final LogThread logThread;
	//是否关闭
	private boolean isShutDown = false;
	//记录队列中消息的个数
	private int reversation = 0;
	
	public LogService(PrintWriter writer) {
		this.queue = new ArrayBlockingQueue<String>(100);
		this.writer = writer;
		this.logThread = new LogThread();
	}
	
	public void start() {
		logThread.start();
	}
	
	public void stop() {
		synchronized (this) {
			isShutDown = true;
		}
		//这个是多余的?
		logThread.interrupt();
	}
	
	public void log(String message) throws InterruptedException {
		synchronized (this) {
			if (isShutDown) {
				throw new IllegalStateException("The logger is shutdown");
			}
			++ reversation;
		}
		queue.put(message);
	}
	
	//日志线程
	private class LogThread extends Thread {
	
		@Override
		public void run() {
			try {
				while (true) {
					try {
						synchronized (LogService.this) {
							if (isShutDown && reversation == 0) {
								break;
							}
						}
						writer.println(queue.take());
						synchronized (LogService.this) {
							-- reversation;
						}
					} catch (InterruptedException e) {
						//忽略，因为需要继续处理队列中剩余的日志，不能马上中断
					}
				}
			} finally {
				writer.close();
			}
		}
		
	}
	
}

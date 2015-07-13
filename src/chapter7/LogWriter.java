package chapter7;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * P125 7-13 不支持关闭的生产者-消费者日志服务
 * 此处的不支持关闭应该是这个意思:
 * 消费者可以关闭(通过IntteruptedException)，但是生产者不知道消费者关闭了，
 * 还是向BlockingQueue中添加消息，从而导致所有的生产者都会阻塞
 * @author skywalker
 *
 */
public class LogWriter {

	private final BlockingQueue<String> queue;
	private final LogThread logThread;
	
	public LogWriter(Writer writer) {
		this.queue = new ArrayBlockingQueue<String>(100);
		this.logThread = new LogThread(writer);
	}
	
	public void start() {
		logThread.start();
	}
	
	/**
	 * 日志记录
	 */
	public void log(String messgae) throws InterruptedException {
		queue.put(messgae);
	}
	
	//日志线程
	private class LogThread extends Thread {
		
		private final Writer writer;
		
		public LogThread(Writer writer) {
			this.writer = writer;
		}
		
		@Override
		public void run() {
			try {
				while (true) {
					writer.write(queue.take());
				}
			} catch (IOException | InterruptedException e) {
				//消费者退出
				e.printStackTrace();
			} finally {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}

package chapter5;

import java.io.File;
import java.util.concurrent.BlockingQueue;

/**
 * 为文件建立索引
 * P75 5-8
 * @author skywalker
 *
 */
public class Indexer implements Runnable {
	
	private final BlockingQueue<File> queue;
	
	public Indexer(BlockingQueue<File> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			while(true) {
				indexFile(queue.take());
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * 建立索引
	 */
	private void indexFile(File file) {
		
	}

}

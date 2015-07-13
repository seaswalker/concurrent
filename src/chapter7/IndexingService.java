package chapter7;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * P128 7-17 通过"毒丸"对象关闭生产者-消费者服务
 * 要求几个条件:
 * 	a) 生产者消费者的数量是已知的
 *  b) 必须是无界队列
 * @author skywalker
 *
 */
public class IndexingService {

	private final BlockingQueue<File> files = new LinkedBlockingQueue<File>();
	//毒丸对象，只有一个
	private static final File POISON = new File("");
	private final File root;
	private final CrawlerThread producer = new CrawlerThread();
	private final IndexerThread consumer = new IndexerThread();
	
	public void start() {
		producer.start();
		consumer.start();
	}
	
	public void stop() {
		//中断生产者，生产者放入毒丸
		producer.interrupt();
	}
	
	public IndexingService(File root) {
		this.root = root;
	}
	
	//生产者线程
	private class CrawlerThread extends Thread {
		@Override
		public void run() {
			try {
				//处理根节点
				crawler(root);
			} finally {
				//加入毒丸对象
				while (true) {
					try {
						files.put(POISON);
						break;
					} catch (InterruptedException e) {
						//重新尝试
					}
				}
			}
			
		}

		private void crawler(File root) {}
	}
	
	//消费者线程
	private class IndexerThread extends Thread {
		@Override
		public void run() {
			try {
				while (true) {
					File file = files.take();
					if (file == POISON) {
						break;
					} else {
						//建立索引
						index(file);
					}
				}
			} catch (InterruptedException e) {
				//退出
			}
		}

		private void index(File file) {}
	}
	
}

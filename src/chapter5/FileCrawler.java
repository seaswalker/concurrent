package chapter5;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;

/**
 * 文件爬虫，把每一个文件加入阻塞队列
 * P75 5-8
 * @author skywalker
 *
 */
public class FileCrawler implements Runnable {

	private final BlockingQueue<File> queue;
	private final FileFilter filter;
	//根目录
	private final File root;
	
	public FileCrawler(BlockingQueue<File> queue, FileFilter filter, File root) {
		this.queue = queue;
		this.filter = filter;
		this.root = root;
	}

	@Override
	public void run() {
		try {
			craml(root);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * 搜索一个根路径
	 */
	private void craml(File root) throws InterruptedException {
		if(root != null && root.isDirectory()) {
			File[] files = root.listFiles(filter);
			for(File file : files) {
				if(file.isDirectory()) {
					craml(file);
				}else {
					queue.put(file);
				}
			}
		}
	}
	
}

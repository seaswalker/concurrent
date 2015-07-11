package chapter5;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 文件索引客户端
 * 这就是一个简单的生产者消费者的例子
 * @author skywalker
 *
 */
public class FileIndexClient {

	public static void main(String[] args) {
		File root = new File("XXXX");
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return true;
			}
		};
		BlockingQueue<File> queue = new ArrayBlockingQueue<File>(16);
		//5个线程遍历路径
		for(int i = 0;i < 5;i ++) {
			new Thread(new FileCrawler(queue, filter, root)).start();;
		}
		//5个线程建立索引
		for(int i = 0;i < 5;i ++) {
			new Thread(new Indexer(queue)).start();
		}
	}
	
}

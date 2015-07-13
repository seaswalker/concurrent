package chapter7;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * P113 7-3 验证标志量退出法不适用于阻塞的情况
 * 解决方法很简单，使用中断就行了
 * @author skywalker
 *
 */
public class BlockFlagExit {
	
	public static void main(String[] args) throws InterruptedException {
		final BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10);
		//生产者线程
		Producer producer = new Producer(queue);
		Thread producerThread = new Thread(producer);
		//消费者线程
		Thread consumerThread = new Thread(new Consumer(queue));
		producerThread.start();
		consumerThread.start();
		//运行10ms结束线程
		TimeUnit.MILLISECONDS.sleep(10);
		consumerThread.interrupt();
		producer.cancell();
	}

	/**
	 * 生产者
	 * @author skywalker
	 *
	 */
	private static class Producer implements Runnable {
		private final BlockingQueue<Integer> queue;
		private volatile boolean cancelled = false;
		private final Random random = new Random();
		
		public Producer(BlockingQueue<Integer> queue) {
			this.queue = queue;
		}
		
		public void cancell() {
			cancelled = true;
		}

		@Override
		public void run() {
			while (!cancelled) {
				try {
					queue.put(random.nextInt(10));
					System.out.println("队列已有" + queue.size() + "个");
				} catch (InterruptedException e) {
				}
			}
		}
		
	}
	
	/**
	 * 消费者
	 * @author skywalker
	 *
	 */
	private static class Consumer implements Runnable {
		
		private final BlockingQueue<Integer> queue;
		private Integer i;
		
		public Consumer(BlockingQueue<Integer> queue) {
			this.queue = queue;
		}
		
		@Override
		public void run() {
			try {
				while (true) {
					TimeUnit.MILLISECONDS.sleep(2);
					i = queue.take();
					System.out.println(i + "被使用");
				}
			} catch (InterruptedException e) {
				System.out.println("消费者退出...");
			}
		}
		
	}
	
}

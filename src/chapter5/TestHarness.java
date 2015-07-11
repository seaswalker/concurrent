package chapter5;

import java.util.concurrent.CountDownLatch;

/**
 * P79 5-11
 * 使用CountDownLatch来启动和停止线程
 * @author skywalker
 *
 */
public class TestHarness {

	/**
	 * 启动-结束门
	 * @param threads 线程数量
	 * @param task 线程运行的任务
	 */
	public static void timeTasks(int threads, final Runnable task) throws InterruptedException {
		//启动门
		final CountDownLatch beginGate = new CountDownLatch(1);
		//结束门
		final CountDownLatch endGate = new CountDownLatch(threads);
		Thread thread = null;
		for(int i = 0;i < threads;i ++) {
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						//等待启动门
						beginGate.await();
						//执行任务
						task.run();
						//结束门减一
						endGate.countDown();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			});
			thread.start();
		}
		long beginTime = System.nanoTime();
		//全部线程启动
		beginGate.countDown();
		//等待所有线程结束
		endGate.await();
		long endTime = System.nanoTime();
		System.out.println("耗时:" + (endTime - beginTime));
	}
	
}

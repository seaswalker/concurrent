package chapter8;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

/**
 * P145 8-4
 * 使用信号量控制任务的提交速率
 * 这个的意思是达到界限后execute就会阻塞，而默认的策略无法实现此功能
 * @author skywalker
 *
 */
public class BoundedExecutor {

	private final Executor executor;
	private final Semaphore semaphore;
	
	public BoundedExecutor(Executor executor, int bound) {
		this.executor = executor;
		this.semaphore = new Semaphore(bound);
	}
	
	/**
	 * 提交任务
	 * 仔细琢磨下两个try块的位置
	 * @param runnable
	 * @throws InterruptedException 
	 */
	public void submitTask(final Runnable runnable) throws InterruptedException {
		semaphore.acquire();
		try {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						runnable.run();
					} finally {
						semaphore.release();
					}
				}
			});
		} catch (RejectedExecutionException e) {
			semaphore.release();
		}
	}
	
}

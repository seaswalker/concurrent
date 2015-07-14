package chapter8;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * P149 8-9 自定义ThreadPoolExecutor，添加即时统计
 * @author skywalker
 *
 */
public class TimingThreadPool extends ThreadPoolExecutor {
	
	//注意默认的级别是INFO，低于这个级别的日志不显示
	private static final Logger logger = Logger.getLogger("TimingPoolExecutor");
	//记录一个线程的开始时间，以便在afterExecution()中使用
	private static ThreadLocal<Long> startTime = new ThreadLocal<Long>();
	//运行的任务数量
	private static final AtomicInteger taskNum = new AtomicInteger();
	//所有任务总的运行时间
	private static final AtomicLong totalTime = new AtomicLong();

	public TimingThreadPool(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		logger.log(Level.INFO, "Thread " + t.getName() + " start");
		startTime.set(System.nanoTime());
	}
	
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		try {
			long endTime = System.nanoTime();
			long taskTime = endTime - startTime.get();
			logger.log(Level.INFO, String.format("Thread %s: end %s, time=%dns", t, r, taskTime));
			taskNum.getAndIncrement();
			totalTime.addAndGet(taskTime);
		} finally {
			super.afterExecute(r, t);
		}
	}
	
	@Override
	protected void terminated() {
		try {
			logger.info(String.format("Service terminted, 运行了 %d 个任务, 共耗时 %dns", taskNum.get(), totalTime.get()));
		} finally {
			super.terminated();
		}
	}
	
	public static void main(String[] args) {
		ExecutorService service = new TimingThreadPool(0, 20, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20));
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		};
		service.submit(task);
		service.submit(task);
		service.submit(task);
		service.shutdown();
	}

}

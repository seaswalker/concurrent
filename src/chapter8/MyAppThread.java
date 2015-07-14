package chapter8;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * P146 8-7 自定义线程
 * @author skywalker
 *
 */
public class MyAppThread extends Thread {

	public static final String DEFAULTNAME = "MyAppThread";
	private static volatile boolean debugLifeCycle = false;
	//被创建的线程的数量
	private static final AtomicInteger created = new AtomicInteger();
	//活着的线程的数量
	private static final AtomicInteger alive = new AtomicInteger();
	private static final Logger logger = Logger.getAnonymousLogger();
	
	public MyAppThread(Runnable runnable) {
		this(runnable, DEFAULTNAME);
	}
	
	public MyAppThread(Runnable runnable, String name) {
		super(runnable, name);
		created.getAndIncrement();
		setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				logger.log(Level.SEVERE, "UNCAUGHT in thread " + getName(), e);
			}
		});
	}
	
	@Override
	public void run() {
		//复制一份，保持一致性，这个比较吊
		boolean debug = debugLifeCycle;
		if (debug) {
			logger.log(Level.FINE, getName() + "created");
		}
		try {
			alive.getAndIncrement();
			super.run();
		} finally {
			alive.getAndDecrement();
			if (debug) {
				logger.log(Level.FINE, getName() + " exited");
			}
		}
	}
	
}

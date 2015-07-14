package chapter8;

import java.util.concurrent.ThreadFactory;

/**
 * P146 8-6 自定义线程工厂
 * @author skywalker
 *
 */
public class MyThreadFactory implements ThreadFactory {

	@Override
	public Thread newThread(Runnable r) {
		return new MyAppThread(r);
	}

}

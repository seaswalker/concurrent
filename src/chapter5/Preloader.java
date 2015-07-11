package chapter5;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * P81 5-12
 * 使用FutureTask预加载
 * @author skywalker
 *
 */
public class Preloader {

	private final FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
		@Override
		public String call() throws Exception {
			return "我来自未来";
		}
	});
	
	public void start() {
		//因为FutureTask实现了RunnableFuture接口，而此接口继承自Runnable和Future
		new Thread(future).start();
	}
	
	public String get() throws InterruptedException, ExecutionException {
		return future.get();
	}
	
}

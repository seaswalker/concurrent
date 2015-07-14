package chapter8;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * P145 8-3 修改队列饱和策略
 * @author skywalker
 *
 */
public class Policy {

	public static void main(String[] args) {
		//注意此处不可使用其父类来引用
		ThreadPoolExecutor executor = 
				new ThreadPoolExecutor(1, 10, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
		//调用者运行策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	}
	
}

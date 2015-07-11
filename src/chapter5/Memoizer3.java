package chapter5;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * P88 5-18 高效可伸缩结果缓存第三个版本
 * 此版本主要解决了第二个线程不知道第一个线程正在计算的问题
 * @author skywalker
 *
 * @param <V>
 * @param <A>
 */
public class Memoizer3<V, A> implements Computable<V, A> {
	
	//这里缓存的值类型是关键
	private final ConcurrentMap<A, Future<V>> cache = new ConcurrentHashMap<A, Future<V>>();
	private final Computable<V, A> computable;
	
	public Memoizer3(Computable<V, A> computable) {
		this.computable = computable;
	}

	/**
	 * 还是有一个问题:
	 * 先检查后执行的问题仍然没有解决
	 */
	@Override
	public V compute(A arg) {
		Future<V> future = cache.get(arg);
		if(future == null) {
			FutureTask<V> futureTask = new FutureTask<V>(new Callable<V>() {
				@Override
				public V call() throws Exception {
					return computable.compute(arg);
				}
			});
			//FutureTask实现了Future接口
			cache.put(arg, futureTask);
			future = futureTask;
			//开始计算
			futureTask.run();
		}
		try {
			//如果有一个线程在计算，那么阻塞等待
			return future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

}

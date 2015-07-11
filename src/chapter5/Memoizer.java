package chapter5;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import annotations.ThreadSafe;

/**
 * P89 5-19 高效可伸缩结果缓存最终版本
 * @author skywalker
 *
 * @param <V>
 * @param <A>
 */
@ThreadSafe
public class Memoizer<V, A> implements Computable<V, A>{
	
	private final ConcurrentMap<A, Future<V>> cache = new ConcurrentHashMap<A, Future<V>>();
	private final Computable<V, A> computable;
	
	public Memoizer(Computable<V, A> computable) {
		this.computable = computable;
	}

	@Override
	public V compute(A arg) {
		//此循环的目的应该是抛出异常后继续尝试
		while(true) {
			Future<V> future = cache.get(arg);
			if(future == null) {
				FutureTask<V> futureTask = new FutureTask<V>(new Callable<V>() {
					@Override
					public V call() throws Exception {
						return computable.compute(arg);
					}
				});
				//关键:使用原子的操作
				//其实这就是双重检查的意思
				future = cache.putIfAbsent(arg, futureTask);
				if(future == null) {
					//如果之前没有值，那么返回null(即old value)
					future = futureTask;
					futureTask.run();
				}
			}
			try {
				return future.get();
			} catch (CancellationException e) {
				//如果计算被取消，应该从缓存中移除
				cache.remove(arg, future);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

}

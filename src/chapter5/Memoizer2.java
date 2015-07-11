package chapter5;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * P87 5-17高效可伸缩结果缓存第二个版本
 * 改用ConcurrentHashMap实现
 * @author skywalker
 *
 * @param <V> 结果类型
 * @param <A> 参数类型
 */
public class Memoizer2<V, A> implements Computable<V, A> {
	
	private final ConcurrentMap<A, V> cache = new ConcurrentHashMap<A, V>();
	private final Computable<V, A> computable;
	
	public Memoizer2(Computable<V, A> computable) {
		this.computable = computable;
	}

	/**
	 * 此版本相较于第一个版本，去掉了同步，提高了性能，但是还是会出现重复计算的问题，原因:
	 * a) 如果计算的时间比较长，有一个线程正在计算，但是第二个线程不知道有一个线程正在计算，所以还是会计算
	 * b) 这是典型的先检查后执行的代码，你懂的 
	 */
	@Override
	public V compute(A arg) {
		V result = cache.get(arg);
		if(result == null) {
			result = computable.compute(arg);
			cache.put(arg, result);
		}
		return result;
	}

}

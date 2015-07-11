package chapter5;

import java.util.HashMap;
import java.util.Map;

import annotations.ThreadSafe;

/**
 * P86 5-16高效可伸缩结果缓存第一个版本
 * @author skywalker
 *
 */
@ThreadSafe
public class Memoizer1<V, A> implements Computable<V, A> {

	private final Map<A, V> cache = new HashMap<A, V>();
	//可以计算的任务，如果缓存中没有，那么会被调用
	private final Computable<V, A> computable;
	
	public Memoizer1(Computable<V, A> computable) {
		this.computable = computable;
	}

	/**
	 * 最简单粗暴的线程安全
	 * 如果计算所需的时间很长，性能甚至不如不加缓存
	 */
	@Override
	public synchronized V compute(A arg) {
		V result = cache.get(arg);
		if(result == null) {
			result = computable.compute(arg);
			cache.put(arg, result);
		}
		return result;
	}

}

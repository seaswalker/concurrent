package chapter5;

/**
 * P86 5-16 可以被计算的任务
 * @author skywalker
 *
 */
public interface Computable<V, A> {

	public V compute(A arg);
	
}

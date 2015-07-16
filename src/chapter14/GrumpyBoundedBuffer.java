package chapter14;

import annotations.ThreadSafe;

/**
 * P240 14-3 当不满足条件时，不会执行相应的操作
 * 这样做的缺点就是调用者须自行处理条件失败的情况
 * @author skywalker
 *
 */
@ThreadSafe
public class GrumpyBoundedBuffer<V> extends BaseBoundedBuffer<V> {

	public GrumpyBoundedBuffer(int capacity) {
		super(capacity);
	}
	
	public synchronized void put(V value) throws BufferFullException {
		if (isFull()) {
			throw new BufferFullException();
		}
		doPut(value);
	}
	
	public synchronized V get() throws BufferEmptyException {
		if (isEmpty()) {
			throw new BufferEmptyException();
		}
		return doGet();
	}
	
	/**
	 * 下面是调用的示例:
	 */
	public static void main(String[] args) {
		GrumpyBoundedBuffer<String> buffer = new GrumpyBoundedBuffer<String>(10);
		while (true) {
			try {
				buffer.get();
				//干点坏事儿
				break;
			} catch (BufferEmptyException e) {
				//此处有三种选择：
				//a) 啥也不干，直接重试，这叫自旋，会导致CPU时钟浪费
				//b) 睡一会儿，会导致低响应性
				//c) Thread.yeild()，缺点就是JVM吊不吊这个方法就不知道了
			}
		}
	}
	
}

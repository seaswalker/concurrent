package chapter14;

import annotations.ThreadSafe;

/**
 * P239 14-2 循环有界缓存的基类
 * @author skywalker
 *
 */
@ThreadSafe
public abstract class BaseBoundedBuffer<V> {

	private final V[] buf;
	//头、尾指针，默认都是0，因为表示的是实际元素的位置
	private int head = 0;
	private int tail = 0;
	//元素的个数
	private int count = 0;
	
	@SuppressWarnings("unchecked")
	public BaseBoundedBuffer(int capacity) {
		this.buf = (V[]) new Object[capacity];
	}
	
	public synchronized final void doPut(V value) {
		buf[tail] = value;
		if (++ tail == buf.length) {
			tail = 0;
		}
		count ++;
	}
	
	public synchronized final V doGet() {
		V value = buf[head];
		//缓存中不要再持有元素的引用
		buf[head] = null;
		if (++ head == buf.length) {
			head = 0;
		}
		-- count;
		return value;
	}
	
	public synchronized boolean isEmpty() {
		return count == 0;
	}
	
	public synchronized boolean isFull() {
		return count == buf.length;
	}
	
}

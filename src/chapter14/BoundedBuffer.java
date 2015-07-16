package chapter14;

/**
 * P243 14-6 使用条件队列实现的有界缓存--终极版
 * "柳暗花明又一村"的既视感
 * 其实还可以改进，就在于notifyAll()方法，不应该唤醒所有线程
 * @author skywalker
 *
 */
public class BoundedBuffer<V> extends BaseBoundedBuffer<V> {

	public BoundedBuffer(int capacity) {
		super(capacity);
	}
	
	public synchronized void put(V value) throws InterruptedException {
		while (isFull()) {
			wait();
		}
		doPut(value);
		notifyAll();
	}
	
	public synchronized V get() throws InterruptedException {
		while (isEmpty()) {
			wait();
		}
		V value = doGet();
		notifyAll();
		return value;
	}

}

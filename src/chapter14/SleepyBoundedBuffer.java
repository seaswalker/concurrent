package chapter14;

import java.util.concurrent.TimeUnit;

/**
 * P241 14-5 使用简单组阻塞实现有界缓存
 * 这下不用调用者自己处理了
 * @author skywalker
 *
 */
public class SleepyBoundedBuffer<V> extends BaseBoundedBuffer<V> {

	public SleepyBoundedBuffer(int capacity) {
		super(capacity);
	}
	
	public void put(V value) throws InterruptedException {
		while (true) {
			synchronized (this) {
				if (!isFull()) {
					doPut(value);
					return;
				}
			}
			TimeUnit.SECONDS.sleep(1);
		}
	}
	
	public V get() throws InterruptedException {
		while (true) {
			synchronized (this) {
				if (!isEmpty()) {
					return doGet();
				}
			}
			TimeUnit.SECONDS.sleep(1);
		}
	}

}

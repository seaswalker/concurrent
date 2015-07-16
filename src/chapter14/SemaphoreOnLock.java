package chapter14;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * P253 14-12 使用ReentrantLock实现信号量
 * @author skywalker
 *
 */
public class SemaphoreOnLock {

	private final ReentrantLock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();
	//许可数量
	private int permits;
	
	public SemaphoreOnLock(int initialPermits) {
		this.permits = initialPermits;
	}
	
	public void acquire() throws InterruptedException {
		lock.lock();
		try {
			while (permits < 1) {
				condition.await();
			}
			permits --;
		} finally {
			lock.unlock();
		}
	}
	
	public void release() {
		lock.lock();
		try {
			permits ++;
			condition.signal();
		} finally {
			lock.unlock();
		}
	}
	
}

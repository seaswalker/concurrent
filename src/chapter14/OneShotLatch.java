package chapter14;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

import annotations.ThreadSafe;

/**
 * 使用AbstractQueuedSynchronizer实现简单的二元闭锁
 * 就相当于一道阀门
 * @author skywalker
 *
 */
@ThreadSafe
public class OneShotLatch {
	
	private final Sync sync = new Sync();
	
	public void await() throws InterruptedException {
		sync.acquireInterruptibly(0);
	}
	
	public void signal() {
		sync.releaseShared(0);
	}

	/**
	 * java.util.concurrent的构件也是使用组而不是继承的方式来实现的
	 */
	@SuppressWarnings("serial")
	private class Sync extends AbstractQueuedSynchronizer {
		@Override
		protected int tryAcquireShared(int arg) {
			return getState() == 1 ? 1 : -1;
		}
		@Override
		protected boolean tryReleaseShared(int arg) {
			//打开闭锁
			setState(1);
			//现在其他线程可以获取闭锁
			return true;
		}
	}
	
}

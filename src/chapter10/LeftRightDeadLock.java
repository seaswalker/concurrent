package chapter10;

/**
 * P171 10-1 简单的死锁(可能)
 * @author skywalker
 *
 */
public class LeftRightDeadLock {

	private static final Object left = new Object();
	private static final Object right = new Object();
	
	public static void leftRight() {
		synchronized (left) {
			synchronized (right) {
				//干点坏事儿
			}
		}
	}
	
	public static void rightLeft() {
		synchronized (right) {
			synchronized (left) {
				//干点坏事儿
			}
		}
	}
	
	public static void main(String[] args) {
		Thread left = new Thread() {
			@Override
			public void run() {
				leftRight();
			}
		};
		Thread right = new Thread() {
			@Override
			public void run() {
				rightLeft();
			}
		};
		left.start();
		right.start();
	}
	
}

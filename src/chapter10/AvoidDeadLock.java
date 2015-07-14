package chapter10;

/**
 * P172 10-3 使用固定的顺序以避免死锁
 * 如果有两个线程同时以相反的顺序转移前，那么可能会死锁
 * @author skywalker
 *
 */
public class AvoidDeadLock {
	
	//"加时赛"，如果两个对象的的由地址计算而来的hashCode是一致的，那么使用这个锁
	private static final Object tieLock = new Object();
	
	/**
	 * 把money从from转移到to
	 */
	public void transfer(final Account from, final Account to, int money) {
		
		/**
		 * 局部类
		 * 这个类真是太机智了，还有其它的方法可以实现此种功能吗?
		 * @author skywalker
		 *
		 */
		class Helper {
			public void transfer() {
				if (from.getBalance() < money) {
					throw new IllegalArgumentException();
				}
				from.debit(money);
				to.credit(money);
			}
		}
		
		//计算两个账户的默认hashCode
		int fromHash = System.identityHashCode(from);
		int toHash = System.identityHashCode(to);
		//总是先获取hash码小的的锁
		if (fromHash < toHash) {
			synchronized (from) {
				synchronized (to) {
					new Helper().transfer();
				}
			}
		} else if (fromHash > toHash) {
			synchronized (to) {
				synchronized (from) {
					new Helper().transfer();
				}
			}
		} else {
			synchronized (tieLock) {
				synchronized (from) {
					synchronized (to) {
						new Helper().transfer();
					}
				}
			}
		}
	}

	//账户
	private static class Account {
		
		//获取余额
		public synchronized int getBalance() {
			return 10;
		}
		
		//取款
		public synchronized void debit(int money) {}
		
		//存款
		public synchronized void credit(int money) {}
		
	}
	
}

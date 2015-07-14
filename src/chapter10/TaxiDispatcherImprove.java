package chapter10;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import annotations.NotDeadLock;

/**
 * P176 10-6 使用开放调用的方式解决死锁
 * @author skywalker
 *
 */
@NotDeadLock
public class TaxiDispatcherImprove {

	private final Set<TaxiImproved> taxis;
	private final Set<TaxiImproved> availableTaxis;
	
	public TaxiDispatcherImprove() {
		this.taxis = new HashSet<TaxiImproved>();
		this.availableTaxis = new HashSet<TaxiImproved>();
	}
	
	/**
	 * 唤醒可用的出租车
	 */
	public synchronized void notifyIfAvailable(TaxiImproved taxi) {
		availableTaxis.add(taxi);
	}
	
	/**
	 * 改为开放调用
	 * 绘制出租车位置图
	 * 这样处理后其实此方法的语义发生了变化，之前那个版本是绘制一个快照而此版本是实时变化的
	 * 前提是可以接受这样的变化
	 */
	public void getIamge() {
		Set<TaxiImproved> copy;
		synchronized (this) {
			copy = new HashSet<TaxiImproved>(taxis);
		}
		for (TaxiImproved taxi : copy) {
			taxi.getLocation();
		}
	}
	
}

class TaxiImproved {
	
	private Point location, destination;
	private final TaxiDispatcherImprove dispatcher;
	
	public TaxiImproved(TaxiDispatcherImprove dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	public synchronized Point getLocation() {
		return location;
	}
	
	/**
	 * 改为开放调用
	 * 这样做的前提是修改位置和通知dispatcher可用不需要是原子操作
	 */
	public void setLocation(Point location) {
		boolean reachDestination = false;
		synchronized (this) {
			this.location = location;
			reachDestination = location.equals(destination);
		}
		if (reachDestination) {
			dispatcher.notifyIfAvailable(this);
		}
	}
  	
}

package chapter4;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import annotations.ThreadSafe;

/**
 * 线程安全性委托实现的车辆追踪器
 * 因为两个组件locations和里面的组件都是线程安全的，所以整个类也是线程安全的
 * @author skywalker
 *
 */
@ThreadSafe
public class DelegatingVehicleTracker {

	private final ConcurrentMap<String, Point> locations;
	//位置的一个不可变视图
	private final Map<String, Point> unmodifiableMap;
	
	public DelegatingVehicleTracker(Map<String, Point> locations) {
		this.locations = new ConcurrentHashMap<String, Point>(locations);
		this.unmodifiableMap = Collections.unmodifiableMap(this.locations);
	}
	
	public Point getLocation(String id) {
		return locations.get(id);
	}
	
	/**
	 * 返回的是可以根据locations实时变化的视图
	 */
	public Map<String, Point> getLocations() {
		return unmodifiableMap;
	}
	
	public void setLocation(String id, int x, int y) {
		if(locations.replace(id, new Point(x, y)) == null) {
			throw new IllegalArgumentException("not exists");
		}
	} 
	
}

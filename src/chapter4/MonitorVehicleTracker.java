package chapter4;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import annotations.ThreadSafe;

/**
 * 52页基于监视器模式的车辆追踪
 * @author skywalker
 *
 */
@ThreadSafe
public class MonitorVehicleTracker {

	//此变量不可以溢出
	private Map<String, MutablePoint> locations;
	
	public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
		this.locations = deepCopy(locations);
	}
	
	public synchronized Map<String, MutablePoint> getLocations() {
		return deepCopy(locations);
	}
	
	public synchronized MutablePoint getLocation(String id) {
		MutablePoint point = locations.get(id);
		return point == null ? null : new MutablePoint(point);
	}
	
	public synchronized void setLocation(String id, int x, int y) {
		MutablePoint point = locations.get(id);
		if(point == null) {
			throw new IllegalArgumentException("The id is not exists");
		}
		point.x = x;
		point.y = y;
	}

	/**
	 * 深度拷贝map
	 */
	private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> locations) {
		Map<String, MutablePoint> result = new HashMap<String, MutablePoint>();
		for(String id : locations.keySet()) {
			result.put(id, new MutablePoint(locations.get(id)));
		}
		return Collections.unmodifiableMap(result);
	}
	
}

package chapter4;

/**
 * 可变类，类似于java.awt.Point
 * @author skywalker
 *
 */
public class MutablePoint {

	public int x, y;
	
	public MutablePoint(MutablePoint m) {
		this.x = m.x;
		this.y = m.y;
	}
	
}

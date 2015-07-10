package chapter4;

import java.util.List;

/**
 * 4.4.2组合
 * 为普通的list添加一个原子的如果不存在则添加的操作
 * 使用组合的方式线程安全的扩展一个类，这也是最好的方式
 * @author skywalker
 *
 */
public class ImprovedList<T> {

	//这其实也就是java的监视器模式，必须保证此类拥有list的唯一引用
	private final List<T> list;
	
	public ImprovedList(List<T> list) {
		this.list = list;
	}
	
	public synchronized void addIfAbsent(T t) {
		if(!list.contains(t)) {
			list.add(t);
		}
	}
	
}

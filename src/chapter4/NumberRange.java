package chapter4;

import java.util.concurrent.atomic.AtomicInteger;

import annotations.ThreadNotSafe;

/**
 * 4.3.3当委托失效时
 * 失效的原因就是两个线程安全的变量之间有了依赖关系
 * 解决方法就是加锁
 * @author skywalker
 *
 */
@ThreadNotSafe
public class NumberRange {

	//约束条件是upper > lower
	private final AtomicInteger upper = new AtomicInteger();
	private final AtomicInteger lower = new AtomicInteger();
	
	public void setUpper(int i) {
		//先检查后执行，错误的来源
		if(i > lower.get()) {
			upper.set(i);
		}
	}
	
	public void setLower(int i) {
		//错误原因同上
		if(i < upper.get()) {
			lower.set(i);
		}
	}
	
}

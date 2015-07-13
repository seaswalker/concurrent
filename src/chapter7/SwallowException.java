package chapter7;

import java.util.concurrent.TimeUnit;

/**
 * 仿照P119 7-8的思路
 * 在另一个线程中运行貌似会吞掉RuntimeException
 * 工作线程中抛出RuntimeException控制台还是会打印出堆栈信息(貌似是废话...)
 * 但书中的"吞掉"应该指的是无法被捕获得问题
 * 这种解决方式有点喧宾夺主的感觉
 * @author skywalker
 *
 */
public class SwallowException {

	//运行结果:ArithmeticException
	public static void main(String[] args) {
		Thread workThread = Thread.currentThread();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(1);
					workThread.interrupt();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}).start();
		try {
			//在main线程中运行任务
			System.out.println(5 / 0);
		} catch (RuntimeException e) {
			System.out.println(e.getClass().getSimpleName());
		}
	}
	
}

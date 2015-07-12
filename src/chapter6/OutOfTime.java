package chapter6;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * P102 6-9
 * java.util.Timer类的问题:一个任务抛出异常，会导致后面的所有任务都无法运行(一个Timer对象只会创建一个线程来执行所有的任务)
 * @author skywalker
 *
 */
public class OutOfTime {

	public static void main(String[] args) throws InterruptedException {
		Timer timer = new Timer();
		timer.schedule(new ThrowTask(), 1);
		TimeUnit.SECONDS.sleep(1);
		//上面出现了异常，Timer就被取消了，下面的就会抛出一个
		//java.lang.IllegalStateException: Timer already cancelled.
		timer.schedule(new ThrowTask(), 5);
		TimeUnit.SECONDS.sleep(5);
	}
	
	/**
	 * 将会抛出异常的任务
	 * @author skywalker
	 *
	 */
	private static class ThrowTask extends TimerTask {

		@Override
		public void run() {
			System.out.println("执行一次");
			throw new RuntimeException();
		}
		
	}
	
}

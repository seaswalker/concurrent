package chapter7;

import java.util.concurrent.TimeUnit;

/**
 * 抛出中断异常后中断标志位复位吗?
 * @author skywalker
 *
 */
public class Reset {

	public static void main(String[] args) {
		Task task = new Task();
		task.start();
		task.interrupt();
	}
	
	public static class Task extends Thread {
		@Override
		public void run() {
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				//false, 确实复位了
				//System.out.println(Thread.currentThread().isInterrupted());
				//一定不要吞掉!
				Thread.currentThread().interrupt();
			}
		}
	}
	
}

package chapter7;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * P130 7-21 解决shutDownNow()的弊端---只能返回没有执行的任务，但是无法返回被取消的任务
 * 这是装饰模式吧
 * @author skywalker
 *
 */
public class TrackingExecutor extends AbstractExecutorService {

	private final ExecutorService service;
	private final Set<Runnable> cancelledTasks = Collections.synchronizedSet(new HashSet<Runnable>());
	
	public TrackingExecutor(ExecutorService service) {
		this.service = service;
	}
	
	public Set<Runnable> getCanceledTasks() {
		//只有终结了才可以执行此功能
		if (!isShutdown()) {
			throw new IllegalStateException();
		}
		//不要发布
		return new HashSet<Runnable>(cancelledTasks);
	}
	
	@Override
	public void execute(Runnable command) {
		service.execute(new Runnable() {
			@Override
			public void run() {
				try {
					command.run();
				} finally {
					if (isShutdown() && Thread.currentThread().isInterrupted()) {
						cancelledTasks.add(command);
					}
				}
			}
		});
	}

	//以下都是委托给service
	@Override
	public void shutdown() {
		service.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return service.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return service.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return service.isTerminated();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		return service.awaitTermination(timeout, unit);
	}
	
	public static void main(String[] args) throws InterruptedException {
		//每次只能执行一个任务
		TrackingExecutor trackingExecutor = new TrackingExecutor(Executors.newFixedThreadPool(3));
		trackingExecutor.submit(new Reset.Task());
		trackingExecutor.submit(new Reset.Task());
		trackingExecutor.submit(new Reset.Task());
		trackingExecutor.submit(new Reset.Task());
		trackingExecutor.submit(new Reset.Task());
		TimeUnit.SECONDS.sleep(2);
		int unPerformed = trackingExecutor.shutdownNow().size();
		System.out.println(unPerformed + "个未被执行");
		System.out.println(trackingExecutor.getCanceledTasks().size() + "被取消");
	}

}

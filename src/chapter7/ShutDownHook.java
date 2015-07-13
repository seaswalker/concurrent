package chapter7;

/**
 * P136 7-26 JVM关闭钩子
 * 所有关闭钩子是并发执行的,最好是把所有的结束任务放到一个钩子里面串行执行
 * @author skywalker
 *
 */
public class ShutDownHook {

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				//XXX干点坏事儿
			}
		});
	}
	
}

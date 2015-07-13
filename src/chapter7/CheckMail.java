package chapter7;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * P130 7-20 主要是为了检测一下volatile boolean和AtomicBoolean到底有什么区别
 * 其实书中不用的原因是只有final才可以修饰局部变量!!!
 * 也不是没有收获:
 * 	a) java.util.concurrent.atomic包是用硬件来实现原子操作的(反映在java中是sun.misc.Unsafe)，所以免去了
 * 		加锁带来的损耗，这是从别人博客摘来的:
 * 		在x86 平台上，CPU提供了在指令执行期间对总线加锁的手段。CPU芯片上有一条引线#HLOCK pin，如果汇编语言的程序中在一条指令前面加上前缀"LOCK"，
 * 		经过汇编以后的机器代码就使CPU在执行这条指令的时候把#HLOCK pin的电位拉低，持续到这条指令结束时放开，从而把总线锁住，
 * 		这样同一总线上别的CPU就暂时不能通过总线访问内存了，保证了这条指令在多处理器环境中的原子性。
 *  b) 具体到volatitle boolean和AtomicBoolean的区别，第一点就是后者支持一些先判断再执行的原子操作，前者只能手动加锁，第二点
 *  	是从StackOverFlow(http://stackoverflow.com/questions/3786825/volatile-boolean-vs-atomicboolean)上摘来的经验:
 *  	I use volatile fields when said field is ONLY UPDATED by its owner thread and the value
 *  	is only read by other threads, you can think of it as a publish/subscribe scenario 
 *  	where there are many observers but only one publisher. 
 *  	However if those observers must perform some logic based on the value of the field 
 *  	and then push back a new value then I go with Atomic* vars or locks or synchronized blocks, 
 *  	whatever suits me best. In many concurrent scenarios it boils down to get the value, 
 *  	compare it with another one and update if necessary, hence the compareAndSet 
 *  	and getAndSet methods present in the Atomic* classes.
 * @author skywalker
 *
 */
public class CheckMail {
	
	public static void main(String[] args) throws InterruptedException {
		checkMail(Arrays.asList("Tom", "xsdwem7@hotmail.com"));
	}

	/**
	 * 此处简化处理，如果字符串中含有@，那么就返回true
	 * @param hosts 主机
	 * @throws InterruptedException 
	 */
	private static boolean checkMail(List<String> hosts) throws InterruptedException {
		final ExecutorService service = Executors.newCachedThreadPool();
		final AtomicBoolean hasMail = new AtomicBoolean(false);
		try {
			for (final String host : hosts) {
				service.execute(new Runnable() {
					@Override
					public void run() {
						if (host.indexOf("@") >= 0) {
							hasMail.set(true);
						}
					}
				});
			}
		} finally {
			service.shutdown();
			service.awaitTermination(10, TimeUnit.SECONDS);
		}
		return hasMail.get();
	}
	
}

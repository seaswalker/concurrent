package chapter7;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * P112 7-1 素数生成器
 * 使用标志量结束线程, 不能用于阻塞的情况
 * @author skywalker
 *
 */
public class PrimeGenerator implements Runnable {
	
	private final List<BigInteger> primes = new ArrayList<BigInteger>();
	private volatile boolean cancelled = false;
	
	public void cancell() {
		cancelled = true;
	}

	@Override
	public void run() {
		BigInteger p = null;
		while (!cancelled) {
			p = nextPrime();
			synchronized (this) {
				primes.add(p);
			}
		}
	}

	private BigInteger nextPrime() {
		return BigInteger.ONE;
	}
	
	public synchronized List<BigInteger> getPrimes() {
		//保护性拷贝
		return new ArrayList<BigInteger>(primes);
	}
	
	public static void main(String[] args) {
		PrimeGenerator primeGenerator = new PrimeGenerator();
		new Thread(primeGenerator).start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			primeGenerator.cancell();
		}
		System.out.println(primeGenerator.getPrimes().size());
	}

}

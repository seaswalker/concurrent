package chapter5;

import java.math.BigInteger;

/**
 * P86 5-16表示一个昂贵的计算任务
 * @author skywalker
 *
 */
public class ExpensiveFunction implements Computable<BigInteger, String> {

	@Override
	public BigInteger compute(String arg) {
		return new BigInteger(arg);
	}

}

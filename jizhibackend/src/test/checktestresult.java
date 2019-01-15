package test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jizhibackend.bean.TestResult;
import com.jizhitest.service.TestResultDaoImpl;

public class checktestresult {

	@Test
	public void test() {
	
		TestResultDaoImpl impl=new TestResultDaoImpl();
		TestResult t=impl.findTestResult(180770635, 1);
		System.out.print(t.getResultid());
	}

}

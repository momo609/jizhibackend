package test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jizhibackend.bean.Question;
import com.jizhitest.service.PaperDaoImpl;

public class querytest {

	@Test
	public void test() {
		Question q=new Question();
		q.setAnswer("aaa");
		q.setStem("aaa");
		q.setType(3);
		PaperDaoImpl dao=new PaperDaoImpl();
		dao.addQuestion(1, q, 1);
		System.out.println(q.getId());
	}

}

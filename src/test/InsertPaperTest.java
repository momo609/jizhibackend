package test;

import static org.junit.Assert.*;



import org.junit.Test;

import com.jizhibackend.bean.Paper;
import com.jizhitest.service.PaperDaoImpl;

public class InsertPaperTest {

	@Test
	public void test() {
		PaperDaoImpl dao=new PaperDaoImpl();
		Paper paper=new Paper();
		paper.setCreatetime(System.currentTimeMillis());
		paper.setOwner(11);
		paper.setTitle("บวบวบว");
		System.out.print(dao.createPaper(paper));
		System.out.print(paper.getId());
	}

}

package test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jizhitest.service.MyClassDaoImpl;

public class removeMemberTest {

	@Test
	public void test() {
	MyClassDaoImpl dao=new MyClassDaoImpl();
	dao.removeClassMember(10003, 2);
	
	}

}

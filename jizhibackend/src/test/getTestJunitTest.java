package test;

import static org.junit.Assert.*;

import java.util.List;

import net.sf.json.JSONArray;

import org.junit.Test;

import com.jizhibackend.bean.MyTest;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyTestDaoImpl;

public class getTestJunitTest {

	@Test
	public void test() {
		User user=new User();
		user.setUsertype(User.teacher);
		user.setUserid(11);
		
		MyTestDaoImpl daoImpl=new MyTestDaoImpl();
		List<MyTest> testList=daoImpl.getTestsOfUsers(user,1);
		JSONArray ja=JSONArray.fromObject(testList);
		List<MyTest> testList2=JSONArray.toList(ja, MyTest.class);
		System.out.print(testList2.get(0).getMyclass().getName());
	}

}

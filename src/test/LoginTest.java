package test;
/**
 * @author junbin
 * @date 2018-3-12
 */
import static org.junit.Assert.*;

import org.junit.Test;

import com.jizhibackend.bean.User;
import com.jizhitest.service.UserDaoImpl;

public class LoginTest {
	@Test
	public void test1(){  //测试输入错误密码的情况
		UserDaoImpl userdao = new UserDaoImpl();
		String username = "A";
		String password = "aa"; //实际密码是a
		User user = userdao.findUser(username);
		assertEquals(password,user.getPassword());
	}
}

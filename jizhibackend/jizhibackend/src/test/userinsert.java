package test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jizhibackend.bean.User;
import com.jizhitest.service.UserDaoImpl;

public class userinsert {

	@Test
	public void test() {
		UserDaoImpl userDaoImpl=new UserDaoImpl();
		User user=new User();
		user.setPassword("123");
		user.setUsername("asdawqscegc");
		user.setUsertype(1);
		userDaoImpl.registerUser2(user);
		System.out.print(user.getUserid());
	}

}

package util;

import com.jizhibackend.bean.User;
import com.jizhitest.service.UserDaoImpl;

public class CheckopenId {
	public boolean check(User user,String curOpenid){
		boolean flag = false;
		UserDaoImpl dao = new UserDaoImpl();
		String openid = dao.findOpneid(user);
		if(openid.equals(curOpenid)){
			flag = true;
		}
		return flag;
	}
}

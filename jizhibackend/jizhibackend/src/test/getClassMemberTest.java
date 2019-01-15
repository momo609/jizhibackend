package test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jizhibackend.bean.MyClass;
import com.jizhitest.service.MyClassDaoImpl;
import net.sf.json.*;
public class getClassMemberTest {

	@Test
	public void test() {
		MyClassDaoImpl daoImpl=new MyClassDaoImpl();
		List<Map<String,String>> list= daoImpl.getClassMember(10003);
	
		 JSONObject jo=new JSONObject();
		JSONArray ja=JSONArray.fromObject(list);
		jo.put("class_member", ja);
		System.out.println(jo);
	}

}

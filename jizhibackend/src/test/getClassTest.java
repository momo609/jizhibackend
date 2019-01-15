package test;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

import com.jizhibackend.bean.MyClass;
import com.jizhitest.service.MyClassDaoImpl;

public class getClassTest {

	@Test
	public void test() {
		List<MyClass> classList=new ArrayList<MyClass>();
		MyClassDaoImpl dao=new MyClassDaoImpl();
		classList=dao.ClassofOwner(11);
		JSONArray ja=JSONArray.fromObject(classList);
		
		JSONObject jo=new JSONObject();
		jo.put("classes", ja);
		System.out.print(jo);
	}

}

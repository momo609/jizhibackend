package test;

import static org.junit.Assert.*;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

import com.jizhibackend.bean.MyClass;
import com.jizhibackend.bean.Student;
import com.jizhitest.service.MyClassDaoImpl;

public class getClassStudentTest {

	@Test
	public void test() {
		
		MyClassDaoImpl dao=new MyClassDaoImpl();
		List<Student> list = dao.getClassMembers(10003);
		JSONArray ja=JSONArray.fromObject(list);
		JSONObject jo=new JSONObject();
		jo.put("classmembers", list);
		System.out.println(jo);
	}

}

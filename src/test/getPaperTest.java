package test;

import static org.junit.Assert.*;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

import com.jizhibackend.bean.Paper;
import com.jizhitest.service.PaperDaoImpl;

public class getPaperTest {

	@Test
	public void test() {
PaperDaoImpl paperdao=new PaperDaoImpl();
		
		List<Paper> paperlist=paperdao.getPapers(11);
		JSONObject jo=new JSONObject();
		JSONArray ja=JSONArray.fromObject(paperlist);
		jo.put("papers", ja);
		System.out.print(jo);
	}

}

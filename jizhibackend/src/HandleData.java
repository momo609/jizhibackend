import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.jizhibackend.bean.AllTestresult;
import com.jizhibackend.bean.MyTest;
import com.jizhibackend.bean.Question;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyTestDaoImpl;
import com.jizhitest.service.QuestionDaoImpl;
import com.jizhitest.service.TestResultDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class HandleData extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		User user = (User) request.getSession().getAttribute("user");
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao1 = new UserDaoImpl();
		int testid= Integer.parseInt(request.getParameter("testeid"));
		TestResultDaoImpl dao=new TestResultDaoImpl();
		JSONObject jo=new JSONObject();
		int m = 0;
		try {
			m=dao.inserthandledata(testid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (m != 0) {
			out.print("{\"errcode\":100,\"errmsg\":\"数据处理成功\"}");
			out.print(jo);
		} else {
			out.print("{\"errcode\":104,\"errmsg\":\"系统错误\"}");
			out.flush();
			out.close();
		}
	}
}


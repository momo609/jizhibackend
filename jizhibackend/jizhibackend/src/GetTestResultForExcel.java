import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.WriteException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.GenerateExcel;

import com.jizhibackend.bean.TestResult;
import com.jizhitest.service.MyTestDaoImpl;
import com.jizhitest.service.TestResultDaoImpl;
import com.jizhitest.service.UserDaoImpl;
/**
 * 获取学生成绩Excel
 * @author Administrator
 *
 */

public class GetTestResultForExcel extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int testid = Integer.parseInt(request.getParameter("testid"));
		//测试总分
		int totalScore = Integer.parseInt(request.getParameter("total"));
//		System.out.println("总分："+totalScore);
		TestResultDaoImpl impl = new TestResultDaoImpl();
		UserDaoImpl dao = new UserDaoImpl();
		List<TestResult> t = impl.findTestResultByTestid(testid);
		//考生学号
		List<String> studentId = new ArrayList<String>();
		//考生姓名
		List<String> studentName = new ArrayList<String>();
		//考生成绩
		List<Integer> studentScore = new ArrayList<Integer>();
		//考生用时
		List<Long> studentTime = new ArrayList<Long>();
		//考生班级
		List<String> studentClass = new ArrayList<String>();
		
		
		for (TestResult testResult : t) {
			studentId.add(dao.findUser(testResult.getStudentid()).getUsername());
			studentName.add(dao.findUser(testResult.getStudentid()).getNickname());
			studentClass.add(dao.findUser(testResult.getStudentid()).getClassname());
			studentScore.add(testResult.getScore());
			studentTime.add(testResult.getTotal_time_used());
		}
		
		//通过testid查找test名称
		MyTestDaoImpl myDao = new MyTestDaoImpl();
		String title = myDao.getTestByid(testid).getTitle();
		
		//将数据写进Excel中
		String path =this.getServletContext().getRealPath("/File/examResults/");//+title+"学生成绩表.xls"
		File file=new File(path,title+"学生成绩表.xls");
//        if(!file.exists()){
//        	file.mkdirs();
//        }
        FileOutputStream os = new FileOutputStream(file);   
		GenerateExcel ge = new GenerateExcel();
		JSONObject jo = new JSONObject();
		String downPath = "http://dawnlab.gxu.edu.cn/jizhibackend/File/examResults/"+title+"学生成绩表.xls";
		String fileTitle = title+"学生成绩表";
		try {
			ge.createExcel(os,fileTitle,studentId,studentName,studentScore,studentClass,studentTime,totalScore);
//			System.out.println(studentClass.toString());
			jo.element("path", downPath);
			jo.element("errcode", 0);
			out.print(jo);
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			out.print("{\"errcode\":104,\"errmsg\":\"生成失败请重试\"}");
		}
		out.flush();
		out.close();
	}

	}



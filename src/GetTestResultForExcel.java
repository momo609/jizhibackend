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
 * ��ȡѧ���ɼ�Excel
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
		//�����ܷ�
		int totalScore = Integer.parseInt(request.getParameter("total"));
//		System.out.println("�ܷ֣�"+totalScore);
		TestResultDaoImpl impl = new TestResultDaoImpl();
		UserDaoImpl dao = new UserDaoImpl();
		List<TestResult> t = impl.findTestResultByTestid(testid);
		//����ѧ��
		List<String> studentId = new ArrayList<String>();
		//��������
		List<String> studentName = new ArrayList<String>();
		//�����ɼ�
		List<Integer> studentScore = new ArrayList<Integer>();
		//������ʱ
		List<Long> studentTime = new ArrayList<Long>();
		//�����༶
		List<String> studentClass = new ArrayList<String>();
		
		
		for (TestResult testResult : t) {
			studentId.add(dao.findUser(testResult.getStudentid()).getUsername());
			studentName.add(dao.findUser(testResult.getStudentid()).getNickname());
			studentClass.add(dao.findUser(testResult.getStudentid()).getClassname());
			studentScore.add(testResult.getScore());
			studentTime.add(testResult.getTotal_time_used());
		}
		
		//ͨ��testid����test����
		MyTestDaoImpl myDao = new MyTestDaoImpl();
		String title = myDao.getTestByid(testid).getTitle();
		
		//������д��Excel��
		String path =this.getServletContext().getRealPath("/File/examResults/");//+title+"ѧ���ɼ���.xls"
		File file=new File(path,title+"ѧ���ɼ���.xls");
//        if(!file.exists()){
//        	file.mkdirs();
//        }
        FileOutputStream os = new FileOutputStream(file);   
		GenerateExcel ge = new GenerateExcel();
		JSONObject jo = new JSONObject();
		String downPath = "http://dawnlab.gxu.edu.cn/jizhibackend/File/examResults/"+title+"ѧ���ɼ���.xls";
		String fileTitle = title+"ѧ���ɼ���";
		try {
			ge.createExcel(os,fileTitle,studentId,studentName,studentScore,studentClass,studentTime,totalScore);
//			System.out.println(studentClass.toString());
			jo.element("path", downPath);
			jo.element("errcode", 0);
			out.print(jo);
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			out.print("{\"errcode\":104,\"errmsg\":\"����ʧ��������\"}");
		}
		out.flush();
		out.close();
	}

	}



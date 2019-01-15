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
import util.GenerateExcelForVote;

import com.jizhibackend.bean.MyVoteTest;
import com.jizhibackend.bean.TestResult;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyTestDaoImpl;
import com.jizhitest.service.MyVoteTestDaoImpl;
import com.jizhitest.service.TestResultDaoImpl;
import com.jizhitest.service.UserDaoImpl;
import com.jizhitest.service.VoteTestResultDaoImpl;
/**
 * ��ȡѧ���ɼ�Excel
 * @author Administrator
 *
 */

public class GetVoteResultForExcel extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int votetestid = Integer.parseInt(request.getParameter("votetestid"));
		VoteTestResultDaoImpl impl=new VoteTestResultDaoImpl();
		MyVoteTestDaoImpl myTestDaoImpl = new MyVoteTestDaoImpl();
		MyVoteTest myTest=myTestDaoImpl.getVoteTestByid(votetestid);
		String studentid=myTest.getStudentid();
		String[] studentids=studentid.split(",");
		UserDaoImpl dao = new UserDaoImpl();
//		List<TestResult> t = impl.findTestResultByTestid(testid);
		//����ѧ��
		List<String> studentId = new ArrayList<String>();
		//��������
		List<String> studentName = new ArrayList<String>();
		//����ͶƱ�ɼ�
		List<Integer> studentVoteScore = new ArrayList<Integer>();
		//ÿ����������ͶƱ���
		List<String> studentVoteDetail = new ArrayList<String>();
		//ȫ����������ͶƱ���
		List<List<String>> studentVoteDetails = new ArrayList<List<String>>();
		
		int scores=-1;
	    for(int i=0;i<studentids.length;i++)
	    {
	    	 scores= impl.findTestResults(votetestid, Integer.parseInt(studentids[i]));
	    	 studentVoteDetail = impl.findVoteResults(votetestid,Integer.parseInt(studentids[i]));
//	    	 System.out.println("sss:"+studentVoteDetail);
	    	 studentVoteDetails.add(studentVoteDetail);
	    	 User student=dao.findUser(Integer.parseInt(studentids[i]));
	    	 studentName.add(student.getNickname());
	    	 studentId.add(student.getUsername());
	    	 studentVoteScore.add(scores);
//	    	 map2.put(studentids[i], student.getNickname());
//	    	 map.put(student.getNickname(), scores);
	    }
			
		//ͨ��votetestid����vote����
		String title = myTestDaoImpl.getVoteTestByid(votetestid).getTitle();
		
		//������д��Excel��
		String path =this.getServletContext().getRealPath("/File/voteResults/");//+title+"ѧ���ɼ���.xls"
		File file=new File(path,title+"ѧ��ͶƱͳ�Ʊ�.xls");
//        if(!file.exists()){
//        	file.mkdirs();
//        }
        FileOutputStream os = new FileOutputStream(file);   
		GenerateExcelForVote ge = new GenerateExcelForVote();
		JSONObject jo = new JSONObject();
		String downPath = "http://dawnlab.gxu.edu.cn/jizhibackend/File/voteResults/"+title+"ѧ��ͶƱͳ�Ʊ�.xls";
//		String fileTitle = title+"ѧ��ͶƱͳ�Ʊ�";
		try {
			ge.createExcelforvote(os,title,studentId,studentName,studentVoteScore,studentVoteDetails);
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



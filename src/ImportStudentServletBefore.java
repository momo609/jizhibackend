import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import util.AnalysisExcel;
import util.AnalysisWord;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jizhibackend.bean.Paper;
import com.jizhibackend.bean.SingleChoiceQuestion;
import com.jizhibackend.bean.Student;
import com.jizhibackend.bean.Student1;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.PaperDaoImpl;
import com.jizhitest.service.UserDaoImpl;
/**
 * ��ȡǰ�˴�������excel�ļ���������Ȼ�󷵻���Ӧ��json�ַ�����
 * @author Administrator
 *
 */
public class ImportStudentServletBefore extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		String path =this.getServletContext().getRealPath(
				"/File/classmembers/");
        //��ô����ļ���Ŀ����  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        //��ȡ�ļ���Ҫ�ϴ�����·��  
       
        File file=new File(path);
        if(!file.exists()){
        	file.mkdirs();
        }
        factory.setRepository(new File(path));  
        //���� ����Ĵ�С
        factory.setSizeThreshold(1024*1024) ;  
        //�ļ��ϴ�����  
        ServletFileUpload upload = new ServletFileUpload(factory);  
//        JSONObject jo=new JSONObject();
        try {  
            //�����ϴ�����ļ�  
            List<FileItem> list = (List<FileItem>)upload.parseRequest(request);  
            for(FileItem item : list){  
                //��ȡ��������  
                String name = item.getFieldName();  
                //�����ȡ�� ����Ϣ����ͨ�� �ı� ��Ϣ  
                if(item.isFormField()){                     
                    //��ȡ�û�����������ַ���,��Ϊ���ύ�������� �ַ������͵�  
                    String value = item.getString() ;  
                    request.setAttribute(name, value);  
                
                }else{  
                	String filename=new String(item.getName());
                    //��ȡ·����  
                    request.setAttribute(name, filename);  
                    //д��������  
                    item.write( new File(path+"/",filename) );//�������ṩ��
                    AnalysisExcel ae = new AnalysisExcel();
            		ArrayList<Student1> stulist=new ArrayList<Student1>();
            		stulist = ae.getStudentList(path+"/"+filename);
                	JSONArray jo=JSONArray.fromObject(stulist);
                    response.getWriter().print(jo);
                }  
            }  
              
        } catch (Exception e) {  
//        	System.out.println("����ʧ��"+e.getMessage());
        	response.getWriter().print("����ʧ�ܣ�"+e.getMessage());
        	response.getWriter().print("{\"errcode\":104,\"errmsg\":\"����ʧ��\"}");
        }  
	}
}

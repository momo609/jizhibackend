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
 * 获取前端传过来的excel文件并解析，然后返回相应的json字符串。
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
        //获得磁盘文件条目工厂  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        //获取文件需要上传到的路径  
       
        File file=new File(path);
        if(!file.exists()){
        	file.mkdirs();
        }
        factory.setRepository(new File(path));  
        //设置 缓存的大小
        factory.setSizeThreshold(1024*1024) ;  
        //文件上传处理  
        ServletFileUpload upload = new ServletFileUpload(factory);  
//        JSONObject jo=new JSONObject();
        try {  
            //可以上传多个文件  
            List<FileItem> list = (List<FileItem>)upload.parseRequest(request);  
            for(FileItem item : list){  
                //获取属性名字  
                String name = item.getFieldName();  
                //如果获取的 表单信息是普通的 文本 信息  
                if(item.isFormField()){                     
                    //获取用户具体输入的字符串,因为表单提交过来的是 字符串类型的  
                    String value = item.getString() ;  
                    request.setAttribute(name, value);  
                
                }else{  
                	String filename=new String(item.getName());
                    //获取路径名  
                    request.setAttribute(name, filename);  
                    //写到磁盘上  
                    item.write( new File(path+"/",filename) );//第三方提供的
                    AnalysisExcel ae = new AnalysisExcel();
            		ArrayList<Student1> stulist=new ArrayList<Student1>();
            		stulist = ae.getStudentList(path+"/"+filename);
                	JSONArray jo=JSONArray.fromObject(stulist);
                    response.getWriter().print(jo);
                }  
            }  
              
        } catch (Exception e) {  
//        	System.out.println("解析失败"+e.getMessage());
        	response.getWriter().print("解析失败："+e.getMessage());
        	response.getWriter().print("{\"errcode\":104,\"errmsg\":\"解析失败\"}");
        }  
	}
}

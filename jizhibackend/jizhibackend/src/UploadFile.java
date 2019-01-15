import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class UploadFile extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		String path =this.getServletContext().getRealPath(
				"/File/upload/");
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
        JSONObject jo=new JSONObject();
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
                	filename = filename.replace(" ", "");
                    //��ȡ·����  
                    request.setAttribute(name, filename);  
                    //д��������  
                    String classid=(String)request.getAttribute("classid");
                    item.write( new File(path+"/"+classid,filename) );//�������ṩ�� 
//                    item.write( new File(path,filename) );//�������ṩ�� 
                    
//                    System.out.println("�ϴ��ɹ���"+filename);
//                    jo.put("filename",filename);
//                    jo.element(0);
//            		jo.element("errmsg", "success");
//            		jo.element("path", path);
//                    response.getWriter().print(filename);//��·�����ظ��ͻ���
                    response.getWriter().print(0);
                }  
            }  
              
        } catch (Exception e) {  
        	System.out.println("�ϴ�ʧ��"+e.getMessage());
        	response.getWriter().print("�ϴ�ʧ�ܣ�"+e.getMessage());
        	response.getWriter().print("{\"errcode\":104,\"errmsg\":\"�ϴ�ʧ��\"}");
        }  
	}
}



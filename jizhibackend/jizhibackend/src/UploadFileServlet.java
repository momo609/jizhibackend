import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class UploadFileServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");  //���ñ���  
        //��ô����ļ���Ŀ����  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        //��ȡ�ļ���Ҫ�ϴ�����·��  
        String path = request.getRealPath("/upload");  
        File file=new File(path);
        if(!file.exists()){
        	file.mkdirs();
        }
        factory.setRepository(new File(path));  
        //���� ����Ĵ�С
        factory.setSizeThreshold(1024*1024) ;  
        //�ļ��ϴ�����  
        ServletFileUpload upload = new ServletFileUpload(factory);  
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
                    //��ȡ·����  
                    
                    String filename = UUID.randomUUID().toString()+".png";
                    request.setAttribute(name, filename);  
                    //д��������  
                    item.write( new File(path,filename) );//�������ṩ��  
                    System.out.println("�ϴ��ɹ���"+filename);
                    response.getWriter().print(filename);//��·�����ظ��ͻ���
                }  
            }  
              
        } catch (Exception e) {  
        	System.out.println("�ϴ�ʧ��");
        	response.getWriter().print("�ϴ�ʧ�ܣ�"+e.getMessage());
        }  
		
	}

}

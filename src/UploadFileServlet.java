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
		request.setCharacterEncoding("utf-8");  //设置编码  
        //获得磁盘文件条目工厂  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        //获取文件需要上传到的路径  
        String path = request.getRealPath("/upload");  
        File file=new File(path);
        if(!file.exists()){
        	file.mkdirs();
        }
        factory.setRepository(new File(path));  
        //设置 缓存的大小
        factory.setSizeThreshold(1024*1024) ;  
        //文件上传处理  
        ServletFileUpload upload = new ServletFileUpload(factory);  
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
                    //获取路径名  
                    
                    String filename = UUID.randomUUID().toString()+".png";
                    request.setAttribute(name, filename);  
                    //写到磁盘上  
                    item.write( new File(path,filename) );//第三方提供的  
                    System.out.println("上传成功："+filename);
                    response.getWriter().print(filename);//将路径返回给客户端
                }  
            }  
              
        } catch (Exception e) {  
        	System.out.println("上传失败");
        	response.getWriter().print("上传失败："+e.getMessage());
        }  
		
	}

}

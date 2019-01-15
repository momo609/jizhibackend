
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
 
public class CORSFilter implements Filter {
 
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
 
    }
 
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
      
//        String hostName = "http://119.29.197.224:8020";
//        
//        if(servletRequest.getRemoteAddr().contains("119.29.197.224")){
//        	 httpResponse.addHeader("Access-Control-Allow-Origin", hostName);
//        	
//        }else{
//        	 httpResponse.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:80");
//        }
//        httpResponse.addHeader("Access-Control-Allow-Origin", "http://119.29.197.224:8020");

//        httpResponse.addHeader("Access-Control-Allow-Origin", ".$_SERVER['HTTP_ORIGIN']");
//        httpResponse.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
 //     httpResponse.addHeader("Access-Control-Allow-Origin", "http://dawnlab.gxu.edu.cn/");
        httpResponse.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
        httpResponse.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        httpResponse.setHeader("Access-Control-Allow-Credentials","true");
        httpResponse.setHeader("Cache-Control", "no-cache");
        filterChain.doFilter(servletRequest, servletResponse);
    }
 
    @Override
    public void destroy() {
 
    }
}

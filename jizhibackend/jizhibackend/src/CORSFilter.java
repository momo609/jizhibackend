
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
 
public class CORSFilter implements Filter {
 
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
 
    }
 
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
//        httpResponse.addHeader("Access-Control-Allow-Origin", "http://dawnlab.gxu.edu.cn");
        httpResponse.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        httpResponse.setHeader("Access-Control-Allow-Credentials","true");
        httpResponse.setHeader("Cache-Control", "no-cache");
        filterChain.doFilter(servletRequest, servletResponse);
    }
 
    @Override
    public void destroy() {
 
    }
}

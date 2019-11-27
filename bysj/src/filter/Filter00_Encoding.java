package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
@WebFilter(filterName = "Filter 0",urlPatterns = {"/*"}*/
/*仅所有资源进行过滤*//*
)
*/
public class Filter00_Encoding implements Filter {

    private String getTime() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(date);
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter 0 -encoding begins");
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String path = request.getRequestURI();
        if (!path.contains("/login")){
            response.setContentType("text/html;charset=UTF-8");
            String method = request.getMethod();
            if ("POST-PUT".contains(method)){
                request.setCharacterEncoding("UTF-8");
            }
        }
        System.out.print(path + "@");
        System.out.println(this.getTime());
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("Filter 0 -encoding ends");
    }

    @Override
    public void destroy() {
    }
}

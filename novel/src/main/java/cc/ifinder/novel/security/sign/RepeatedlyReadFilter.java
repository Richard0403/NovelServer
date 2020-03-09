package cc.ifinder.novel.security.sign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 复制请求数据包body
 * 以提供 拦截器下 可数次获取Body数据包
 * @author zhangmz
 * @version 1.0.0
 * @date 2017/09/21
 */
public class RepeatedlyReadFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RepeatedlyReadFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //复制request.getInputStream流
        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            requestWrapper = new ServletRequestWrapper((HttpServletRequest) request);
        }
        if (null == requestWrapper) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }

    @Override
    public void destroy() {

    }
}

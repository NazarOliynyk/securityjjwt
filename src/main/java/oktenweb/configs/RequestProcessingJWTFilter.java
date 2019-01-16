package oktenweb.configs;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by user on 16.01.19.
 */
public class RequestProcessingJWTFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        filterChain.doFilter(servletRequest, servletResponse); // allows to switch to the next filter
        System.out.println("FILTER WORKS");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}

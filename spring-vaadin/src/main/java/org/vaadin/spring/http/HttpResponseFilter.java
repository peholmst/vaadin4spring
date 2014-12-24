package org.vaadin.spring.http;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter to provide access to the {@link HttpServletResponse}
 * 
 * @author Marko Radinovic (markoradinovic79@gmail.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * <br>
 * Initial code:<a href="https://github.com/markoradinovic/Vaadin4Spring-MVP-Sample-SpringSecurity">https://github.com/markoradinovic/Vaadin4Spring-MVP-Sample-SpringSecurity</a>
 */
public class HttpResponseFilter implements Filter {

	private ThreadLocal<HttpServletResponse> responses = new ThreadLocal<HttpServletResponse>();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletResponse r = (HttpServletResponse) response;
		responses.set(r);
		chain.doFilter(request, response);
		responses.remove();		
	}
	
	public HttpServletResponse getHttpServletReponse() {
		return responses.get();
	}

	@Override
	public void destroy() {		
		
	}

}

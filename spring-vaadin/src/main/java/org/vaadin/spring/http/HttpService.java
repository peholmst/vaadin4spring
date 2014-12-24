package org.vaadin.spring.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface that provides access to the {@link HttpServletRequest} and {@link HttpServletResponse}
 * 
 * @author Marko Radinovic (markoradinovic79@gmail.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * <br><br>
 * Initial code:<a href="https://github.com/markoradinovic/Vaadin4Spring-MVP-Sample-SpringSecurity">https://github.com/markoradinovic/Vaadin4Spring-MVP-Sample-SpringSecurity</a>
 */
public interface HttpService {
	
	public HttpServletRequest getCurrentRequest();
	
	public HttpServletResponse getCurrentResponse();
	
}

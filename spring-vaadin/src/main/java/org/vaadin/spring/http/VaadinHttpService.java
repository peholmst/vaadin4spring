package org.vaadin.spring.http;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service to provide access to the {@link HttpServletRequest} and {@link HttpServletResponse}
 * 
 * @author Marko Radinovic (markoradinovic79@gmail.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * <br><br>
 * Initial code:<a href="https://github.com/markoradinovic/Vaadin4Spring-MVP-Sample-SpringSecurity">https://github.com/markoradinovic/Vaadin4Spring-MVP-Sample-SpringSecurity</a>
 */
public class VaadinHttpService implements HttpService {

    @Autowired
    private HttpServletRequest request;

    @Resource(name = HttpResponseFactory.BEAN_NAME)
    private HttpServletResponse response;

    @Override
    public HttpServletRequest getCurrentRequest() {		
        return request;
    }

    @Override
    public HttpServletResponse getCurrentResponse() {
        return response;
    }

}

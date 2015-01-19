package org.vaadin.spring.http;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.NamedBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Factory to provide access to the {@link HttpServletResponse}
 * 
 * @author Marko Radinovic (markoradinovic79@gmail.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * <br><br>
 * Initial code:<a href="https://github.com/markoradinovic/Vaadin4Spring-MVP-Sample-SpringSecurity">https://github.com/markoradinovic/Vaadin4Spring-MVP-Sample-SpringSecurity</a>
 */
public class HttpResponseFactory implements FactoryBean<HttpServletResponse>, ApplicationContextAware, NamedBean {

    public static final String BEAN_NAME = "httpResponseFactory";
    
    private ApplicationContext applicationContext;

    @Override
    public HttpServletResponse getObject() throws Exception {
        HttpResponseFilter httpResponseFilter = applicationContext.getBean(HttpResponseFilter.class);	
        return httpResponseFilter.getHttpServletReponse();
    }

    @Override
    public Class<?> getObjectType() {	
        return HttpServletResponse.class;
    }

    @Override
    public boolean isSingleton() {		
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;

    }

    @Override
    public String getBeanName() {
        return BEAN_NAME;
    }

}

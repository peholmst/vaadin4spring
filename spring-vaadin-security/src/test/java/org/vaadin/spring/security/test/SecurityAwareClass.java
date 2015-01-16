package org.vaadin.spring.security.test;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.VaadinSecurityAware;
import org.vaadin.spring.security.VaadinSecurityContext;
import org.vaadin.spring.security.VaadinSecurityContextAware;

public class SecurityAwareClass implements InitializingBean, VaadinSecurityAware, VaadinSecurityContextAware {

    private VaadinSecurity vaadinSecurity;
    private VaadinSecurityContext vaadinSecurityContext;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(vaadinSecurity);
        Assert.notNull(vaadinSecurityContext);
    }

    @Override
    public void setVaadinSecurity(VaadinSecurity vaadinSecurity) {
        this.vaadinSecurity = vaadinSecurity;
    }
    
    @Override
    public void setVaadinSecurityContext(VaadinSecurityContext vaadinSecurityContext) {
        this.vaadinSecurityContext = vaadinSecurityContext;
    }

    public VaadinSecurity getVaadinSecurity() {
        return vaadinSecurity;
    }

    public VaadinSecurityContext getVaadinSecurityContext() {
        return vaadinSecurityContext;
    }

}

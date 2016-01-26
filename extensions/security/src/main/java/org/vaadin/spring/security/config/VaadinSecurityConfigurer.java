package org.vaadin.spring.security.config;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Created by petterwork on 22/01/16.
 */
public class VaadinSecurityConfigurer {

    public static final Logger LOGGER = LoggerFactory.getLogger(VaadinSecurityConfigurer.class);

    private final Set<Callback> callbacks = new HashSet<Callback>();

    public synchronized void addCallback(Callback callback) {
        callbacks.add(callback);
    }

    public void configureVaadinSecurity(HttpSecurity httpSecurity) {
        LOGGER.debug("Invoking VaadinSecurityConfigurer callbacks");
        Set<Callback> callbacksCopy;
        synchronized (this) {
            callbacksCopy = new HashSet<Callback>(callbacks);
        }
        for (Callback callback : callbacksCopy) {
            callback.configure(httpSecurity);
        }
    }

    public interface Callback {
        void configure(HttpSecurity httpSecurity);
    }
}

package org.vaadin.spring.samples.security.managed.backend;

import org.springframework.stereotype.Service;

/**
 * Created by petterwork on 09/06/15.
 */
@Service
public class MyBackendBean implements MyBackend {

    @Override
    public String adminOnlyEcho(String s) {
        return "admin:" + s;
    }

    @Override
    public String echo(String s) {
        return s;
    }
}

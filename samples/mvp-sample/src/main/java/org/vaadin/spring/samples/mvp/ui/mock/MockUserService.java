package org.vaadin.spring.samples.mvp.ui.mock;

import org.vaadin.spring.samples.mvp.ui.service.UserService;


public class MockUserService implements UserService {

    @Override
    public String getUserName() {
        return "Joe Blow";
    }


}

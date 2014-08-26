package org.vaadin.spring.samples.mvp.ui.mock;

import org.springframework.stereotype.Service;
import org.vaadin.spring.samples.mvp.ui.service.UserService;

@Service
public class MockUserService implements UserService {

    @Override
    public String getUserName() {
        return "Joe Blow";
    }


}

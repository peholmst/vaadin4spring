package org.vaadin.spring.mvp.explicit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.EnableVaadin;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.mvp.FooView;

@Configuration
@EnableVaadin
public class ExplicitConfig {

    @Autowired
    private EventBus eventBus;

    @Bean
    @UIScope
    public FooView fooView() {
        return new FooView();
    }

    @Bean
    @UIScope
    public ExplicitPresenter fooPresenter() {
        return new ExplicitPresenter(fooView(), eventBus);
    }
}

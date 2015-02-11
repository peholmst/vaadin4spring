/*
 * Copyright 2015 Gert-Jan Timmer <gjr.timmer@gmail.com>.
 *
 */
package org.vaadin.spring.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 *
 * @author Gert-Jan Timmer <gjr.timmer@gmail.com>
 */
@Slf4j
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.profiles(addDefaultProfile())
                .showBanner(false)
                .sources(ApplicationInitializer.class);
    }

    /**
     * Set a default profile if it has not been set.
     * <p/>
     * <p>
     * Please use -Dspring.active.profile=dev
     * </p>
     */
    private String addDefaultProfile() {
        String profile = System.getProperty("spring.active.profile");
        if (profile != null) {
            log.info("Running with Spring profile(s) : {}", profile);
            return profile;
        }

        log.warn("No Spring profile provided, falling back to default");
        return Profiles.DEVELOPMENT;
    }
}

/*
 * Copyright 2015 Gert-Jan Timmer <gjr.timmer@gmail.com>.
 *
 */
package org.vaadin.spring.demo;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

/**
 *
 * @author Gert-Jan Timmer <gjr.timmer@gmail.com>
 */
@Slf4j
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class ApplicationInitializer {

    @Inject
    private Environment env;

    @PostConstruct
    public void initApplication() {
        if ( env.getActiveProfiles().length == 0 ) {
            log.warn("No Spring profile provided, falling back to default");
        } else {
            log.info("Active Spring profile(s): {}", ArrayUtils.toString(env.getActiveProfiles()));
        }
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ApplicationInitializer.class);
        application.setShowBanner(false);

        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);

        addDefaultProfile(application, source);

        addLiquibaseScanPackages();

        ConfigurableApplicationContext context = application.run(args);

        log.debug("Spring Application Started");
    }

    /**
     * Set a default profile if it has not been set
     */
    private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
        if (!source.containsProperty("spring.profiles.active")) {
            app.setAdditionalProfiles(Profiles.DEVELOPMENT);
        }
    }

    /**
     * Set the liquibases.scan.packages to avoid an exception from ServiceLocator
     * <p/>
     * See the following JIRA issue https://liquibase.jira.com/browse/CORE-677
     */
    private static void addLiquibaseScanPackages() {
        System.setProperty(
            "liquibase.scan.packages",
            "liquibase.change"              + "," +
            "liquibase.database"            + "," +
            "liquibase.parser"              + "," +
            "liquibase.precondition"        + "," +
            "liquibase.datatype"            + "," +
            "liquibase.serializer"          + "," +
            "liquibase.sqlgenerator"        + "," +
            "liquibase.executor"            + "," +
            "liquibase.snapshot"            + "," +
            "liquibase.logging"             + "," +
            "liquibase.diff"                + "," +
            "liquibase.structure"           + "," +
            "liquibase.structurecompare"    + "," +
            "liquibase.lockservice"         + "," +
            "liquibase.ext"                 + "," +
            "liquibase.changelog"
        );
    }

}

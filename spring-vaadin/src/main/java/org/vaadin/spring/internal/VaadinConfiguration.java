package org.vaadin.spring.internal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Josh Long (josh@joshlong.com)
 * @author petter@vaadin.com
 * @see EnableVaadin
 */
@Configuration
class VaadinConfiguration {

    @Bean
    static org.vaadin.spring.internal.VaadinUIScope uiScope() {
        return new VaadinUIScope();
    }
}

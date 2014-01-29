package org.vaadin.spring.internal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for registering the custom Vaadin {@link VaadinUIScope scope}.
 *
 * @author Josh Long (josh@joshlong.com)
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.EnableVaadin
 */
@Configuration
public class VaadinConfiguration {

    @Bean
    static VaadinUIScope uiScope() {
        return new VaadinUIScope();
    }
}

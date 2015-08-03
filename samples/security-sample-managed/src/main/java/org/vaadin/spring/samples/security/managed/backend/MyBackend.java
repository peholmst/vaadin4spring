package org.vaadin.spring.samples.security.managed.backend;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Created by petterwork on 09/06/15.
 */
public interface MyBackend {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String adminOnlyEcho(String s);

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    String echo(String s);
}

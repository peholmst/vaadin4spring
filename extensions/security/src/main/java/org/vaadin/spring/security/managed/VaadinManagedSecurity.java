package org.vaadin.spring.security.managed;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.security.VaadinSecurity;

/**
 * Extension of {@link org.vaadin.spring.security.VaadinSecurity} that is used when Vaadin is managing the
 * security of the web application.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface VaadinManagedSecurity extends VaadinSecurity {

    /**
     * Tries to login using the specified authentication object. If authentication succeeds, this method
     * will store the authentication token in the Vaadin Session and return without exceptions.
     *
     * @param authentication the authentication object to authenticate, must not be {@code null}.
     * @return the authenticated {@code Authentication} token.
     * @throws org.springframework.security.core.AuthenticationException if authentication fails.
     */
    Authentication login(Authentication authentication) throws AuthenticationException, Exception;

    /**
     * Convenience method that invokes {@link #login(org.springframework.security.core.Authentication)} with a
     * {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}-object.
     *
     * @param username the username to use, must not be {@code null}.
     * @param password the password to use, must not be {@code null}.
     * @return the authenticated {@code Authentication} token.
     * @throws AuthenticationException if authentication fails.
     */
    Authentication login(String username, String password) throws AuthenticationException, Exception;
}

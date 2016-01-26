package org.vaadin.spring.security.shared;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.security.VaadinSecurity;

/**
 * Extension of {@link org.vaadin.spring.security.VaadinSecurity} that is used when Vaadin is participating
 * in an existing Spring Web Security setup.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface VaadinSharedSecurity extends VaadinSecurity {

    /**
     * Tries to login using the specified authentication object. If authentication succeeds, this method will store the authentication
     * token in the session, TODO continue
     *
     * @param authentication the authentication object to authenticate, must not be {@code null}.
     * @throws org.springframework.security.core.AuthenticationException if authentication fails.
     */
    void login(Authentication authentication) throws AuthenticationException, Exception;

    void login(Authentication authentication, boolean rememberMe) throws AuthenticationException, Exception;

    /**
     * Convenience method that invokes {@link #login(org.springframework.security.core.Authentication)} with a
     * {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}-object.
     *
     * @param username the username to use, must not be {@code null}.
     * @param password the password to use, must not be {@code null}.
     * @throws AuthenticationException if authentication fails.
     */
    void login(String username, String password) throws AuthenticationException, Exception;

    void login(String username, String password, boolean rememberMe) throws AuthenticationException, Exception;
}

package org.vaadin.spring.security.shared.internal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Created by petterwork on 25/01/16.
 */
public class VaadinAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    static final ThreadLocal<Authentication> authenticationRequest = new ThreadLocal<Authentication>();

    public VaadinAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public VaadinAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {
        try {
            return getAuthenticationManager().authenticate(authenticationRequest.get());
        } finally {
            authenticationRequest.remove();
        }
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return authenticationRequest.get() != null;
    }
}

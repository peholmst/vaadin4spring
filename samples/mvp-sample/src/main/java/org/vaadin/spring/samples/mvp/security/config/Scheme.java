package org.vaadin.spring.samples.mvp.security.config;


/**
 * Type-safe enum that represents a security scheme
 * Influences Spring Security -- which authentication strategy to employ.
 * @see SecurityConfig
 *
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public enum Scheme {

    /**
     * Basic Authentication: UI will NOT have a logout option in this case.
     * Session time-out will expire the cookie; or user may clear browser's
     * cache to force re-authentication.  Username and password sent in header.
     * @see javax.servlet.http.HttpServletRequest#BASIC_AUTH
     */
    BASIC("basic"),

    /**
     * Digest Authentication: UI will NOT have a logout option in this case.
     * A much more secure option than <code>BASIC</code>.
     * @see javax.servlet.http.HttpServletRequest#DIGEST_AUTH
     */
    DIGEST("digest"),

    /**
     * Form-based Authentication: UI will have a logout option.
     * Note: Not for use with web-services!
     * @see javax.servlet.http.HttpServletRequest#FORM_AUTH
     */
    FORM("form");


    private String id;

    Scheme(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public static Scheme fromValue(String v) {
        for (Scheme s: Scheme.values()) {
            if (s.id.equals(v)) {
                return s;
            }
        }
        throw new IllegalArgumentException(v);
    }
}

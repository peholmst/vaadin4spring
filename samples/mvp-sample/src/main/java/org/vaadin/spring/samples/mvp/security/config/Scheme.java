/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

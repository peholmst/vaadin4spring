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
package org.vaadin.spring.security.util;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

/**
 * Helper methods for working with security exceptions in Vaadin UIs.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public final class SecurityExceptionUtils {

    private SecurityExceptionUtils() {
    }

    /**
     * Checks if there is an {@link org.springframework.security.access.AccessDeniedException} anywhere in the exception chain
     * of the specified {@code throwable}.
     */
    public static boolean isAccessDeniedException(Throwable throwable) {
        return hasExceptionOfTypeInChain(AccessDeniedException.class, throwable);
    }

    /**
     * Checks if there is an {@link org.springframework.security.core.AuthenticationException} anywhere in the exception chain
     * of the specified {@code throwable}.
     */
    public static boolean isAuthenticationException(Throwable throwable) {
        return hasExceptionOfTypeInChain(AuthenticationException.class, throwable);
    }

    private static boolean hasExceptionOfTypeInChain(Class<? extends Throwable> type, Throwable throwable) {
        if (throwable == null) {
            return false;
        } else if (type.isInstance(throwable)) {
            return true;
        } else {
            return hasExceptionOfTypeInChain(type, throwable.getCause());
        }
    }
}

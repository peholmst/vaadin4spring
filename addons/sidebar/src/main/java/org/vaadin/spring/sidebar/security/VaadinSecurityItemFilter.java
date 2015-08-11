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
package org.vaadin.spring.sidebar.security;

import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.sidebar.SideBarItemDescriptor;
import org.vaadin.spring.sidebar.components.AbstractSideBar;

/**
 * This is an {@link org.vaadin.spring.sidebar.components.AbstractSideBar.ItemFilter ItemFilter} that uses
 * {@link org.vaadin.spring.security.VaadinSecurity} to filter out items with the {@link org.springframework.security.access.annotation.Secured} annotation
 * based on the current user's authorities. If a user has any of the authorities/roles listed in the annotation, the item passes the filter. Items without
 * the annotation also pass the filter.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.security.VaadinSecurity#hasAnyAuthority(String...)
 */
public class VaadinSecurityItemFilter implements AbstractSideBar.ItemFilter {

    private final VaadinSecurity vaadinSecurity;

    /**
     * Creates a new instance of {@code VaadinSecurityItemFilter}.
     *
     * @param vaadinSecurity an instance of {@link org.vaadin.spring.security.VaadinSecurity}.
     */
    public VaadinSecurityItemFilter(VaadinSecurity vaadinSecurity) {
        this.vaadinSecurity = vaadinSecurity;
    }

    @Override
    public boolean passesFilter(SideBarItemDescriptor descriptor) {
        Secured secured = descriptor.findAnnotationOnBean(Secured.class);
        if (secured != null) {
            return vaadinSecurity.hasAnyAuthority(secured.value());
        }
        return true;
    }
}

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
package org.vaadin.spring.request;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;

/**
 * Interface to be implemented by managed (singleton) beans that want to be notified when a Vaadin request starts.
 * The listeners are not invoked in any particular order.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface VaadinRequestStartListener {
    /**
     * Called before Vaadin starts handling a request.
     *
     * @param request  The request
     * @param response The response
     * @see com.vaadin.server.VaadinService#requestStart(com.vaadin.server.VaadinRequest, com.vaadin.server.VaadinResponse)
     */
    void onRequestStart(VaadinRequest request, VaadinResponse response);
}

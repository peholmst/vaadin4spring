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

import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinSession;

/**
 * Interface to be implemented by managed (singleton) beans that want to be notified when a Vaadin request ends.
 * The listeners are not invoked in any particular order.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface VaadinRequestEndListener {
    /**
     * Called after Vaadin has handled a request and the response has
     * been written.
     *
     * @param request  The request object
     * @param response The response object
     * @param session  The session which was used during the request or null if the
     *                 request did not use a session
     * @see com.vaadin.server.VaadinService#requestEnd(com.vaadin.server.VaadinRequest, com.vaadin.server.VaadinResponse, com.vaadin.server.VaadinSession)
     */
    void onRequestEnd(VaadinRequest request, VaadinResponse response, VaadinSession session);
}

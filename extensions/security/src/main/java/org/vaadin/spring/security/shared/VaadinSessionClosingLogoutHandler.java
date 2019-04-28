/*
 * Copyright 2016 The original authors
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
package org.vaadin.spring.security.shared;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.vaadin.flow.server.VaadinSession;


/**
 * A {@link LogoutHandler} that closes all the Vaadin sessions in the current HTTP servlet session.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class VaadinSessionClosingLogoutHandler implements LogoutHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(VaadinSessionClosingLogoutHandler.class);

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            LOGGER.debug("Logging out and closing all Vaadin sessions");
            Collection<VaadinSession> allSessions = VaadinSession.getAllSessions(request.getSession());
            for (VaadinSession session : allSessions) {
                LOGGER.debug("Closing session [{}]", session);
                session.close();
            }
        } catch (Exception ex) {
            LOGGER.error("An exception occurred while attempting to close the Vaadin sessions", ex);
        }
    }
}

/*
 * Copyright 2014 The original authors
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
package org.vaadin.spring.async;

import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedHttpSession;
import com.vaadin.server.WrappedSession;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * When performing asynchronous operations on the server that need to push changes to the UI,
 * you have to access the UI using {@link com.vaadin.ui.UI#access(Runnable)} or {@link com.vaadin.ui.UI#accessSynchronously(Runnable)}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.async.ContextAwareRunnableWrapper
 */
public abstract class ContextAwareRunnable implements Runnable {

    /**
     * Performs the actual operation that requires the context to be set.
     */
    protected abstract void doRun();

    /**
     * Called by {@link #run()} before the actual operation is performed, to set up any thread local state. Please
     * remember that if you are invoking the operation using {@link com.vaadin.ui.UI#access(Runnable)}, the thread local
     * state must be inheritable since the operation might be executed in a different thread. This implementation does nothing, subclasses may override.
     *
     * @param httpSession the current HTTP session, never {@code null}.
     */
    protected void transferState(HttpSession httpSession) {
    }

    /**
     * Called by {@link #run()} after the actual operation has been performed, to reset any thread local state.
     * This implementation does nothing, subclasses may override.
     */
    protected void resetState() {
    }

    @Override
    public final void run() {
        final HttpSession httpSession = getCurrentHttpSession();
        final Map<String, ContextTransferrer> contextTransferrers = getApplicationContext(httpSession).getBeansOfType(ContextTransferrer.class);

        RequestContextHolder.setRequestAttributes(getRequestAttributes(httpSession), true);

        for (ContextTransferrer contextTransferrer : contextTransferrers.values()) {
            contextTransferrer.transferState(httpSession);
        }

        transferState(httpSession);

        try {
            doRun();
        } finally {
            resetState();

            for (ContextTransferrer contextTransferrer : contextTransferrers.values()) {
                contextTransferrer.resetState();
            }

            RequestContextHolder.resetRequestAttributes();
        }
    }

    /**
     * Returns the Spring application context from the specified HTTP session (via the servlet context).
     *
     * @param session the HTTP session, never {@code null}.
     * @return the Spring application context, never {@code null}.
     */
    protected ApplicationContext getApplicationContext(HttpSession session) {
        return WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
    }

    private RequestAttributes getRequestAttributes(HttpSession session) {
        return new ServletRequestAttributes(new RequestProxy(session));
    }

    /**
     * Returns the current HTTP session, retrieved from the current Vaadin session.
     *
     * @return the HTTP session, never {@code null}.
     */
    protected HttpSession getCurrentHttpSession() {
        final WrappedSession wrappedSession = VaadinSession.getCurrent().getSession();
        if (wrappedSession instanceof WrappedHttpSession) {
            return ((WrappedHttpSession) wrappedSession).getHttpSession();
        } else {
            throw new IllegalStateException("This add-on only works in servlet containers");
        }
    }
}

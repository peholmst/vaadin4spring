package org.vaadin.spring.internal;

import com.vaadin.server.UICreateEvent;
import com.vaadin.ui.UI;

import java.io.Serializable;

/**
 * Uniquely identifies a UI instance for a given window/tab and session.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @author Josh Long (josh@joshlong.com)
 */
class VaadinUIIdentifier implements Serializable {
    private final int uiId;
    private final String sessionId;

    public VaadinUIIdentifier(UICreateEvent createEvent) {
        this.uiId = createEvent.getUiId();
        this.sessionId = createEvent.getRequest().getWrappedSession().getId();
    }

    public VaadinUIIdentifier(UI ui) {
        this.uiId = ui.getUIId();
        this.sessionId = ui.getSession().getSession().getId();
    }

    public int getUIId() {
        return uiId;
    }

    public String getSessionId() {
        return sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VaadinUIIdentifier that = (VaadinUIIdentifier) o;

        if (uiId != that.uiId) return false;
        if (!sessionId.equals(that.sessionId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uiId;
        result = 31 * result + sessionId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("UI:%s:%d", sessionId, uiId);
    }
}

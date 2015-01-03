package org.vaadin.spring.samples.mvp.ui.component.listener;

import javax.inject.Inject;

import org.vaadin.spring.VaadinUIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.samples.mvp.ui.component.nav.NavElement;
import org.vaadin.spring.samples.mvp.ui.component.util.ControlsContext;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.shared.MouseEventDetails.MouseButton;

@VaadinUIScope
@VaadinComponent
public class NavigationPanelListener implements ItemClickListener {

    @Inject
    @EventBusScope(EventScope.APPLICATION)
    private EventBus eventBus;

    @Override
    public void itemClick(ItemClickEvent event) {
        eventBus.publish(this, ControlsContext.empty());
        // Pick only left mouse clicks
        if (event.getButton() == MouseButton.LEFT) {
            NavElement ne = (NavElement) event.getItemId();
            eventBus.publish(this, ne);
        }
    }

}

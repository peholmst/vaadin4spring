package org.vaadin.spring.samples.security.ui.views;

import javax.annotation.PostConstruct;

import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@VaadinView(name = SecuredView.NAME)
@VaadinUIScope
@Secured("ROLE_USER")
public class SecuredView extends VerticalLayout implements View {

	private static final long serialVersionUID = 6937605817612926676L;

	public static final String NAME = "secured";

	@PostConstruct
	private void postConstruct() {
		
		setSizeFull();
		setSpacing(true);
		setMargin(true);
		
		addComponent(new Label("<h3>Secured View</h3>", ContentMode.HTML));
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}

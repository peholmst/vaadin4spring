package org.vaadin.spring.samples.security.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.PostConstruct;

@SpringView(name = SecuredView.NAME)
@UIScope
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

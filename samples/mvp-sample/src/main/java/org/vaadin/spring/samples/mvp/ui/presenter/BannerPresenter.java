package org.vaadin.spring.samples.mvp.ui.presenter;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.vaadin.spring.events.EventBusListenerMethod;
import org.vaadin.spring.navigator.Presenter;
import org.vaadin.spring.navigator.VaadinPresenter;
import org.vaadin.spring.samples.mvp.ui.view.BannerView;
import org.vaadin.spring.security.Security;

@VaadinPresenter(viewName = BannerView.NAME)
public class BannerPresenter extends Presenter<BannerView> {

	@Inject
	Security security;

	@EventBusListenerMethod(filter=StartupFilter.class)
	public void onStartup(Action action) {
		if (security.isAuthenticated()) {
			Authentication auth = security.getAuthentication();
			User user = (User) auth.getPrincipal();
			getView().setUser(user.getUsername());
		}
	}

}
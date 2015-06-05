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
package org.vaadin.spring.samples.mvp.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import org.springframework.core.env.Environment;
import org.vaadin.spring.samples.mvp.security.config.Scheme;
import org.vaadin.spring.samples.mvp.ui.component.listener.LogoutLinkListener;

import javax.inject.Inject;

@UIScope
@SpringView(name = BannerView.NAME)
public class BannerView extends Panel implements View {

    private static final long serialVersionUID = 2140523090860294866L;

    public static final String NAME = "banner";

	@Inject
	Environment env;

	@Inject
	private LogoutLinkListener logoutListener;

	public void setUser(String username) {
		setContent(buildRightArea(username));
	}

	protected Layout buildUserArea(String username) {
		HorizontalLayout userArea = new HorizontalLayout();
		String id = env.getProperty("app.security.scheme", Scheme.BASIC.id());
		if (id.equals(Scheme.FORM.id())) {
			Button signOut = new Button("Sign Out");
			signOut.addClickListener(logoutListener);
			userArea.addComponent(signOut);
		}
		Label loggedInUser = new Label();
		loggedInUser.setValue(username);
		loggedInUser.setSizeUndefined();
		userArea.addComponent(loggedInUser);
		return userArea;
	}

	protected GridLayout buildRightArea(String username) {
		GridLayout right = new GridLayout(1, 1);
		right.setWidth(100f, Unit.PERCENTAGE);
		Layout loggedInUser = buildUserArea(username);
		right.addComponent(loggedInUser, 0, 0);
		right.setComponentAlignment(loggedInUser, Alignment.MIDDLE_RIGHT);
		return right;
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}


}

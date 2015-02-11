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
package org.vaadin.spring.samples.security.ui.views;

import javax.annotation.PostConstruct;

import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@VaadinView(name = MainView.NAME)
@VaadinUIScope
public class MainView extends VerticalLayout implements View {

    private static final long serialVersionUID = -3780256410686877889L;
    
    public static final String NAME = "";
    
    @PostConstruct
    private void postConstruct() {
        
        setSizeFull();
        setSpacing(true);
        setMargin(true);
        
        addComponent(new Label("<h2>Main View</h2>", ContentMode.HTML));
        
        Button goToSecuredView = new Button("Go To Secured View");
        goToSecuredView.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = -2896151918118631378L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getNavigator().navigateTo(SecuredView.NAME);				
			}
		});
        addComponent(goToSecuredView);
    }
    
    @Override
    public void enter(ViewChangeEvent event) {
        
    }

}

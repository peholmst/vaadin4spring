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
package org.vaadin.spring.samples.mvp.ui.component.listener;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.navigator.annotation.VaadinPresenter;
import org.vaadin.spring.samples.mvp.ui.component.util.ControlsContext;
import org.vaadin.spring.samples.mvp.ui.presenter.Screen;
import org.vaadin.spring.samples.mvp.ui.view.HeaderView;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

@UIScope
@Component
public class TabSelectedListener implements SelectedTabChangeListener {

  private static final long serialVersionUID = -6279286514453567595L;

  @Inject
  ApplicationContext context;

  @Inject
  private EventBus.UIEventBus eventBus;

  @Override
  // this is kind of hacky!
  // since all presenters are UI-scoped we need to call them into being before publishing events
  // that can be received and handled by them
  public void selectedTabChange(final SelectedTabChangeEvent event) {
    eventBus.publish(context.getBean(HeaderView.class), ControlsContext.empty());
    // TODO RAS enable
    com.vaadin.ui.Component c = event.getTabSheet().getSelectedTab();
    if (View.class.isAssignableFrom(c.getClass())) {
      View v = (View) c;
      SpringView springView = v.getClass().getAnnotation(SpringView.class);
      if (springView != null) {
        context.getBeansWithAnnotation(VaadinPresenter.class);
        eventBus.publish(this, new Screen(springView.name()));
      }
    }
  }

}

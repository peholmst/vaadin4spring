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

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.samples.mvp.ui.component.util.ControlsContext;
import org.vaadin.spring.samples.mvp.ui.view.HeaderView;

import javax.inject.Inject;

@UIScope
@Component
public class TabSelectedListener implements SelectedTabChangeListener {

    private static final long serialVersionUID = -6279286514453567595L;

    @Inject
    ApplicationContext context;

    @Inject
    private EventBus eventBus;

    @Override
    // this is kind of hacky!
    // since all presenters are UI-scoped we need to call them into being before publishing events
    // that can be received and handled by them
    public void selectedTabChange(final SelectedTabChangeEvent event) {
        eventBus.publish(context.getBean(HeaderView.class), ControlsContext.empty());
        // TODO RAS enable
        //Component c = event.getTabSheet().getSelectedTab();
//        if (View.class.isAssignableFrom(c.getClass())) {
//            View v = (View) c;
//            Annotation[] annotations = v.getClass().getAnnotations();
//            if (ArrayUtils.isNotEmpty(annotations)) {
//                for (Annotation a : annotations) {
//                    if (a instanceof VaadinView) {
//                        VaadinView vv = (VaadinView) a;
        // really just need the presenter whose name matched VaadinView#name to be
        // called into being, but Spring caches scoped-beans too
//                    context.getBeansWithAnnotation(VaadinPresenter.class);
//                    eventBus.publish(this, new Screen(vv.name()));
//                    break;
//                }
//            }
//        }
//    }
    }

}

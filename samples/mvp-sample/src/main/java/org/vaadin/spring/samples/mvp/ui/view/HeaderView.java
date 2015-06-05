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
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.spring.samples.mvp.ui.component.util.ControlsContext;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Header >>> responsible for rendering fixed and dynamic elements.
 *
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
@UIScope
@SpringView(name = HeaderView.NAME)
public class HeaderView extends HorizontalLayout implements View {

    private static final long serialVersionUID = -3507331087453071116L;

    public static final String NAME = "header";

    @PostConstruct
    private void init() {
        setWidth("100%");
    }

    public void setContext(ControlsContext context) {
        if (getComponentCount() > 0) {
            removeAllComponents();
        }
        addComponent(buildControlsArea(context));
    }

    protected HorizontalLayout buildControlsArea(ControlsContext context) {
        HorizontalLayout left = new HorizontalLayout();
        left.setSpacing(true);
        left.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        // ---- DYNAMIC HEADER ELEMENTS ----
        // Rendering depends on what tab was selected
        List<Component> controls = context.getControls();
        LayoutIntegrator.addComponents(left, controls.toArray(new Component[controls.size()]));

        return left;
    }


    @Override
    public void enter(ViewChangeEvent event) {

    }


}

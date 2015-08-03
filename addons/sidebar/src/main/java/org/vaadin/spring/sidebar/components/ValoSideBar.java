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
package org.vaadin.spring.sidebar.components;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.spring.sidebar.SideBarItemDescriptor;
import org.vaadin.spring.sidebar.SideBarSectionDescriptor;
import org.vaadin.spring.sidebar.SideBarUtils;

import java.util.Collection;

/**
 * Created by petterwork on 03/08/15.
 */
public class ValoSideBar extends AbstractSideBar {

    private CssLayout layout;

    /**
     * You should not need to create instances of this component directly. Instead, just inject the side bar into
     * your UI.
     *
     * @param sideBarUtils
     */
    public ValoSideBar(SideBarUtils sideBarUtils) {
        super(sideBarUtils);
        setPrimaryStyleName(ValoTheme.MENU_ROOT);
        setSizeUndefined();
    }

    @Override
    protected Component createCompositionRoot() {
        layout = new CssLayout();
        layout.addStyleName(ValoTheme.MENU_PART);
        layout.setWidth(null);
        layout.setHeight("100%");
        return layout;
    }

    @Override
    protected SectionComponentFactory createDefaultSectionComponentFactory() {
        return new DefaultSectionComponentFactory();
    }

    @Override
    protected ItemComponentFactory createDefaultItemComponentFactory() {
        return new DefaultItemComponentFactory();
    }

    @Override
    protected void createSection(SideBarSectionDescriptor section, Collection<SideBarItemDescriptor> items) {
        getSectionComponentFactory().createSectionComponent(section, items);
    }

    /**
     * Extended version of {@link com.vaadin.ui.Button} that is used by the {@link ValoSideBar.DefaultItemComponentFactory}.
     */
    static class ItemButton extends Button {

        ItemButton(final SideBarItemDescriptor descriptor) {
            setPrimaryStyleName(ValoTheme.MENU_ITEM);
            setCaption(descriptor.getCaption());
            setIcon(descriptor.getIcon());
            setDisableOnClick(true);
            addClickListener(new Button.ClickListener() {

                private static final long serialVersionUID = -8512905888847432801L;

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    try {
                        descriptor.itemInvoked(getUI());
                    } finally {
                        setEnabled(true);
                    }
                }

            });
        }
    }

    /**
     * Extended version of {@link ValoSideBar.ItemButton} that is used for view items. This
     * button keeps track of the currently selected view in the current UI's {@link com.vaadin.navigator.Navigator} and
     * updates its style so that the button of the currently visible view can be highlighted.
     */
    static class ViewItemButton extends ItemButton implements ViewChangeListener {

        private final String viewName;
        private static final String STYLE_SELECTED = "selected";

        ViewItemButton(SideBarItemDescriptor.ViewItemDescriptor descriptor) {
            super(descriptor);
            viewName = descriptor.getViewName();
        }

        @Override
        public void attach() {
            super.attach();
            if (getUI().getNavigator() == null) {
                throw new IllegalStateException("Please configure the Navigator before you attach the SideBar to the UI");
            }
            getUI().getNavigator().addViewChangeListener(this);
        }

        @Override
        public void detach() {
            getUI().getNavigator().removeViewChangeListener(this);
            super.detach();
        }

        @Override
        public boolean beforeViewChange(ViewChangeEvent event) {
            return true;
        }

        @Override
        public void afterViewChange(ViewChangeEvent event) {
            if (event.getViewName().equals(viewName)) {
                addStyleName(STYLE_SELECTED);
            } else {
                removeStyleName(STYLE_SELECTED);
            }
        }
    }

    /**
     * Default implementation of {@link ValoSideBar.SectionComponentFactory} that creates
     * a {@link com.vaadin.ui.CssLayout} that contains the items.
     */
    public class DefaultSectionComponentFactory implements SectionComponentFactory {

        private ItemComponentFactory itemComponentFactory;

        @Override
        public void setItemComponentFactory(ItemComponentFactory itemComponentFactory) {
            this.itemComponentFactory = itemComponentFactory;
        }

        @Override
        public Component createSectionComponent(SideBarSectionDescriptor descriptor, Collection<SideBarItemDescriptor> itemDescriptors) {
            Label header = new Label();
            header.setValue(descriptor.getCaption());
            header.setSizeUndefined();
            header.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
            layout.addComponent(header);
            for (SideBarItemDescriptor item : itemDescriptors) {
                layout.addComponent(itemComponentFactory.createItemComponent(item));
            }
            return null; // TODO Redesign the factories to get rid of stuff like this
        }
    }

    /**
     * Default implementation of {@link ValoSideBar.ItemComponentFactory} that creates
     * {@link com.vaadin.ui.Button}s.
     */
    public class DefaultItemComponentFactory implements ItemComponentFactory {

        @Override
        public Component createItemComponent(SideBarItemDescriptor descriptor) {
            if (descriptor instanceof SideBarItemDescriptor.ViewItemDescriptor) {
                return new ViewItemButton((SideBarItemDescriptor.ViewItemDescriptor) descriptor);
            } else {
                return new ItemButton(descriptor);
            }
        }
    }
}

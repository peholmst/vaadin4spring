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

import com.vaadin.annotations.StyleSheet;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.spring.sidebar.SideBarItemDescriptor;
import org.vaadin.spring.sidebar.SideBarSectionDescriptor;
import org.vaadin.spring.sidebar.SideBarUtils;

import java.util.Collection;

/**
 * This is a side bar component that can be used as a main menu in applications. The side bar is an {@link com.vaadin.ui.Accordion}
 * that looks like this:
 * <p>
 * <pre>
 * |-------------|
 * |   Section   |
 * |-------------|
 * |    Item     |
 * |    Item     |
 * |    Item     |
 * |             |
 * |-------------|
 * |   Section   |
 * |-------------|
 * |   Section   |
 * |-------------|
 * </pre>
 * <p>
 * The sections and items are declared using the {@link org.vaadin.spring.sidebar.annotation.SideBarSection} and {@link org.vaadin.spring.sidebar.annotation.SideBarItem} annotations, respectively.
 * To use this side bar, simply enable it in your application configuration using the {@link org.vaadin.spring.sidebar.annotation.EnableSideBar} annotation,
 * and inject it into your UI.
 * <p>
 * The side bar comes with a simple theme. In most cases, you probably want to change the styles to make the look and feel of the side bar more compatible with your application.
 * Please see the {@code VAADIN/addons/sidebar/sidebar.css} file in the resources directory.
 * <p>
 * If you want to customize the components that are added to the accordion, implement {@link AccordionSideBar.SectionComponentFactory} and/or {@link AccordionSideBar.ItemComponentFactory}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@StyleSheet("vaadin://addons/sidebar/sidebar.css")
public class AccordionSideBar extends AbstractSideBar {

    private static final long serialVersionUID = 2268915666228648275L;
    
    public static final String SIDE_BAR_STYLE = "sideBar";
    public static final String SIDE_BAR_SECTION_ITEM_STYLE = "sideBarSectionItem";
    public static final String SIDE_BAR_SECTION_STYLE = "sideBarSection";
    public static final String SELECTED_STYLE = "selected";

    private Accordion accordion;

    /**
     * You should not need to create instances of this component directly. Instead, just inject the side bar into
     * your UI.
     */
    public AccordionSideBar(SideBarUtils sideBarUtils) {
        super(sideBarUtils);
        addStyleName(SIDE_BAR_STYLE);
        setSizeFull();
    }

    @Override
    protected Component createCompositionRoot() {
        accordion = new Accordion();
        accordion.setSizeFull();
        return accordion;
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
        accordion.addTab(getSectionComponentFactory().createSectionComponent(section, items), section.getCaption());
    }

    /**
     * Extended version of {@link com.vaadin.ui.NativeButton} that is used by the {@link AccordionSideBar.DefaultItemComponentFactory}.
     */
    static class ItemButton extends NativeButton {

        private static final long serialVersionUID = 7051031354148037389L;

        ItemButton(final SideBarItemDescriptor descriptor) {
            setCaption(descriptor.getCaption());
            setIcon(descriptor.getIcon());
            setDisableOnClick(true);
            addStyleName(SIDE_BAR_SECTION_ITEM_STYLE);
            setWidth(100, Unit.PERCENTAGE);
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
     * Extended version of {@link AccordionSideBar.ItemButton} that is used for view items. This
     * button keeps track of the currently selected view in the current UI's {@link com.vaadin.navigator.Navigator} and
     * updates its style so that the button of the currently visible view can be highlighted.
     */
    static class ViewItemButton extends ItemButton implements ViewChangeListener {

        private static final long serialVersionUID = -9062573995531971821L;
        private final String viewName;

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
                addStyleName(SELECTED_STYLE);
            } else {
                removeStyleName(SELECTED_STYLE);
            }
        }
    }

    /**
     * Default implementation of {@link AccordionSideBar.SectionComponentFactory} that creates
     * a {@link com.vaadin.ui.Panel} with a {@link com.vaadin.ui.VerticalLayout} that contains the items.
     */
    public class DefaultSectionComponentFactory implements SectionComponentFactory {

        private ItemComponentFactory itemComponentFactory;

        @Override
        public void setItemComponentFactory(ItemComponentFactory itemComponentFactory) {
            this.itemComponentFactory = itemComponentFactory;
        }

        @Override
        public Component createSectionComponent(SideBarSectionDescriptor descriptor, Collection<SideBarItemDescriptor> itemDescriptors) {
            final Panel panel = new Panel();
            panel.addStyleName(SIDE_BAR_SECTION_STYLE);
            panel.setSizeFull();
            final VerticalLayout layout = new VerticalLayout();
            panel.setContent(layout);
            for (SideBarItemDescriptor item : itemDescriptors) {
                layout.addComponent(itemComponentFactory.createItemComponent(item));
            }
            return panel;
        }
    }

    /**
     * Default implementation of {@link AccordionSideBar.ItemComponentFactory} that creates
     * {@link com.vaadin.ui.NativeButton}s.
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

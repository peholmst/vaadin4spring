/*
 * Copyright 2014 The original authors
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
package org.vaadin.spring.stuff.sidebar;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import java.util.Collection;

/**
 * This is a side bar component that can be used as a main menu in applications. The side bar is an {@link com.vaadin.ui.Accordion}
 * that looks like this:
 * <p/>
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
 * <p/>
 * The sections and items are declared using the {@link org.vaadin.spring.stuff.sidebar.SideBarSection} and {@link org.vaadin.spring.stuff.sidebar.SideBarItem} annotations, respectively.
 * To use this side bar, simply enable it in your application configuration using the {@link org.vaadin.spring.stuff.sidebar.EnableSideBar} annotation,
 * and inject it into your UI.
 * <p/>
 * The side bar comes with a simple theme. In most cases, you probably want to change the styles to make the look and feel of the side bar more compatible with your application.
 * Please see the {@code VAADIN/addons/sidebar/sidebar.css} file in the resources directory.
 * <p/>
 * If you want to customize the components that are added to the accordion, implement {@link org.vaadin.spring.stuff.sidebar.SideBar.SectionComponentFactory} and/or {@link org.vaadin.spring.stuff.sidebar.SideBar.ItemComponentFactory}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@StyleSheet("vaadin://addons/sidebar/sidebar.css")
public class SideBar extends Accordion {

    public static final String SIDE_BAR_STYLE = "sideBar";
    public static final String SIDE_BAR_SECTION_ITEM_STYLE = "sideBarSectionItem";
    public static final String SIDE_BAR_SECTION_STYLE = "sideBarSection";
    public static final String SELECTED_STYLE = "selected";

    private final SideBarUtils sideBarUtils;
    private final SectionComponentFactory sectionComponentFactory;
    private final ItemComponentFactory itemComponentFactory;

    /**
     * You should not need to create instances of this component directly. Instead, just inject the side bar into
     * your UI.
     */
    public SideBar(SideBarUtils sideBarUtils, SectionComponentFactory sectionComponentFactory, ItemComponentFactory itemComponentFactory) {
        this.sideBarUtils = sideBarUtils;
        addStyleName(SIDE_BAR_STYLE);
        setSizeFull();

        if (sectionComponentFactory == null) {
            sectionComponentFactory = new DefaultSectionComponentFactory();
        }
        if (itemComponentFactory == null) {
            itemComponentFactory = new DefaultItemComponentFactory();
        }
        this.sectionComponentFactory = sectionComponentFactory;
        this.itemComponentFactory = itemComponentFactory;
        sectionComponentFactory.setItemComponentFactory(itemComponentFactory);
    }

    @Override
    public void attach() {
        super.attach();
        for (SideBarSectionDescriptor section : sideBarUtils.getSideBarSections(getUI().getClass())) {
            addTab(sectionComponentFactory.createSectionComponent(section, sideBarUtils.getSideBarItems(section)),
                    section.getCaption());
        }
    }

    @Override
    public void detach() {
        removeAllComponents();
        super.detach();
    }

    /**
     * Interface defining a factory for creating components that correspond to sections in a side bar.
     * In practice, a section is a tab of a {@link org.vaadin.spring.stuff.sidebar.SideBar}.
     * <p/>
     * If you want to use your own factory, make a Spring managed bean that implements this interface.
     * It will automatically be used by the {@link org.vaadin.spring.stuff.sidebar.SideBar}.
     *
     * @see SideBar#addTab(com.vaadin.ui.Component)
     */
    public interface SectionComponentFactory {
        /**
         * Sets the {@code ItemComponentFactory} to use when creating the items of the section.
         *
         * @param itemComponentFactory the item component factory, must not be {@code null}.
         */
        void setItemComponentFactory(ItemComponentFactory itemComponentFactory);

        /**
         * Creates a component to be added as a tab to the {@link org.vaadin.spring.stuff.sidebar.SideBar}.
         *
         * @param descriptor      the descriptor of the side bar section, must not be {@code null}.
         * @param itemDescriptors the descriptors of the items to be added to the section, must not be {@code null}.
         * @return a component, never {@code null}.
         * @see SideBar#addTab(com.vaadin.ui.Component)
         */
        Component createSectionComponent(SideBarSectionDescriptor descriptor, Collection<SideBarItemDescriptor> itemDescriptors);
    }

    /**
     * Interface defining a factory for creating components that correspond to items in a side bar section. When
     * the item is clicked by the user, {@link org.vaadin.spring.stuff.sidebar.SideBarItemDescriptor#itemInvoked(com.vaadin.ui.UI)}
     * must be called.
     * <p/>
     * If you want to use your own factory, make a Spring managed bean that implements this interface.
     * It will automatically be used by the {@link org.vaadin.spring.stuff.sidebar.SideBar}.
     */
    public interface ItemComponentFactory {

        /**
         * Creates a component to be added to a side bar section by a {@link org.vaadin.spring.stuff.sidebar.SideBar.SectionComponentFactory}.
         * Remember to call {@link org.vaadin.spring.stuff.sidebar.SideBarItemDescriptor#itemInvoked(com.vaadin.ui.UI)} when the item
         * is clicked by the user.
         *
         * @param descriptor the descriptor of the side bar item, must not be {@code null}.
         * @return a component, never {@code null}.
         */
        Component createItemComponent(SideBarItemDescriptor descriptor);
    }

    /**
     * Extended version of {@link com.vaadin.ui.NativeButton} that is used by the {@link org.vaadin.spring.stuff.sidebar.SideBar.DefaultItemComponentFactory}.
     */
    static class ItemButton extends NativeButton {
        ItemButton(final SideBarItemDescriptor descriptor) {
            setCaption(descriptor.getCaption());
            setIcon(descriptor.getIcon());
            setDisableOnClick(true);
            addStyleName(SIDE_BAR_SECTION_ITEM_STYLE);
            setWidth(100, Unit.PERCENTAGE);
            addClickListener(new Button.ClickListener() {
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
     * Extended version of {@link org.vaadin.spring.stuff.sidebar.SideBar.ItemButton} that is used for view items. This
     * button keeps track of the currently selected view in the current UI's {@link com.vaadin.navigator.Navigator} and
     * updates its style so that the button of the currently visible view can be highlighted.
     */
    static class ViewItemButton extends ItemButton implements ViewChangeListener {

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
     * Default implementation of {@link org.vaadin.spring.stuff.sidebar.SideBar.SectionComponentFactory} that creates
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
     * Default implementation of {@link org.vaadin.spring.stuff.sidebar.SideBar.ItemComponentFactory} that creates
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

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
import com.vaadin.ui.Layout;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.spring.sidebar.SideBarItemDescriptor;
import org.vaadin.spring.sidebar.SideBarSectionDescriptor;
import org.vaadin.spring.sidebar.SideBarUtils;

import java.util.Collection;

/**
 * <p>
 * This is a side bar component that has been especially designed to be used with the {@link com.vaadin.ui.themes.ValoTheme Valo} theme.
 * It is based on {@link com.vaadin.ui.CssLayout}s and the {@code MENU_} -styles.
 * </p>
 * <p>
 * The sections and items are declared using the {@link org.vaadin.spring.sidebar.annotation.SideBarSection} and {@link org.vaadin.spring.sidebar.annotation.SideBarItem} annotations, respectively.
 * To use this side bar, simply enable it in your application configuration using the {@link org.vaadin.spring.sidebar.annotation.EnableSideBar} annotation,
 * and inject it into your UI. Also remember to use the Valo theme.
 * </p>
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see #setHeader(com.vaadin.ui.Layout)
 * @see #setLogo(com.vaadin.ui.Component)
 * @see #setLargeIcons(boolean)
 */
public class ValoSideBar extends AbstractSideBar<CssLayout> {

    private Layout headerLayout;
    private Component logo;
    private boolean largeIcons = false;

    /**
     * You should not need to create instances of this component directly. Instead, just inject the side bar into
     * your UI.
     */
    public ValoSideBar(SideBarUtils sideBarUtils) {
        super(sideBarUtils);
        setPrimaryStyleName(ValoTheme.MENU_ROOT);
        setSizeUndefined();
    }

    @Override
    protected CssLayout createCompositionRoot() {
        CssLayout layout = new CssLayout();
        layout.addStyleName(ValoTheme.MENU_PART);
        if (largeIcons) {
            layout.addStyleName(ValoTheme.MENU_PART_LARGE_ICONS);
        }
        layout.setWidth(null);
        layout.setHeight("100%");
        if (logo != null) {
            layout.addComponent(logo);
        }
        if (headerLayout != null) {
            layout.addComponent(headerLayout);
        }
        return layout;
    }

    /**
     * Returns whether the side bar is using large icons or not. Default is false.
     *
     * @see ValoTheme#MENU_PART_LARGE_ICONS
     */
    public boolean isLargeIcons() {
        return largeIcons;
    }

    /**
     * Specifies whether the side bar should use large icons or not.
     *
     * @see ValoTheme#MENU_PART_LARGE_ICONS
     */
    public void setLargeIcons(boolean largeIcons) {
        this.largeIcons = largeIcons;
        if (getCompositionRoot() != null) {
            if (largeIcons) {
                getCompositionRoot().addStyleName(ValoTheme.MENU_PART_LARGE_ICONS);
            } else {
                getCompositionRoot().removeStyleName(ValoTheme.MENU_PART_LARGE_ICONS);
            }
        }
    }

    /**
     * Adds a header to the top of the side bar, below the logo. The {@link ValoTheme#MENU_TITLE} style
     * will automatically be added to the layout.
     *
     * @param headerLayout the layout containing the header, or {@code null} to remove.
     */
    public void setHeader(Layout headerLayout) {
        if (getCompositionRoot() != null && this.headerLayout != null) {
            getCompositionRoot().removeComponent(this.headerLayout);
        }
        this.headerLayout = headerLayout;
        if (headerLayout != null) {
            headerLayout.addStyleName(ValoTheme.MENU_TITLE);
            if (getCompositionRoot() != null) {
                if (this.logo != null) {
                    getCompositionRoot().addComponent(headerLayout, 1);
                } else {
                    getCompositionRoot().addComponentAsFirst(headerLayout);
                }
            }
        }
    }

    /**
     * Returns the header layout, or {@code null} if none has been set.
     *
     * @see #setHeader(com.vaadin.ui.Layout)
     */
    public Layout getHeader() {
        return headerLayout;
    }

    /**
     * Adds a logo to the very top of the side bar, above the header. The logo's primary style is automatically
     * set to {@link ValoTheme#MENU_LOGO} ands its size to undefined.
     *
     * @param logo a {@link com.vaadin.ui.Label} or {@link com.vaadin.ui.Button} to use as the logo, or {@code null} to remove the logo completely.
     */
    public void setLogo(Component logo) {
        if (getCompositionRoot() != null && this.logo != null) {
            getCompositionRoot().removeComponent(this.logo);
        }
        this.logo = logo;
        if (logo != null) {
            logo.setPrimaryStyleName(ValoTheme.MENU_LOGO);
            logo.setSizeUndefined();
            if (getCompositionRoot() != null) {
                getCompositionRoot().addComponentAsFirst(logo);
            }
        }
    }

    /**
     * Returns the logo, or {@code null} if none has been set.
     *
     * @see #setLogo(com.vaadin.ui.Component)
     */
    public Component getLogo() {
        return logo;
    }

    @Override
    protected SectionComponentFactory<CssLayout> createDefaultSectionComponentFactory() {
        return new DefaultSectionComponentFactory();
    }

    @Override
    protected ItemComponentFactory createDefaultItemComponentFactory() {
        return new DefaultItemComponentFactory();
    }

    /**
     * Extended version of {@link com.vaadin.ui.Button} that is used by the {@link ValoSideBar.DefaultItemComponentFactory}.
     */
    static class ItemButton extends Button {

        ItemButton(final SideBarItemDescriptor descriptor) {
            setPrimaryStyleName(ValoTheme.MENU_ITEM);
            setCaption(descriptor.getCaption());
            setIcon(descriptor.getIcon());
            setId(descriptor.getItemId());
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
     * Default implementation of {@link ValoSideBar.SectionComponentFactory} that adds the section header
     * and items directly to the composition root.
     */
    public class DefaultSectionComponentFactory implements SectionComponentFactory<CssLayout> {

        private ItemComponentFactory itemComponentFactory;

        @Override
        public void setItemComponentFactory(ItemComponentFactory itemComponentFactory) {
            this.itemComponentFactory = itemComponentFactory;
        }

        @Override
        public void createSection(CssLayout compositionRoot, SideBarSectionDescriptor descriptor, Collection<SideBarItemDescriptor> itemDescriptors) {
            Label header = new Label();
            header.setValue(descriptor.getCaption());
            header.setSizeUndefined();
            header.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
            compositionRoot.addComponent(header);
            for (SideBarItemDescriptor item : itemDescriptors) {
                compositionRoot.addComponent(itemComponentFactory.createItemComponent(item));
            }
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

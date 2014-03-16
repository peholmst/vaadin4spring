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

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.Collection;

/**
 * TODO Document me
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class SideBar extends Accordion {

    private final SideBarUtils sideBarUtils;
    private final SectionComponentFactory sectionComponentFactory;
    private final ItemComponentFactory itemComponentFactory;

    public SideBar(SideBarUtils sideBarUtils, SectionComponentFactory sectionComponentFactory, ItemComponentFactory itemComponentFactory) {
        this.sideBarUtils = sideBarUtils;
        addStyleName("sideBar");

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
     *
     */
    public interface SectionComponentFactory {
        /**
         * @param itemComponentFactory
         */
        void setItemComponentFactory(ItemComponentFactory itemComponentFactory);

        /**
         * @param descriptor
         * @return
         */
        Component createSectionComponent(SideBarSectionDescriptor descriptor, Collection<SideBarItemDescriptor> itemDescriptors);
    }

    /**
     *
     */
    public interface ItemComponentFactory {

        /**
         * @param descriptor
         * @return
         */
        Component createItemComponent(SideBarItemDescriptor descriptor);
    }

    /**
     *
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
            panel.addStyleName("sideBarSection");
            final VerticalLayout layout = new VerticalLayout();
            panel.setContent(layout);
            for (SideBarItemDescriptor item : itemDescriptors) {
                layout.addComponent(itemComponentFactory.createItemComponent(item));
            }
            return panel;
        }
    }

    /**
     *
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

    /**
     *
     */
    static class ItemButton extends NativeButton {
        ItemButton(final SideBarItemDescriptor descriptor) {
            setCaption(descriptor.getCaption());
            setIcon(descriptor.getIcon());
            setDisableOnClick(true);
            addStyleName("sideBarSectionItem");
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
     *
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
            getUI().getNavigator().addViewChangeListener(this);
        }

        @Override
        public void detach() {
            getUI().getNavigator().removeViewChangeListener(this);
            super.detach();
        }

        @Override
        public boolean beforeViewChange(ViewChangeEvent event) {
            return false;
        }

        @Override
        public void afterViewChange(ViewChangeEvent event) {
            if (event.getViewName().equals(viewName)) {
                addStyleName("selected");
            } else {
                removeStyleName("selected");
            }
        }
    }
}

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

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import org.vaadin.spring.sidebar.SideBarItemDescriptor;
import org.vaadin.spring.sidebar.SideBarSectionDescriptor;
import org.vaadin.spring.sidebar.SideBarUtils;

import java.util.Collection;

/**
 * Created by petterwork on 03/08/15.
 */
public abstract class AbstractSideBar extends CustomComponent {

    private final SideBarUtils sideBarUtils;
    private SectionComponentFactory sectionComponentFactory;
    private ItemComponentFactory itemComponentFactory;

    /**
     * You should not need to create instances of this component directly. Instead, just inject the side bar into
     * your UI.
     */
    public AbstractSideBar(SideBarUtils sideBarUtils) {
        this.sideBarUtils = sideBarUtils;
    }

    protected abstract Component createCompositionRoot();

    @Override
    public void attach() {
        super.attach();
        setCompositionRoot(createCompositionRoot());
        for (SideBarSectionDescriptor section : sideBarUtils.getSideBarSections(getUI().getClass())) {
            createSection(section, sideBarUtils.getSideBarItems(section));
        }
    }

    protected abstract SectionComponentFactory createDefaultSectionComponentFactory();

    protected SectionComponentFactory getSectionComponentFactory() {
        if (sectionComponentFactory == null) {
            sectionComponentFactory = createDefaultSectionComponentFactory();
        }
        sectionComponentFactory.setItemComponentFactory(getItemComponentFactory());
        return sectionComponentFactory;
    }

    protected void setSectionComponentFactory(SectionComponentFactory sectionComponentFactory) {
        this.sectionComponentFactory = sectionComponentFactory;
    }

    protected abstract ItemComponentFactory createDefaultItemComponentFactory();

    protected ItemComponentFactory getItemComponentFactory() {
        if (itemComponentFactory == null) {
            itemComponentFactory = createDefaultItemComponentFactory();
        }
        return itemComponentFactory;
    }

    protected void setItemComponentFactory(ItemComponentFactory itemComponentFactory) {
        this.itemComponentFactory = itemComponentFactory;
    }

    protected SideBarUtils getSideBarUtils() {
        return sideBarUtils;
    }

    protected abstract void createSection(SideBarSectionDescriptor section, Collection<SideBarItemDescriptor> items);

    @Override
    public void detach() {
        setCompositionRoot(null);
        super.detach();
    }

    /**
     * Interface defining a factory for creating components that correspond to sections in a side bar.
     * In practice, a section is a tab of a {@link AccordionSideBar}.
     * <p/>
     * If you want to use your own factory, make a Spring managed bean that implements this interface.
     * It will automatically be used by the {@link AccordionSideBar}.
     *
     * @see AccordionSideBar#addTab(com.vaadin.ui.Component)
     */
    public interface SectionComponentFactory {
        /**
         * Sets the {@code ItemComponentFactory} to use when creating the items of the section.
         *
         * @param itemComponentFactory the item component factory, must not be {@code null}.
         */
        void setItemComponentFactory(ItemComponentFactory itemComponentFactory);

        /**
         * Creates a component to be added as a tab to the {@link AccordionSideBar}.
         *
         * @param descriptor      the descriptor of the side bar section, must not be {@code null}.
         * @param itemDescriptors the descriptors of the items to be added to the section, must not be {@code null}.
         * @return a component, never {@code null}.
         * @see AccordionSideBar#addTab(com.vaadin.ui.Component)
         */
        Component createSectionComponent(SideBarSectionDescriptor descriptor, Collection<SideBarItemDescriptor> itemDescriptors);
    }

    /**
     * Interface defining a factory for creating components that correspond to items in a side bar section. When
     * the item is clicked by the user, {@link org.vaadin.spring.sidebar.SideBarItemDescriptor#itemInvoked(com.vaadin.ui.UI)}
     * must be called.
     * <p/>
     * If you want to use your own factory, make a Spring managed bean that implements this interface.
     * It will automatically be used by the {@link AccordionSideBar}.
     */
    public interface ItemComponentFactory {

        /**
         * Creates a component to be added to a side bar section by a {@link AccordionSideBar.SectionComponentFactory}.
         * Remember to call {@link org.vaadin.spring.sidebar.SideBarItemDescriptor#itemInvoked(com.vaadin.ui.UI)} when the item
         * is clicked by the user.
         *
         * @param descriptor the descriptor of the side bar item, must not be {@code null}.
         * @return a component, never {@code null}.
         */
        Component createItemComponent(SideBarItemDescriptor descriptor);
    }
}

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
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import org.vaadin.spring.sidebar.SideBarItemDescriptor;
import org.vaadin.spring.sidebar.SideBarSectionDescriptor;
import org.vaadin.spring.sidebar.SideBarUtils;

import java.util.Collection;

/**
 * Base class for visual side bar components. The side bar has access to an instance of {@link org.vaadin.spring.sidebar.SideBarUtils}
 * that will provide information about the sections and items to show.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public abstract class AbstractSideBar<CR extends ComponentContainer> extends CustomComponent {

    private final SideBarUtils sideBarUtils;
    private SectionComponentFactory<CR> sectionComponentFactory;
    private ItemComponentFactory itemComponentFactory;

    /**
     * Protected constructor. The instance of {@link org.vaadin.spring.sidebar.SideBarUtils} should come from the Spring application context.
     */
    protected AbstractSideBar(SideBarUtils sideBarUtils) {
        this.sideBarUtils = sideBarUtils;
    }

    /**
     * Creates the component that actually contain the side bar sections and items. This method is called every time
     * the side bar is attached to a UI. Every time the side bar is detached, its composition root will be set back to {@code null}.
     */
    protected abstract CR createCompositionRoot();

    /**
     * {@inheritDoc}
     * <p>
     * When the side bar is not attached to a UI, this method will always return {@code null}.
     * </p>
     *
     * @see #createCompositionRoot()
     */
    @Override
    @SuppressWarnings("unchecked")
    protected CR getCompositionRoot() {
        return (CR) super.getCompositionRoot();
    }

    @Override
    public void attach() {
        super.attach();
        CR compositionRoot = createCompositionRoot();
        setCompositionRoot(compositionRoot);
        for (SideBarSectionDescriptor section : sideBarUtils.getSideBarSections(getUI().getClass())) {
            createSection(compositionRoot, section, sideBarUtils.getSideBarItems(section));
        }
    }

    /**
     * Creates the default {@link org.vaadin.spring.sidebar.components.AbstractSideBar.SectionComponentFactory} to use.
     * This method must never return {@code null}.
     */
    protected abstract SectionComponentFactory<CR> createDefaultSectionComponentFactory();

    /**
     * Returns the current {@link org.vaadin.spring.sidebar.components.AbstractSideBar.SectionComponentFactory}. If no
     * factory has been set, a default factory is created.
     *
     * @see #setSectionComponentFactory(org.vaadin.spring.sidebar.components.AbstractSideBar.SectionComponentFactory)
     * @see #createDefaultSectionComponentFactory()
     */
    protected SectionComponentFactory<CR> getSectionComponentFactory() {
        if (sectionComponentFactory == null) {
            sectionComponentFactory = createDefaultSectionComponentFactory();
        }
        sectionComponentFactory.setItemComponentFactory(getItemComponentFactory());
        return sectionComponentFactory;
    }

    /**
     * Sets the {@link org.vaadin.spring.sidebar.components.AbstractSideBar.SectionComponentFactory} to use.
     */
    protected void setSectionComponentFactory(SectionComponentFactory<CR> sectionComponentFactory) {
        this.sectionComponentFactory = sectionComponentFactory;
    }

    /**
     * Creates the default {@link org.vaadin.spring.sidebar.components.AbstractSideBar.ItemComponentFactory} to use.
     * This method must never return {@code null}.
     */
    protected abstract ItemComponentFactory createDefaultItemComponentFactory();

    /**
     * Returns the current {@link org.vaadin.spring.sidebar.components.AbstractSideBar.ItemComponentFactory}. If no
     * factory has been set, a default factory is created.
     *
     * @see #setItemComponentFactory(org.vaadin.spring.sidebar.components.AbstractSideBar.ItemComponentFactory)
     * @see #createDefaultItemComponentFactory()
     */
    protected ItemComponentFactory getItemComponentFactory() {
        if (itemComponentFactory == null) {
            itemComponentFactory = createDefaultItemComponentFactory();
        }
        return itemComponentFactory;
    }

    /**
     * Sets the {@link org.vaadin.spring.sidebar.components.AbstractSideBar.ItemComponentFactory} to use.
     */
    protected void setItemComponentFactory(ItemComponentFactory itemComponentFactory) {
        this.itemComponentFactory = itemComponentFactory;
    }

    private void createSection(CR compositionRoot, SideBarSectionDescriptor section, Collection<SideBarItemDescriptor> items) {
        getSectionComponentFactory().createSection(compositionRoot, section, items);
    }

    @Override
    public void detach() {
        setCompositionRoot(null);
        super.detach();
    }

    /**
     * Interface defining a factory for creating components that correspond to sections in a side bar.
     */
    public interface SectionComponentFactory<CR extends ComponentContainer> {
        /**
         * Sets the {@code ItemComponentFactory} to use when creating the items of the section. This method
         * is always called before the first invocation of {@link #createSection(com.vaadin.ui.ComponentContainer, org.vaadin.spring.sidebar.SideBarSectionDescriptor, java.util.Collection)}.
         *
         * @param itemComponentFactory the item component factory, must not be {@code null}.
         */
        void setItemComponentFactory(ItemComponentFactory itemComponentFactory);

        /**
         * Creates and adds the specified section and items to the composition root.
         *
         * @param compositionRoot the component to which the section and items are to be added, must not be {@code null}.
         * @param descriptor      the descriptor of the side bar section, must not be {@code null}.
         * @param itemDescriptors the descriptors of the items to be added to the section, must not be {@code null}.
         */
        void createSection(CR compositionRoot, SideBarSectionDescriptor descriptor, Collection<SideBarItemDescriptor> itemDescriptors);
    }

    /**
     * Interface defining a factory for creating components that correspond to items in a side bar section. When
     * the item is clicked by the user, {@link org.vaadin.spring.sidebar.SideBarItemDescriptor#itemInvoked(com.vaadin.ui.UI)}
     * must be called.
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

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
package org.vaadin.spring.samples.mvp.ui.component.nav;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.vaadin.data.util.HierarchicalContainer;

/**
 * Responsible for construction of a navigation hierarchy
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public class NavContainerFactory {

    private final List<NavElement> elements;

    public NavContainerFactory(@NotNull List<NavElement> elements) {
        this.elements = elements;
    }

    public NavElement getNodeById(@NotNull Double id) {
        NavElement result = null;
        for (NavElement e: elements) {
            if (e.getId().equals(id)) {
                result = e;
                break;
            }
        }
        return result;
    }

    public NavElement getNodeByAlias(@NotNull String alias) {
        NavElement result = null;
        for (NavElement e: elements) {
            if (e.getAlias().equals(alias)) {
                result = e;
                break;
            }
        }
        return result;
    }

    protected NavElement getParent(@NotNull NavElement element) {
        NavElement result = null;
        Double parent = element.getParent();
        if (parent != null) {
            for (NavElement e: elements) {
                if (parent.equals(e.getId())) {
                    result = e;
                    break;
                }
            }
        }
        return result;
    }

    protected List<NavElement> getRoots() {
        List<NavElement> result = new ArrayList<>();
        for (NavElement e: elements) {
            if (e.getParent() == null) {
                result.add(e);
            }
        }
        return result;
    }

    protected List<NavElement> getChildren(@NotNull NavElement element) {
        List<NavElement> result = new ArrayList<>();
        Double parent = null;
        for (NavElement e: elements) {
            parent = e.getParent();
            if (parent != null && element.getId().equals(parent)) {
                result.add(e);
            }
        }
        return result;
    }

    protected boolean hasChildren(@NotNull NavElement element) {
        boolean result = false;
        if (!getChildren(element).isEmpty()) {
            result = true;
        }
        return result;
    }


    public HierarchicalContainer constructNavigationHierarchy() {
        HierarchicalContainer navData = new HierarchicalContainer();
        navData.addContainerProperty("name", String.class, null);
        navData.addContainerProperty("id", Double.class, null);
        navData.addContainerProperty("parent", Double.class, null);
        navData.addContainerProperty("alias", String.class, null);
        List<NavElement> children = null;
        // FIXME only capable of rendering two levels
        for (NavElement e: getRoots()) {
            navData.addItem(e);
            if (hasChildren(e)) {
                children = getChildren(e);
                for (NavElement child: children) {
                    navData.addItem(child);
                    navData.setParent(child, e);
                    navData.setChildrenAllowed(child, false);
                }
            } else {
                navData.setChildrenAllowed(e, false);
            }
        }
        return navData;
    }
}

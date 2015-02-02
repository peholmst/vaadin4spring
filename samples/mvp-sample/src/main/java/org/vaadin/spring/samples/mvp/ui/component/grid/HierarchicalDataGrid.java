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
package org.vaadin.spring.samples.mvp.ui.component.grid;

import java.util.List;

import com.vaadin.ui.TreeTable;

/**
 * Specialized form of {@link DataGrid}.  Allows for presentation of sub-rows.
 *
 * @author Chris Phillipson (fastnsilver@gmail.com)
 */
public abstract class HierarchicalDataGrid<DTO> extends DataGrid<DTO, TreeTable> {

    private static final long serialVersionUID = 4767979974675466140L;

    @Override
    public void populateGrid(List<DTO> data) {
        populateGrid(data, new TreeTable());
    }

    /*
     * (Note: There is a variance in calls in DataGrid and HierarchicalDataGrid. Turns out a Table must be supplied a
     * BeanItemContainer whereas for TreeTable it already has a built-in HierarchalContainer so the order of calls is
     * different.)
     */
    @Override
    protected void doPopulateGrid(List<DTO> data, TreeTable table) {
        addControls();
        defineColumns(data);
        defineFieldPresentation();
        defineTableProperties();
        insertData(data);
        addComponent(table);
        setExpandRatio(table, 1);
    }

}

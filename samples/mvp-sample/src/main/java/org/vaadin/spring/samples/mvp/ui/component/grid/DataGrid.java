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

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Base class for all grid-based views.
 * Creates a layout comprised of a {@link Table} and controls to work with data within.
 * Derivatives must be annotated with both {@link org.vaadin.spring.navigator.annotation.VaadinView}
 * and {@link org.vaadin.spring.annotation.VaadinUIScope}.
 *
 * @author Chris Phillipson (fastnsilver@gmail.com)
 */
public abstract class DataGrid<DTO, T extends Table> extends VerticalLayout implements View {

    private static final long serialVersionUID = 1L;
    private static final String NO_RESULTS_CAPTION = "No results";

    protected T table;

    /**
     * Constructs an empty grid
     * @return an empty grid
     */
    public static DataGrid<Void, Table> emptyGrid() {
        return new EmptyGrid();
    }

    @PostConstruct
    private void init() {
        setMargin(true);
        setSizeFull();
    }

    /**
     * Populates this grid with data
     * @param data any list of transfer objects
     */
    @SuppressWarnings("unchecked")
    public void populateGrid(List<DTO> data) {
        populateGrid(data, (T) new Table());
    }

    protected void populateGrid(List<DTO> data, T table) {
        // preemptively remove all components from layout
        if (getComponentCount() > 0) {
            removeAllComponents();
        }
        // initialize table
        this.table = table;
        // if no data show default message
        if (CollectionUtils.isEmpty(data)) {
            addComponent(new Label(NO_RESULTS_CAPTION, ContentMode.TEXT));
        } else {
            // we have data... insert it into table,
            // then define table, column and field presentation properties
            doPopulateGrid(data, table);
        }
    }

    protected void doPopulateGrid(List<DTO> data, T table) {
        addControls();
        insertData(data);
        defineColumns(data);
        defineFieldPresentation();
        defineTableProperties();
        addComponent(table);
        setExpandRatio(table, 1);
    }

    /**
     * Define the columns (and associated display properties) for this grid
     */
    protected abstract void defineColumns(List<DTO> data);


    /**
     * Define presentation rules for each field within a column
     */
    protected abstract void defineFieldPresentation();

    /**
     * Define properties that are global for grid
     */
    protected abstract void defineTableProperties();

    /**
     * Insert data into table
     * Typically one would define a {@link BeanItemContainer}, optionally adding any nested property
     * from the type of transfer object using {@link BeanItemContainer#addNestedContainerProperty(String)},
     * add the data to the container using {@link BeanItemContainer#addAll(List)}, then set the container
     * as a data source using {@link Table#setContainerDataSource(com.vaadin.data.Container)}.
     * @param data any list of transfer objects
     */
    protected abstract void insertData(List<DTO> data);

    protected abstract void addControls();


}

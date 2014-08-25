package org.vaadin.spring.samples.mvp.ui.component.grid;

import java.util.List;

import com.vaadin.ui.TreeTable;

/**
 * Specialized form of {@link DataGrid}.  Allows for presentation of sub-rows.
 *
 * @author Chris Phillipson (fastnsilver@gmail.com)
 */
public abstract class HierarchicalDataGrid<DTO> extends DataGrid<DTO, TreeTable> {

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

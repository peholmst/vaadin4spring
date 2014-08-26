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

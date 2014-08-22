package org.vaadin.spring.samples.mvp.ui.component.util;

import java.util.Map;
import java.util.TreeMap;

/**
 * Associates a parent row with N child rows.  A child row is an instance of {@link HierarchicalRow}.
 * Useful for translation of lists.  Rows are typically added to a {@link com.vaadin.ui.TreeTable}.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public class HierarchicalRows {

    private Map<String, HierarchicalRow> rows;

    public HierarchicalRows() {
        rows = new TreeMap<>();
    }

    public void addRow(String parent, String child, Object... data) {
        HierarchicalRow row = rows.get(parent);
        if (row == null) {
            row = HierarchicalRow.emptyRow();
        }
        row.addRowItem(child, data);
        rows.put(parent, row);
    }

    public Map<String, HierarchicalRow> getRows() {
        return rows;
    }

}

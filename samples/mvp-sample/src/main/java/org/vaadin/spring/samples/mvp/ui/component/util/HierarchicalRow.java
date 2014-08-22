package org.vaadin.spring.samples.mvp.ui.component.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Associates a key with a var-arg number of values and then provides
 * for translation of key's values into an Object[]. Used by {@link HierarchicalRows}.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public class HierarchicalRow {

    private final Map<String, Object[]> rowData;

    public HierarchicalRow() {
        this.rowData = new HashMap<>();
    }

    public void addRowItem(String child, Object... data) {
        List<Object> o = new ArrayList<>();
        for (Object item: data) {
            o.add(item);
        }
        Object[] objects = o.toArray(new Object[o.size()]);
        rowData.put(child, objects);
    }

    public Object[] getRowAsObjectArray(String dataDescriptor, int childRowIndex) {
        List<Object> r = new ArrayList<>();
        if (StringUtils.isNotBlank(dataDescriptor)) {
            r.add(dataDescriptor);
        }
        for (Object[] value: rowData.values()) {
            r.add(value[childRowIndex]);
        }
        Object[] result = r.toArray(new Object[r.size()]);
        return result;
    }

    public static HierarchicalRow emptyRow() {
        return new HierarchicalRow();
    }
}
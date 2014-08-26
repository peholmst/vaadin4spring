package org.vaadin.spring.samples.mvp.ui.component.util;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.vaadin.spring.samples.mvp.dto.HasHour;
import org.vaadin.spring.samples.mvp.dto.UniquelyKeyed;
import org.vaadin.spring.samples.mvp.util.SSTimeUtil;

import com.vaadin.ui.Table;


/**
 * Utility methods for constructing content for display within a {@link Table}.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public class DataGridUtil {

    public static Set<String> addHourColumnHeaders(Table table, List<? extends UniquelyKeyed<? extends HasHour>> data, Class<?> type) {
        Set<String> columnHeaders = new TreeSet<>();
        String hour;
        for (UniquelyKeyed<? extends HasHour> item: data) {
            hour = SSTimeUtil.isoToHourLabel(item.getKey().getHour());
            if (!columnHeaders.contains(hour)) {
                table.addContainerProperty(hour, type, null);
            }
            columnHeaders.add(hour);
        }
        return columnHeaders;
    }

}

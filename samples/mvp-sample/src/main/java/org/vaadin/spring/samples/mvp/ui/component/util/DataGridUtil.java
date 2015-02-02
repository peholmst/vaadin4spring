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

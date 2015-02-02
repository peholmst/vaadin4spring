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
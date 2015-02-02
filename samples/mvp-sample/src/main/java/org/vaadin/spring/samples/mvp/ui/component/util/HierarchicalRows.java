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

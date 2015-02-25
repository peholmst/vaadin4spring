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
package org.vaadin.spring.samples.mvp.ui.view;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.vaadin.spring.navigator.annotation.VaadinView;
import org.vaadin.spring.samples.mvp.dto.DSRUpdateHourlyDTO;
import org.vaadin.spring.samples.mvp.ui.component.grid.HierarchicalDataGrid;
import org.vaadin.spring.samples.mvp.ui.component.util.DataGridUtil;
import org.vaadin.spring.samples.mvp.ui.component.util.HierarchicalRow;
import org.vaadin.spring.samples.mvp.ui.component.util.HierarchicalRows;
import org.vaadin.spring.samples.mvp.util.SSTimeUtil;

import com.vaadin.data.Container.Hierarchical;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Table;

/**
 * Demand Side Response Hourly Update grid
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
@VaadinUIScope
@VaadinView(name = DSRUpdateHourlyView.NAME)
public class DSRUpdateHourlyView extends HierarchicalDataGrid<DSRUpdateHourlyDTO> {

    private static final long serialVersionUID = 1L;

    public static final String NAME = "demand/dsr/hourly_updates";

    private static final String LOCATION_COLUMN = "Location";
    private static final String COMMIT_STATUS_COLUMN = "Commit Status";
    private static final String ECO_MIN_COLUMN = "Economic Minimum";
    private static final String ECO_MAX_COLUMN = "Economic Maximum";


    @Override
    protected void defineColumns(List<DSRUpdateHourlyDTO> data) {
        table.addContainerProperty(LOCATION_COLUMN, String.class, null);
        Set<String> hours = DataGridUtil.addHourColumnHeaders(table, data, String.class);

        // TODO move to its own method?
        table.setColumnAlignment(LOCATION_COLUMN, Table.Align.LEFT);
        for (String hour: hours) {
            table.setColumnAlignment(hour, Table.Align.RIGHT);
            table.setColumnWidth(hour, 60);
        }

        table.setColumnWidth(LOCATION_COLUMN, 150);

    }

    @Override
    protected void insertData(List<DSRUpdateHourlyDTO> data) {
        int rowIndex = 0;
        HierarchicalRows struct = new HierarchicalRows();
        NumberFormat df = NumberFormat.getNumberInstance();
        for (DSRUpdateHourlyDTO dto : data) {
            struct.addRow(dto.getId().getLocation(),
                    SSTimeUtil.isoToHourLabel(dto.getId().getHour()), dto.getCommitStatus(),
                    df.format(dto.getEconomicMin()), df.format(dto.getEconomicMax()));
        }

        String interfaceName;
        Object[] parentRow;
        Object[] commitStatus;
        Object[] ecoMin;
        Object[] ecoMax;
        HierarchicalRow hr;
        Object itemId;
        for (Map.Entry<String, HierarchicalRow> parent: struct.getRows().entrySet()) {
            interfaceName = parent.getKey();
            hr = parent.getValue();
            commitStatus = hr.getRowAsObjectArray(COMMIT_STATUS_COLUMN , 0);
            ecoMin = hr.getRowAsObjectArray(ECO_MIN_COLUMN , 1);
            ecoMax = hr.getRowAsObjectArray(ECO_MAX_COLUMN , 2);
            parentRow = new Object[NumberUtils.max(new int[] {commitStatus.length, ecoMin.length, ecoMax.length})];
            parentRow[0] = interfaceName;
            table.addItem(parentRow, rowIndex);
            rowIndex++;

            itemId = table.addItem(commitStatus, rowIndex);
            ((Hierarchical) table).setParent(rowIndex, rowIndex-1);
            table.setChildrenAllowed(itemId, false);
            rowIndex++;

            itemId = table.addItem(ecoMin, rowIndex);
            ((Hierarchical) table).setParent(rowIndex, rowIndex-2);
            table.setChildrenAllowed(itemId, false);
            rowIndex++;

            itemId = table.addItem(ecoMax, rowIndex);
            ((Hierarchical) table).setParent(rowIndex, rowIndex-3);
            table.setChildrenAllowed(itemId, false);

            table.setCollapsed(rowIndex-3, false);
            rowIndex++;
        }

        // we want to see all hours at once; disable paging
        table.setPageLength(0);
    }

    @Override
    protected void defineFieldPresentation() {

    }

    @Override
    protected void defineTableProperties() {
        table.setImmediate(true);
    }

    @Override
    protected void addControls() {

    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

}

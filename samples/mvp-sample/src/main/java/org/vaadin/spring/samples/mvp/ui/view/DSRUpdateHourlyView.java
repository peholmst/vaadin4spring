package org.vaadin.spring.samples.mvp.ui.view;

import java.math.BigDecimal;
import java.util.List;

import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;
import org.vaadin.spring.samples.mvp.dto.CommitStatusType;
import org.vaadin.spring.samples.mvp.dto.DSRUpdateHourlyDTO;
import org.vaadin.spring.samples.mvp.ui.component.grid.DataGrid;
import org.vaadin.spring.samples.mvp.ui.component.layout.Styles;
import org.vaadin.spring.samples.mvp.ui.component.selector.DefaultSelector;
import org.vaadin.spring.samples.mvp.ui.component.util.DataGridUtil;
import org.vaadin.spring.samples.mvp.util.SSTimeUtil;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;

/**
 * Demand Side Response Hourly Update grid
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
@UIScope
@VaadinView(name = DSRUpdateHourlyView.NAME)
public class DSRUpdateHourlyView extends DataGrid<DSRUpdateHourlyDTO, Table> {

    private static final long serialVersionUID = 1L;

    public static final String NAME = "demand/dsr/hourly_updates";

    private static final String ID = "id";
    private static final String HOUR_GEN_COLUMN = "hour";
    private static final String HOUR_COLUMN = "id.hour";
    private static final String COMMIT_STATUS_COLUMN = "commitStatus";
    private static final String ECO_MIN_COLUMN = "economicMin";
    private static final String ECO_MAX_COLUMN = "economicMax";


    @Override
    protected void defineColumns(List<DSRUpdateHourlyDTO> data) {
        // TODO externalize
        // Idea: create new utility class UiHints w/ constructor arg of screen id
        // it would look for a JSON file with screen id as filename
        // JSON file would have display properties for that screen
        // e.g., visibleColumns and any other queue for presentation or enabling feature

        table.addGeneratedColumn(HOUR_GEN_COLUMN, new ColumnGenerator() {
            @Override
            public Component generateCell(Table source,
                    final Object itemId, Object columnId) {
                // Get the value in the first column
                String isoHour = (String) source
                        .getContainerProperty(itemId, HOUR_COLUMN).getValue();

                TextField tf = new TextField(null, SSTimeUtil.isoToHourLabel(isoHour));
                tf.addStyleName(Styles.NUMERIC);
                tf.setReadOnly(true);
                tf.setWidth("40");
                return tf;
            }
        });

        table.setColumnHeader(COMMIT_STATUS_COLUMN, "Commit Status");
        table.setColumnHeader(ECO_MIN_COLUMN, "Economic Minimum");
        table.setColumnHeader(ECO_MAX_COLUMN, "Economic Maximum");

        table.setColumnWidth(HOUR_GEN_COLUMN, 40);

        table.setColumnAlignment(HOUR_GEN_COLUMN, Table.Align.RIGHT);
        table.setColumnAlignment(ECO_MIN_COLUMN, Table.Align.RIGHT);
        table.setColumnAlignment(ECO_MAX_COLUMN, Table.Align.RIGHT);

        // these are the properties (including nested properties and generated columns) of the DTO, in column name order
        table.setVisibleColumns(new Object[]{ HOUR_GEN_COLUMN, COMMIT_STATUS_COLUMN, ECO_MIN_COLUMN, ECO_MAX_COLUMN });

    }

    @Override
    protected void insertData(List<DSRUpdateHourlyDTO> data) {
        BeanItemContainer<DSRUpdateHourlyDTO> container = new BeanItemContainer<>(DSRUpdateHourlyDTO.class);
        container.addNestedContainerBean(ID);
        container.addAll(data);
        table.setContainerDataSource(container);
        // we want to see all hours at once; disable paging
        table.setPageLength(0);
    }

    // TODO for now only editable presentation defined, split into editable and read-only and provide toggle
    @Override
    protected void defineFieldPresentation() {
        table.setTableFieldFactory(new DefaultFieldFactory() {

            @Override
            public Field<?> createField(Container container, Object itemId,
                    Object propertyId, Component uiContext) {

                Class<?> cls = container.getType(propertyId);

                Field<?> field = null;

                if (cls.equals(BigDecimal.class)) {
                    TextField tf = new TextField();
                    tf.addStyleName(Styles.NUMERIC);
                    field = tf;
                }

                // Make commitStatus field render a single select
                if (propertyId.equals(COMMIT_STATUS_COLUMN)) {
                    String value = ((DSRUpdateHourlyDTO) itemId).getCommitStatus();
                    CommitStatusType status = CommitStatusType.fromValue(value);
                    try {
                        field = new DefaultSelector(status, "getValue");
                    } catch (Exception e) {
                        // do nothing, and fall-through
                    }
                }

                // catch-all
                if (field == null) {
                    field = super.createField(container, itemId, propertyId, uiContext);
                }

                return field;
            }
        });
    }

    @Override
    protected void defineTableProperties() {
        table.setImmediate(true);
        // TODO consider setting this property with a UiHint
        table.setEditable(true);
    }

    @Override
    protected void addControls() {
        addComponent(DataGridUtil.addReportControls());
    }


    @Override
    public void enter(ViewChangeEvent event) {

    }

}

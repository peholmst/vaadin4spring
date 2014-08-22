package org.vaadin.spring.samples.mvp.ui.component.util;

import org.vaadin.spring.samples.mvp.ui.component.listener.TrayListener;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table;


/**
 * Utility methods for constructing content for display within a {@link Table}.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public class DataGridUtil {

    public static GridLayout addReportControls() {
        GridLayout right = new GridLayout(1, 1);
        right.setWidth(100f, Unit.PERCENTAGE);
        HorizontalLayout reportSelection = buildReportSelectionArea();
        right.addComponent(reportSelection, 0, 0);
        right.setComponentAlignment(reportSelection, Alignment.MIDDLE_RIGHT);
        return right;
    }

    protected static HorizontalLayout buildReportSelectionArea() {
        HorizontalLayout reportSelection = new HorizontalLayout();
        reportSelection.setSpacing(true);
        NativeButton getXmlBtn = new NativeButton("Get XML Report");
        // FIXME wire to appropriate back-end service
        getXmlBtn.addClickListener(new TrayListener("Fetching XML Report..."));

        NativeButton getCsvBtn = new NativeButton("Get CSV Report");
        // FIXME wire to appropriate back-end service
        getXmlBtn.addClickListener(new TrayListener("Fetching CSV Report..."));

        reportSelection.addComponent(getXmlBtn);
        reportSelection.addComponent(getCsvBtn);

        return reportSelection;
    }

}

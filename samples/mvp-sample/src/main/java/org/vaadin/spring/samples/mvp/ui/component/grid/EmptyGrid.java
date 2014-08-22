package org.vaadin.spring.samples.mvp.ui.component.grid;

import java.util.List;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Table;

/**
 * An empty grid
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
class EmptyGrid extends DataGrid<Void, Table> {


    @Override
    protected void defineColumns(List<Void> data) {

    }

    @Override
    protected void defineFieldPresentation() {

    }

    @Override
    protected void defineTableProperties() {

    }

    @Override
    protected void insertData(List<Void> data) {

    }

    @Override
    protected void addControls() {

    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

}

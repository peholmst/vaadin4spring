package org.vaadin.spring.samples.mvp.ui.view;

import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

@UIScope
@VaadinView(name = BannerView.NAME)
public class BannerView extends Panel implements View {

    public static final String NAME = "banner";


    public void setUser(String username) {
        setContent(buildRightArea(username));
    }

    protected Label buildUserArea(String username) {
        Label loggedInUser = new Label();
        loggedInUser.setValue(username);
        loggedInUser.setSizeUndefined();
        return loggedInUser;
    }

    protected GridLayout buildRightArea(String username) {
        GridLayout right = new GridLayout(1, 1);
        right.setWidth(100f, Unit.PERCENTAGE);
        Label loggedInUser = buildUserArea(username);
        right.addComponent(loggedInUser, 0, 0);
        right.setComponentAlignment(loggedInUser, Alignment.MIDDLE_RIGHT);
        return right;
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }


}

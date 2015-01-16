package org.vaadin.spring.samples.mvp.ui.presenter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.navigator.Presenter;
import org.vaadin.spring.navigator.VaadinPresenter;
import org.vaadin.spring.samples.mvp.dto.AssetOwnedDailyId;
import org.vaadin.spring.samples.mvp.dto.DSRUpdateDTO;
import org.vaadin.spring.samples.mvp.ui.component.ControlButton;
import org.vaadin.spring.samples.mvp.ui.component.listener.MarketDaySelectedListener;
import org.vaadin.spring.samples.mvp.ui.component.listener.ParticipantSelectedListener;
import org.vaadin.spring.samples.mvp.ui.component.selector.AnyParticipantSelector;
import org.vaadin.spring.samples.mvp.ui.component.selector.MarketDayPicker;
import org.vaadin.spring.samples.mvp.ui.component.selector.ParticipantSelector;
import org.vaadin.spring.samples.mvp.ui.service.DsrService;
import org.vaadin.spring.samples.mvp.ui.view.DSRUpdateHourlyView;
import org.vaadin.spring.samples.mvp.util.SSTimeUtil;

import com.vaadin.data.Property;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;

@VaadinPresenter(viewName = DSRUpdateHourlyView.NAME)
public class DSRUpdateHourlyPresenter extends Presenter<DSRUpdateHourlyView> {

    private static Logger log = LoggerFactory.getLogger(DSRUpdateHourlyPresenter.class);

    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private DsrService dsrService;

    // XXX each presenter may have multiple listener methods each with same signature;
    // therefore, each method's implementation is responsible for deciding whether or not
    // to handle event payload. furthermore, one or more presenters will implement similarly
    // annotated methods.

    @EventBusListenerMethod
    public void onInit(final Screen screen) {
        if (screen.getViewName().equals(DSRUpdateHourlyView.NAME) && screen.getAction().equals(Action.START)) {
            // initialize controls for use with this screen

            MarketDayPicker marketDayPicker = (MarketDayPicker) getViewProvider().getView(MarketDayPicker.NAME);
            marketDayPicker.addValueChangeListener(applicationContext.getBean(MarketDaySelectedListener.class));

            ParticipantSelector participantSelector = (AnyParticipantSelector) getViewProvider().getView(AnyParticipantSelector.NAME);
            participantSelector.addValueChangeListener(applicationContext.getBean(ParticipantSelectedListener.class));

            ControlButton fetchBtn = new ControlButton("Fetch", new ClickListener() {

                private static final long serialVersionUID = 6466059668727933204L;

                @Override
                public void buttonClick(ClickEvent event) {
                    Screen target = new Screen(screen.getViewName(), screen.getContext(), Action.GET_DATA);
                    getEventBus().publish(this, target);
                }
            });

            Component[] controls = new Component[] { marketDayPicker, participantSelector, fetchBtn };
            screen.getContext().getControls().addAll(Arrays.asList(controls));
            getEventBus().publish(this, screen.getContext());
        }
    }

    @SuppressWarnings("unchecked")
    @EventBusListenerMethod
    public void onFetch(Screen screen) {
        if (screen.getViewName().equals(DSRUpdateHourlyView.NAME) && screen.getAction().equals(Action.GET_DATA)) {

            // obtain control values
            Date marketDay = ((Property<Date>) screen.getControl(MarketDayPicker.class)).getValue();
            AssetOwnedDailyId id = ((Property<AssetOwnedDailyId>) screen.getControl(AnyParticipantSelector.class)).getValue();

            if (marketDay != null && id != null) {
                try {
                    List<DSRUpdateDTO> dsrUpdateHourlies = dsrService.getDSRHourly(SSTimeUtil.dateToDateTime(marketDay), id.getAssetOwner());
                    new Table();
                    // should only ever have one
                    if (CollectionUtils.isNotEmpty(dsrUpdateHourlies)) {
                        getView().populateGrid(dsrUpdateHourlies.get(0).getRecords());
                    } else {
                        getView().populateGrid(null);
                    }
                } catch (Exception e) {
                    String fullTrace = ExceptionUtils.getStackTrace(e);
                    String trace = fullTrace;
                    if (fullTrace.length() > 1000) {
                        trace = fullTrace.substring(0, 1000) + "...";
                    }
                    String message = "Problems populating grid";
                    Notification.show(message, trace, Type.ERROR_MESSAGE);
                    log.error(message, e);
                }

            } else {
                String message = "Please complete selection of market day, participant and/or DSR.";
                Notification.show("Invalid criteria", message, Type.WARNING_MESSAGE);
            }
        }
    }
}


package org.vaadin.spring.samples.mvp.ui.component.selector;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTime;
import org.springframework.core.env.Environment;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.VaadinView;
import org.vaadin.spring.samples.mvp.util.SSTimeUtil;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.PopupDateField;

/**
 * Restrict selection of an operating day
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
@VaadinUIScope
@VaadinView(name = MarketDayPicker.NAME)
public class MarketDayPicker extends PopupDateField implements View {

    private static final long serialVersionUID = 5271190219929731976L;

    @Inject
    private Environment env;

    public static final String NAME = "marketDayPicker";

    private static final String DEFAULT_CAPTION = "Market Day";
    private static final String OUT_OF_RANGE_MESSAGE = "You chose a date outside the operating window! Please choose another day.";

    @PostConstruct
    private void init() {
        // Sets this picker's default value
        // Injection of any other date-like format (including dashes or slashes) gets auto-converted by Spring
        // yielding an unusable String.  Define the default day in YYYYmmdd format.
        String dayInYMDFormat = env.getProperty("market.default.day");
        if (dayInYMDFormat != null) {
            StringBuilder dayWithDashes = new StringBuilder();
            dayWithDashes.append(dayInYMDFormat.substring(0,4));
            dayWithDashes.append("-");
            dayWithDashes.append(dayInYMDFormat.substring(4,6));
            dayWithDashes.append("-");
            dayWithDashes.append(dayInYMDFormat.substring(6,8));
            String defaultIsoDay = SSTimeUtil.dayToIso(dayWithDashes.toString());
            setValue(SSTimeUtil.isoDayToDateTime(defaultIsoDay).toDate());
        }
        String offsetDays = env.getProperty("market.default.offsetDays");
        if (offsetDays != null) {
            setOffsetDays(Integer.valueOf(offsetDays));
        }
    }

    public void setOffsetDays(int offsetDays) {
        setOffsetDays(offsetDays, offsetDays);
    }

    public void setOffsetRange(String[] offsetRange) {
        if (ArrayUtils.isNotEmpty(offsetRange)) {
            if (offsetRange.length == 1) {
                int offsetDay = Integer.valueOf(offsetRange[0]);
                setOffsetDays(offsetDay, offsetDay);
            } else {
                int startOffset = Integer.valueOf(offsetRange[0]);;
                int endOffset = Integer.valueOf(offsetRange[1]);;
                setOffsetDays(startOffset, endOffset);
            }
        }
    }

    private void setOffsetDays(int startOffset, int endOffset) {
        setCaption(DEFAULT_CAPTION);
        DateTime today = getValue() != null ? new DateTime(getValue().getTime())  : new DateTime();
        Date end = today.plusDays(startOffset).toDate();
        Date start = today.minusDays(endOffset).toDate();
        setValue(today.toDate());
        setDateFormat("yyyy-MM-dd");
        setRangeStart(start);
        setRangeEnd(end);
        setDateOutOfRangeMessage(OUT_OF_RANGE_MESSAGE);
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

}

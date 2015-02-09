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
package org.vaadin.spring.samples.push;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.UI;

import org.atmosphere.cpr.SessionSupport;
import org.vaadin.spring.annotation.VaadinUI;

import javax.annotation.PreDestroy;
import javax.servlet.annotation.WebListener;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * UI that pushes updates from a background thread to the client, using long polling (web sockets
 * do not work with the Spring add-on).
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Push(transport = Transport.WEBSOCKET)
@Widgetset("org.vaadin.spring.samples.push.AppWidgetSet")
@Title("Push Demo")
@VaadinUI
public class PushUI extends UI {

    private static final long serialVersionUID = 3708190173011782944L;
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

    private Random rnd = new Random();
    private Chart chart;
    private ListSeries series;
    private ScheduledFuture<?> jobHandle;

    private Runnable updateGraphJob = new Runnable() {
        public void run() {
            access(new Runnable() {
                @Override
                public void run() {
                    series.addData(rnd.nextInt(1000));
                }
            });
        }
    };

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        chart = new Chart();
        chart.setSizeFull();
        setContent(chart);

        series = new ListSeries("Random values");

        final Configuration configuration = new Configuration();
        configuration.setSeries(series);
        chart.drawChart(configuration);

        jobHandle = executorService.scheduleWithFixedDelay(updateGraphJob, 500, 2000, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    void destroy() {
        jobHandle.cancel(true);
    }

    @WebListener
    public static class AtmosphereSessionSupport extends SessionSupport{

    }
}

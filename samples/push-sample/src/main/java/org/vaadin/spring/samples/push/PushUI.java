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

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.annotation.VaadinUI;

import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * UI that pushes updates from a background thread to the client.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Push(transport = Transport.WEBSOCKET)
@Title("Push Demo")
@VaadinUI
@Theme(ValoTheme.THEME_NAME)
public class PushUI extends UI {

    private static final long serialVersionUID = 3708190173011782944L;
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
    private static final Logger LOGGER = LoggerFactory.getLogger(PushUI.class);

    private Random rnd = new Random();
    private Grid grid;
    private ScheduledFuture<?> jobHandle;
    private IndexedContainer measurements;

    private Runnable updateGraphJob = new Runnable() {
        public void run() {
            access(new Runnable() {
                @Override
                @SuppressWarnings("unchecked")
                public void run() {
                    LOGGER.info("Storing new measurement");
                    Item item = measurements.getItem(measurements.addItem());
                    item.getItemProperty("Timestamp").setValue(new Date());
                    item.getItemProperty("Measurement").setValue(rnd.nextInt());
                }
            });
        }
    };

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        measurements = new IndexedContainer();
        measurements.addContainerProperty("Timestamp", Date.class, null);
        measurements.addContainerProperty("Measurement", Integer.class, null);

        grid = new Grid(measurements);
        grid.setSizeFull();
        setContent(grid);

        LOGGER.info("Scheduling background job");
        jobHandle = executorService.scheduleWithFixedDelay(updateGraphJob, 500, 3000, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    void destroy() {
        LOGGER.info("Canceling background job");
        jobHandle.cancel(true);
    }

}

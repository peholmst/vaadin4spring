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
package org.vaadin.spring.samples.mvp.ui.presenter;

import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBusListenerMethodFilter;

/**
 * A filter for startup events.
 * Filters methods annotated with {@link EventBusListenerMethodFilter}.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public class StartupFilter implements EventBusListenerMethodFilter {

    @Override
    public boolean filter(Event<?> event) {
        Object payload = event.getPayload();
        boolean result = false;
        if (Action.class.isAssignableFrom(payload.getClass())) {
            Action action = (Action) payload;
            if (action.equals(Action.START)) {
                result = true;
            }
        }
        return result;    }

}

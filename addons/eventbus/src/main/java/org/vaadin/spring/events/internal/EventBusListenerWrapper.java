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
package org.vaadin.spring.events.internal;

import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;

/**
 * Implementation of {@link org.vaadin.spring.events.internal.AbstractListenerWrapper} that wraps a single
 * {@link org.vaadin.spring.events.EventBusListener} instance.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
class EventBusListenerWrapper extends AbstractListenerWrapper {

    private static final long serialVersionUID = 8964309195124823892L;
    private final Class<?> payloadType;

    public EventBusListenerWrapper(EventBus owningEventBus, EventBusListener<?> listenerTarget, String topic, boolean includingPropagatingEvents) {
        super(owningEventBus, listenerTarget, topic, includingPropagatingEvents);
        payloadType = GenericTypeResolver.resolveTypeArgument(listenerTarget.getClass(), EventBusListener.class);
        Assert.notNull(payloadType, "Could not resolve payload type");
    }

    @Override
    @SuppressWarnings("rawtypes")
    public EventBusListener getListenerTarget() {
        return (EventBusListener) super.getListenerTarget();
    }

    @Override
    protected Class<?> getPayloadType() {
        return payloadType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void publish(Event<?> event) {
        getListenerTarget().onEvent(event);
    }
}

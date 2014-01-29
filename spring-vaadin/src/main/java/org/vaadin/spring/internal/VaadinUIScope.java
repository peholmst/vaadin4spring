package org.vaadin.spring.internal;


import com.vaadin.server.ClientConnector;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of Spring's {@link org.springframework.beans.factory.config.Scope} contract.
 * Registered by default as the scope {@code ui}.
 *
 * @author Josh Long (josh@joshlong.com)
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.UIScope
 */
public class VaadinUIScope implements Scope, ClientConnector.DetachListener, BeanFactoryPostProcessor {

    private Log logger = LogFactory.getLog(getClass());

    private final Map<VaadinUIIdentifier, Map<String, Object>> objectMap =
            new ConcurrentHashMap<VaadinUIIdentifier, Map<String, Object>>();

    private final Map<VaadinUIIdentifier, Map<String, Runnable>> destructionCallbackMap =
            new ConcurrentHashMap<VaadinUIIdentifier, Map<String, Runnable>>();

    private VaadinUIIdentifier currentUiId() {
        final UI currentUI = UI.getCurrent();
        if (currentUI != null) {
            return new VaadinUIIdentifier(currentUI);
        } else {
            VaadinUIIdentifier currentIdentifier = CurrentInstance.get(VaadinUIIdentifier.class);
            Assert.notNull(currentIdentifier, "found no valid " + VaadinUIIdentifier.class.getName() + " instance!");
            return currentIdentifier;
        }
    }

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        final VaadinUIIdentifier uiIdentifier = currentUiId();
        logger.debug("Getting bean with name " + name + " [UI-ID: " + uiIdentifier + "]");
        Map<String, Object> uiSpace = objectMap.get(uiIdentifier);
        if (uiSpace == null) {
            logger.debug("Creating new uiSpace [UI-ID: " + uiIdentifier + "]");
            uiSpace = new ConcurrentHashMap <String, Object> ();
            objectMap.put(uiIdentifier, uiSpace);
        }

        Object bean = uiSpace.get(name);
        if (bean == null) {
            logger.debug("Bean " + name + " not found in uiSpace, invoking object factory [UI-ID: " + uiIdentifier + "]");
            bean = objectFactory.getObject();
            if (bean instanceof UI) {
                logger.debug("Registering DetachListener with " + bean + "[UI-ID: " + uiIdentifier + "]");
                ((UI) bean).addDetachListener(this);
            }
            uiSpace.put(name, bean);
        }

        logger.debug("Returning bean " + bean + " with name " + name + " [UI-ID: " + uiIdentifier + "]");
        return bean;
    }

    @Override
    public Object remove(String name) {
        final VaadinUIIdentifier uiIdentifier = currentUiId();
        logger.debug("Removing bean with name " + name + " [UI-ID: " + uiIdentifier + "]");

        final Map<String, Runnable> destructionSpace = destructionCallbackMap.get(uiIdentifier);
        if (destructionSpace != null) {
            destructionSpace.remove(name);
        }

        final Map<String, Object> uiSpace = objectMap.get(uiIdentifier);
        if (uiSpace != null) {
            try {
                return uiSpace.remove(name);
            } finally {
                if (uiSpace.isEmpty()) {
                    logger.debug("UiSpace empty, removing [UI-ID: " + uiIdentifier + "]");
                    objectMap.remove(uiIdentifier);
                }
            }
        }
        return null;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        final VaadinUIIdentifier uiIdentifier = currentUiId();
        logger.debug("Registering destruction callback " + callback + " for bean with name " + name + " [UI-ID: " + uiIdentifier + "]");
        Map<String, Runnable> destructionSpace = destructionCallbackMap.get(uiIdentifier);
        if (destructionSpace == null) {
            destructionSpace = Collections.synchronizedMap(new LinkedHashMap<String, Runnable>());
            destructionCallbackMap.put(uiIdentifier, destructionSpace);
        }
        destructionSpace.put(name, callback);
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return currentUiId().toString();
    }

    @Override
    public void detach(ClientConnector.DetachEvent event) {
        logger.debug("Received DetachEvent from " + event.getSource());
        final VaadinUIIdentifier uiIdentifier = new VaadinUIIdentifier((UI) event.getSource());
        final Map<String, Runnable> destructionSpace = destructionCallbackMap.remove(uiIdentifier);
        if (destructionSpace != null) {
            for (Runnable runnable : destructionSpace.values()) {
                runnable.run();
            }
        }
        objectMap.remove(uiIdentifier);
    }

    @Override
    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) throws BeansException {
        logger.debug("Registering UI scope with beanFactory " + beanFactory);
        beanFactory.registerScope(UI_SCOPE_NAME, this);
    }

    public static final String UI_SCOPE_NAME = "ui";
}

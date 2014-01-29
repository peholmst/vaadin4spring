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

    public static final String UI_SCOPE_NAME = "ui";
    private final Log logger = LogFactory.getLog(getClass());
    private final Map<VaadinUIIdentifier, Map<String, Object>> objectMap =
            new ConcurrentHashMap<>();
    private final Map<VaadinUIIdentifier, Map<String, Runnable>> destructionCallbackMap =
            new ConcurrentHashMap<>();

    private VaadinUIIdentifier currentUiId() {
        final UI currentUI = UI.getCurrent();
        if (currentUI != null) {
            return new VaadinUIIdentifier(currentUI);
        } else {
            VaadinUIIdentifier currentIdentifier = CurrentInstance.get(VaadinUIIdentifier.class);
            Assert.notNull(currentIdentifier, String.format("Found no valid %s instance!", VaadinUIIdentifier.class.getName()));
            return currentIdentifier;
        }
    }

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        final VaadinUIIdentifier uiIdentifier = currentUiId();
        logger.debug(String.format("Getting bean with name [%s] from UI space [%s]", name, uiIdentifier));
        Map<String, Object> uiSpace = objectMap.get(uiIdentifier);
        if (uiSpace == null) {
            logger.debug(String.format("Creating new UI space [%s]", uiIdentifier));
            uiSpace = new ConcurrentHashMap<>();
            objectMap.put(uiIdentifier, uiSpace);
        }

        Object bean = uiSpace.get(name);
        if (bean == null) {
            logger.debug(String.format("Bean [%s] not found in UI space [%s], invoking object factory", name, uiIdentifier));
            bean = objectFactory.getObject();
            if (bean instanceof UI) {
                ((UI) bean).addDetachListener(this);
            }
            uiSpace.put(name, bean);
        }

        logger.debug(String.format("Returning bean [%s] with name [%s] from UI space [%s]", bean, name, uiIdentifier));
        return bean;
    }

    @Override
    public Object remove(String name) {
        final VaadinUIIdentifier uiIdentifier = currentUiId();
        logger.debug(String.format("Removing bean with name [%s] from UI space [%s]", name, uiIdentifier));

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
                    logger.debug(String.format("Removing empty UI space [%s]", uiIdentifier));
                    objectMap.remove(uiIdentifier);
                }
            }
        }
        return null;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        final VaadinUIIdentifier uiIdentifier = currentUiId();
        logger.debug(String.format("Registering destruction callback [%s] for bean with name [%s] in UI space [%s]", callback, name, uiIdentifier));
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
        logger.debug(String.format("Received DetachEvent from [%s]", event.getSource()));
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
        logger.debug(String.format("Registering UI scope with beanFactory [%s]", beanFactory));
        beanFactory.registerScope(UI_SCOPE_NAME, this);
    }
}

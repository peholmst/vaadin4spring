package org.vaadin.spring.portlet;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.annotation.VaadinPortletUI;
import com.vaadin.spring.internal.UIID;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Vaadin {@link com.vaadin.server.UIProvider} that looks up UI classes from the Spring application context. The UI
 * classes must be annotated with {@link org.vaadin.spring.annotation.VaadinPortletUI}.
 */
public class SpringAwareUIProvider extends UIProvider {
    private static final long serialVersionUID = -6195911893325385491L;

    private static final Logger logger = LoggerFactory.getLogger(SpringAwareUIProvider.class);

    private final Map<String, Class<? extends UI>> uiNameToUiClassMap = new ConcurrentHashMap<String, Class<? extends UI>>();

    private final ApplicationContext applicationContext;

    public SpringAwareUIProvider(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        detectUIs();
    }

    @SuppressWarnings("unchecked")
    private void detectUIs() {
        logger.info("Checking the application context for Vaadin UIs");
        final String[] uiBeanNames = applicationContext.getBeanNamesForAnnotation(VaadinPortletUI.class);
        for (String uiBeanName : uiBeanNames) {
            Class<?> beanType = applicationContext.getType(uiBeanName);
            if (UI.class.isAssignableFrom(beanType)) {
                String uiClassName = beanType.getName();
                int endIndex = uiClassName.indexOf('$');    //exclude AspectJ proxy class name
                if (endIndex > 0) {
                    uiClassName = uiClassName.substring(0, endIndex);
                }
                logger.info("Found Vaadin UI [{}]", uiClassName);
                Class<? extends UI> existingBeanType = uiNameToUiClassMap.get(uiClassName);
                if (existingBeanType != null) {
                    throw new IllegalStateException(String.format("[%s] is already mapped to the UI [%s]",
                            existingBeanType.getCanonicalName(), uiClassName));
                }
                logger.debug("Mapping Vaadin UI [{}] to uiClassName [{}]", beanType.getCanonicalName(), uiClassName);
                Class<? extends UI> uiClass = (Class<? extends UI>) beanType;
                uiNameToUiClassMap.put(uiClassName, uiClass);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        Object uiClassNameObj = event.getService()
                .getDeploymentConfiguration()
                .getApplicationOrSystemProperty(VaadinSession.UI_PARAMETER, null);

        if (uiClassNameObj instanceof String) {
            String uiClassName = uiClassNameObj.toString();
            return uiNameToUiClassMap.get(uiClassName);
        }
        return null;
    }

    @Override
    public UI createInstance(UICreateEvent event) {
        final Class<UIID> key = UIID.class;
        final UIID identifier = new UIID(event);
        CurrentInstance.set(key, identifier);
        try {
            Class<? extends UI> uiClass = event.getUIClass();
            logger.debug("Creating a new UI bean of class [{}] with identifier [{}]",
                    uiClass.getCanonicalName(), identifier);
            return applicationContext.getBean(uiClass);
        } finally {
            CurrentInstance.set(key, null);
        }
    }
}

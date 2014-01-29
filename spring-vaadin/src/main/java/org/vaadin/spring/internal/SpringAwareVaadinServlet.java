package org.vaadin.spring.internal;

import com.vaadin.server.*;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vaadin.spring.VaadinUI;

import javax.servlet.ServletException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Subclass of the standard Vaadin {@link com.vaadin.server.VaadinServlet vaadinServlet} that registers information
 * about the current Vaadin {@link com.vaadin.ui.UI} in a thread-local
 * for the custom {@link org.vaadin.spring.internal.VaadinUIScope scope}.
 *
 * @author petter@vaadin.com
 * @author Josh Long (josh@joshlong.com)
 */
public class SpringAwareVaadinServlet extends VaadinServlet {

    @Override
    protected void servletInitialized() throws ServletException {
        getService().addSessionInitListener(new SessionInitListener() {
            @Override
            public void sessionInit(SessionInitEvent sessionInitEvent) throws ServiceException {
                WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
                UIScopedAwareUiProvider uiProvider = new UIScopedAwareUiProvider(webApplicationContext);
                sessionInitEvent.getSession().addUIProvider(uiProvider);
            }
        });
    }

    static class UIScopedAwareUiProvider extends UIProvider {

        private final Log logger = LogFactory.getLog(getClass());
        private final WebApplicationContext webApplicationContext;
        private final Map<String, Class<? extends UI>> pathToUIMap = new ConcurrentHashMap<>();

        public UIScopedAwareUiProvider(WebApplicationContext webApplicationContext) {
            this.webApplicationContext = webApplicationContext;
            detectUIs();
        }

        private void detectUIs() {
            logger.info("Checking the application context for Vaadin UIs");
            final String[] uiBeanNames = webApplicationContext.getBeanNamesForAnnotation(VaadinUI.class);
            for (String uiBeanName : uiBeanNames) {
                Class<?> beanType = webApplicationContext.getType(uiBeanName);
                if (UI.class.isAssignableFrom(beanType)) {
                    logger.info("Found Vaadin UI [" + beanType.getCanonicalName() + "]");
                    final String path = beanType.getAnnotation(VaadinUI.class).path();
                    Class<? extends UI> existingBeanType = pathToUIMap.get(path);
                    if (existingBeanType != null) {
                        throw new IllegalStateException("[" + existingBeanType.getCanonicalName() + "] is already mapped to the path [" + path + "]");
                    }
                    pathToUIMap.put(path, (Class<? extends UI>) beanType);
                }
            }
            if (pathToUIMap.isEmpty()) {
                logger.warn("Found no Vaadin UIs in the application context");
            }
        }

        @Override
        public Class<? extends UI> getUIClass(UIClassSelectionEvent uiClassSelectionEvent) {
            final String path = extractUIPathFromPathInfo(uiClassSelectionEvent.getRequest().getPathInfo());
            return pathToUIMap.get(path);
        }

        private String extractUIPathFromPathInfo(String pathInfo) {
            if (pathInfo != null && pathInfo.length() > 1) {
                String path = pathInfo;
                final int indexOfBang = path.indexOf('!');
                if (indexOfBang > -1) {
                    path = path.substring(0, indexOfBang - 1);
                }

                if (path.endsWith("/")) {
                    path = path.substring(0, path.length() - 1);
                }
                return path;
            }
            return "";
        }

        @Override
        public UI createInstance(UICreateEvent event) {
            final Class<VaadinUIIdentifier> key = VaadinUIIdentifier.class;
            final VaadinUIIdentifier identifier = new VaadinUIIdentifier(event);
            CurrentInstance.set(key, identifier);
            try {
                logger.debug("Creating a new UI bean of class [" + event.getUIClass().getCanonicalName() + "] with identifier [" + identifier + "]");
                return webApplicationContext.getBean(event.getUIClass());
            } finally {
                CurrentInstance.set(key, null);
            }
        }
    }
}

package org.vaadin.spring.samples.mvp.ui.component.nav;


import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org.vaadin.spring.samples.mvp.util.JSONUtil;

import com.google.gwt.thirdparty.guava.common.reflect.TypeToken;

/**
 * Constructs a list of navigation beans from a JSON file on the classpath
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public class NavElementFactory {

    public List<NavElement> getNavElements(String resource) {
        Type collectionType = new TypeToken<Collection<NavElement>>(){}.getType();
        List<NavElement> result = JSONUtil.restoreFromJson(resource, collectionType);
        return result;
    }

}
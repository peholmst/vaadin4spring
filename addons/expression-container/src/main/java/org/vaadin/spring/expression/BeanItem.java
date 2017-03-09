package org.vaadin.spring.expression;

import com.vaadin.data.Item;
import com.vaadin.data.Property;

import java.util.Collection;

class BeanItem<T> implements Item {

    final T bean;
    private final Class<T> beanClass;

    BeanItem(T bean, Class<T> beanClass){
        this.bean = bean;
        this.beanClass = beanClass;
    }

    public Property getItemProperty(Object id) {

        String key = ExpressionContainer.asString(id);

        Accessor accessor = Accessors.getAccessor(beanClass, key);

        return new ExpressionProperty(bean, accessor);
    }

    public Collection<?> getItemPropertyIds() {
        return Accessors.getItemPropertyIds(beanClass);
    }

    public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

}

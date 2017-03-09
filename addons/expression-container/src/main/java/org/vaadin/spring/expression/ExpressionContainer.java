package org.vaadin.spring.expression;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractInMemoryContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.util.Collections.sort;

public class ExpressionContainer<T> extends AbstractInMemoryContainer<T, String, BeanItem<T>> {

    private final Class<T> beanClass;
    private List<String> properties;

    public ExpressionContainer(Class<T> beanClass){
        this.beanClass = beanClass;
        Accessors.register(beanClass);
    }

    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        T bean = asBean(itemId);

        BeanItem<T> item = new BeanItem<T>(bean, beanClass);

        internalAddItemAtEnd(bean, item, true);

        return item;
    }

    @Override
    public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        return internalRemoveItem(itemId);
    }

    public void addExpression(String expression){
        //make sure the expression compiles
        Accessors.getAccessor(beanClass, expression);

        if(properties == null){
            Collection<String> defaultProperties = Accessors.getItemPropertyIds(beanClass);

            properties = new ArrayList<String>(defaultProperties.size() + 1);
            properties.addAll(defaultProperties);
        }

        properties.add(expression);
        sort(properties, CASE_INSENSITIVE_ORDER);
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        internalRemoveAllItems();
        return true;
    }

    public Collection<?> getContainerPropertyIds() {

        if(properties != null){
            return properties;
        } else {
            return Accessors.getItemPropertyIds(beanClass);
        }
    }

    protected BeanItem<T> getUnfilteredItem(Object itemId) {
        return new BeanItem<T>(asBean(itemId), beanClass);
    }

    public Property getContainerProperty(Object itemId, Object propertyId) {
            return getItem(itemId).getItemProperty(propertyId);
    }

    public Class<?> getType(Object propertyId) {
        return Accessors.getAccessor(beanClass, asString(propertyId)).getValueType();
    }

    static String asString(Object o){
        try {
            return (String) o;
        } catch (ClassCastException e){
            throw new RuntimeException("only String allowed");
        }
    }

    @SuppressWarnings("unchecked")
    private T asBean(Object o){
        try {
            return (T)o;
        } catch (ClassCastException e){
            throw new RuntimeException("only " + beanClass + " allowed");
        }
    }
}

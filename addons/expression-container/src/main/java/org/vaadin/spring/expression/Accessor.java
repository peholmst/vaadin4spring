package org.vaadin.spring.expression;

interface Accessor {
    Object get(Object bean);
    void set(Object bean, Object object);
    Class getValueType();
    boolean isReadOnly();
}

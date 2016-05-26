package org.vaadin.spring.expression;

import com.vaadin.data.Property;

class ExpressionProperty implements Property {
    private Object bean;
    private final Accessor accessor;
    private boolean readOnly;

    ExpressionProperty(Object bean, Accessor accessor) {
        this.bean = bean;
        this.accessor = accessor;
        readOnly = false;
    }

    public Object getValue() {
        return accessor.get(bean);
    }

    public void setValue(Object newValue) throws ReadOnlyException {
        if(readOnly || accessor.isReadOnly()){
            throw new ReadOnlyException();
        }

        accessor.set(bean, newValue);
    }

    public Class getType() {
        return accessor.getValueType();
    }

    public boolean isReadOnly() {
        return accessor.isReadOnly() || readOnly;
    }

    public void setReadOnly(boolean newStatus) {
        if(!newStatus && accessor.isReadOnly()){
            throw new IllegalArgumentException("accessor is readonly, cannot set readonly of property to false");
        }

        readOnly = newStatus;
    }
}

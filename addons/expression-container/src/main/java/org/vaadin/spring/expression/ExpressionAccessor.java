package org.vaadin.spring.expression;

import org.springframework.expression.Expression;

import static org.springframework.util.ClassUtils.isAssignable;

class ExpressionAccessor implements Accessor {

    private Expression expression;
    private Class expressionType;
    private boolean readOnly;

    ExpressionAccessor(Expression expression) {
        this.expression = expression;
    }

    @SuppressWarnings("unchecked")
    public Object get(Object bean) {
        Object value = expression.getValue(bean);

        if(value == null){
            return null;
        }

        if(expressionType == null){
            expressionType = expression.getValueType(bean);

            readOnly = !expression.isWritable(bean);

            return value.toString();
        }

        if(!isAssignable(expressionType, value.getClass())){
            throw new IllegalStateException("expression was supposed to be of type " + expressionType + " but was " + value.getClass());
        }

        return value;
    }

    public void set(Object bean, Object object) {
        expression.setValue(bean, object);
    }

    public Class getValueType() {
        return expressionType != null ? expressionType : String.class;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly || expressionType == null;
    }
}

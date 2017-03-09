package org.vaadin.spring.expression;

import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.Assert;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.beans.Introspector.getBeanInfo;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.util.Collections.sort;
import static org.springframework.util.ClassUtils.resolvePrimitiveIfNecessary;

class Accessors {

    private static final SpelExpressionParser parser = new SpelExpressionParser(new SpelParserConfiguration(true, true));
    private static final Map<Class<?>, Map<String, Accessor>> accessorCache = new ConcurrentHashMap<Class<?>, Map<String, Accessor>>();
    private static final Map<Class<?>, List<String>> propertyIdCache = new ConcurrentHashMap<Class<?>, List<String>>();
    public static final String COLLECTION_INDEXER = "#COLLECTION#";

    static Accessor getAccessor(final Class<?> beanClass, String key){
        Map<String, Accessor> accessorMap = getAccessorMap(beanClass);

        Accessor accessor = accessorMap.get(key);

        if(accessor == null){
            Expression expression = parser.parseExpression(key);

            accessor = new ExpressionAccessor(expression);

            accessorMap.put(key, accessor);
        }

        return accessor;
    }

    static Collection<String> getItemPropertyIds(Class<?> beanClass) {
        return propertyIdCache.get(beanClass);
    }

    private static Map<String, Accessor> getAccessorMap(Class<?> beanClass){
        return accessorCache.get(beanClass);
    }

    private static boolean toBeIncluded(PropertyDescriptor propertyDescriptor){
        if(propertyDescriptor.isHidden()){
            return false;
        }

        Class type = propertyDescriptor.getPropertyType();

        return type.isPrimitive() || String.class.equals(type);
    }

    static void register(Class<?> beanClass) {
        Map<String, Accessor> expressionMap = accessorCache.get(beanClass);

        if(expressionMap == null){
            expressionMap = new ConcurrentHashMap<String, Accessor>();

            try {
                List<String> propertyIds = new ArrayList<String>();

                for (PropertyDescriptor propertyDescriptor : getBeanInfo(beanClass).getPropertyDescriptors()) {
                    if (!toBeIncluded(propertyDescriptor)){
                        continue;
                    }

                    String property = propertyDescriptor.getName();

                    propertyIds.add(property);

                    expressionMap.put(property, new MethodAccessor(propertyDescriptor.getReadMethod(), propertyDescriptor.getWriteMethod()));
                }

                sort(propertyIds, CASE_INSENSITIVE_ORDER);

                propertyIdCache.put(beanClass, propertyIds);

            } catch (IntrospectionException e) {
                throw new RuntimeException(e);
            }

            accessorCache.put(beanClass, expressionMap);
        }
    }
}

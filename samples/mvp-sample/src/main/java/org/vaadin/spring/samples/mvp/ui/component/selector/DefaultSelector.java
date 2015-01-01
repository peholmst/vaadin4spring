package org.vaadin.spring.samples.mvp.ui.component.selector;

import java.util.EnumSet;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.vaadin.ui.ComboBox;

public class DefaultSelector extends ComboBox {

    private static final long serialVersionUID = -3103214555075605482L;

    public DefaultSelector(@NotNull String items[]) {
        this(null, items);
    }

    public DefaultSelector(String caption, @NotNull String[] items) {
        if (StringUtils.isNotBlank(caption)) {
            setCaption(caption);
        }
        setItemCaptionMode(ItemCaptionMode.ID);
        setNullSelectionAllowed(false);
        setImmediate(true);
        int i = 0;
        for (String item: items) {
            addItem(item);
            if (i == 0) {
                setValue(item);
            }
            i++;
        }
    }

    public <E extends Enum<E>> DefaultSelector(E e, String accessor) throws Exception {
        Class<E> enumClass = (Class<E>) e.getClass();
        EnumSet<E> set = EnumSet.allOf(enumClass);
        String value;
        for (E item: set) {
            value = (String) MethodUtils.invokeExactMethod(item, accessor, null);
            addItem(value);
            setItemCaption(value, value);
        }
        String selectedValue = (String) MethodUtils.invokeExactMethod(e, accessor, null);
        setValue(selectedValue);

        setNullSelectionAllowed(false);
        setImmediate(true);
    }
}

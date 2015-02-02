/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    @SuppressWarnings("unchecked")
    public <E extends Enum<E>> DefaultSelector(E e, String accessor) throws Exception {
        Class<E> enumClass = (Class<E>) e.getClass();
        EnumSet<E> set = EnumSet.allOf(enumClass);
        String value;
        for (E item: set) {
            value = (String) MethodUtils.invokeExactMethod(item, accessor, new Object[]{});
            addItem(value);
            setItemCaption(value, value);
        }
        String selectedValue = (String) MethodUtils.invokeExactMethod(e, accessor, new Object[]{});
        setValue(selectedValue);

        setNullSelectionAllowed(false);
        setImmediate(true);
    }
}

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
package org.vaadin.spring.samples.eventbus;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.DefaultConverterFactory;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import org.vaadin.spring.expression.ExpressionContainer;

/**
 * Demo of the expression container
 *
 * @author Bernd Hopp (bernd@vaadin.com)
 */
@SpringUI
@Theme(ValoTheme.THEME_NAME)
public class ExpressionContainerUI extends UI {

    private static class Form extends FormLayout {

        @PropertyId("aFloat")
        private TextField aFloatTextField = new TextField();

        @PropertyId("anInt")
        private TextField anIntTextField = new TextField();

        @PropertyId("aString")
        private TextField aStringTextField = new TextField();

        @PropertyId("anotherBean.anotherInt")
        private TextField anotherIntTextField = new TextField();

        @PropertyId("anotherBean.anotherString")
        private TextField anotherStringTextField = new TextField();

        @PropertyId("anotherBean.anotherString.concat(' ').concat(anotherBean.anotherInt)")
        private TextField anotherStringAndIntTextField = new TextField();

        Form(){
            anIntTextField.setCaption("anInt");
            aStringTextField.setCaption("aString");
            aFloatTextField.setCaption("aFloat");
            anotherIntTextField.setCaption("anotherBean.anotherInt");
            anotherStringTextField.setCaption("anotherBean.anotherString");
            anotherStringAndIntTextField.setCaption("anotherBean.anotherString.concat(' ').concat(anotherBean.anotherInt)");

            addComponents(anIntTextField, aStringTextField, aFloatTextField
                    ,anotherIntTextField,
                    anotherStringTextField, anotherStringAndIntTextField);
        }
    }

    //set up some arbitraty object graph
    static AnotherBean anotherBean = new AnotherBean(23, "another string");
    static AnotherBean anotherBean2 = new AnotherBean(222, "yet another string");

    static ABean aBean = new ABean(42, "a String", 2.71828f, anotherBean);
    static ABean aBean2 = new ABean(123, "a String 2", 3.1415f, anotherBean);
    static ABean aBean3 = new ABean(123, "a String 3", 3.1415f, anotherBean2);

    static {
        aBean.getLinkedBeans().add(aBean2);
        aBean.getLinkedBeans().add(aBean3);
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        //add root objects to container
        ExpressionContainer<ABean> expressionBeanItemContainer = new ExpressionContainer<>(ABean.class);

        expressionBeanItemContainer.addExpression("1 == 1");
        expressionBeanItemContainer.addExpression("aString.concat(aFloat)");
        expressionBeanItemContainer.addExpression("linkedBeans[1].aString");

        final Item aBeanItem = expressionBeanItemContainer.addItem(aBean);
        expressionBeanItemContainer.addItem(aBean2);
        expressionBeanItemContainer.addItem(aBean3);

        //set up components
        Grid grid = new Grid(expressionBeanItemContainer);

        grid.setEditorEnabled(true);

        grid.setWidth("100%");

        grid.getColumn("aString.concat(aFloat)").setHeaderCaption("String and Float");
        grid.getColumn("linkedBeans[1].aString").setHeaderCaption("aString of linkedBeans[1]");

        Form form = new Form();

        Button button = new Button("flush");

        VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.addComponents(grid, form, button);

        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);

        setContent(verticalLayout);

        //bind grid selection to form
        final FieldGroup binder = new FieldGroup(aBeanItem);

        binder.bindMemberFields(form);

        grid.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                binder.setItemDataSource(event.getItem());
            }
        });

        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    binder.commit();
                } catch (FieldGroup.CommitException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

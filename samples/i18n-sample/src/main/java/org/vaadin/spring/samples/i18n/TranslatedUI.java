/*
 * Copyright 2016 The original authors
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
package org.vaadin.spring.samples.i18n;

import com.vaadin.annotations.Widgetset;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.i18n.support.Translatable;
import org.vaadin.spring.i18n.support.TranslatableUI;

import com.vaadin.v7.data.util.ObjectProperty;
import com.vaadin.v7.data.util.converter.StringToBigDecimalConverter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Example of a translated UI.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@SpringUI
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
public class TranslatedUI extends TranslatableUI implements Translatable {

    @Autowired
    I18N i18n;

    private Button english;
    private Button swedish;

    private TextField bigDecimalTextField;
    private DateField dateField;
    private ObjectProperty<BigDecimal> bigDecimalObjectProperty = new ObjectProperty<>(new BigDecimal("1230500.25"));

    @Override
    protected void initUI(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

        english = new Button("English", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                setLocale(Locale.ENGLISH);
            }
        });
        layout.addComponent(english);

        swedish = new Button("Svenska", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                setLocale(new Locale("sv"));
            }
        });
        layout.addComponent(swedish);

        bigDecimalTextField = new TextField();
        bigDecimalTextField.setConverter(new StringToBigDecimalConverter());
        bigDecimalTextField.setPropertyDataSource(bigDecimalObjectProperty);
        bigDecimalTextField.setImmediate(true);
        bigDecimalTextField.setNullSettingAllowed(false);
        layout.addComponent(bigDecimalTextField);

        dateField = new DateField();
        dateField.setValue(new Date());
        layout.addComponent(dateField);
    }

    @Override
    public void updateMessageStrings(Locale locale) {
        getPage().setTitle(i18n.get("demoapp.title", locale, new Date()));
        bigDecimalTextField.setCaption(i18n.get("demoapp.bigDecimalTextField.caption", locale));
        bigDecimalTextField.setConversionError(i18n.get("demoapp.bigDecimalTextField.conversionError", locale));
        bigDecimalTextField.setLocale(locale);

        // In this demo application, the 'locale' parameter is the same as the locale returned by the I18N instance,
        // so we don't actually have to pass it in
        dateField.setCaption(i18n.get("demoapp.dateField.caption"));
        dateField.setLocale(i18n.getLocale());
    }
}

/*
 * Copyright 2014 The original authors
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
package org.vaadin.spring.i18n;

import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import org.junit.Test;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test case for {@link org.vaadin.spring.i18n.Translator}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class TranslatorTest {

    @TranslatedProperty(property = "caption", key = "privateLabel.caption")
    private Label privateLabel = new Label();

    @TranslatedProperty(property = "caption", key = "publicLabel.caption")
    public Label publicLabel = new Label();

    @TranslatedProperties({
            @TranslatedProperty(property = "caption", key = "textField.caption"),
            @TranslatedProperty(property = "description", key = "textField.description")
    })
    private TextField textField = new TextField();

    private Label methodLabel = new Label();

    private TextField methodTextField = new TextField();

    @TranslatedProperty(property = "caption", key = "methodLabel.caption")
    public Label getMethodLabel() {
        return methodLabel;
    }

    @TranslatedProperties({
            @TranslatedProperty(property = "caption", key = "methodTextField.caption"),
            @TranslatedProperty(property = "description", key = "methodTextField.description")
    })
    public TextField getMethodTextField() {
        return methodTextField;
    }

    @Test
    public void testTranslate() {
        MessageSource messageSource = mock(MessageSource.class);
        when(messageSource.getMessage("privateLabel.caption", null, "", Locale.ENGLISH)).thenReturn("Private Label Caption");
        when(messageSource.getMessage("publicLabel.caption", null, "", Locale.ENGLISH)).thenReturn("Public Label Caption");
        when(messageSource.getMessage("textField.caption", null, "", Locale.ENGLISH)).thenReturn("Text Field Caption");
        when(messageSource.getMessage("textField.description", null, "", Locale.ENGLISH)).thenReturn("Text Field Description");
        when(messageSource.getMessage("methodLabel.caption", null, "", Locale.ENGLISH)).thenReturn("Method Label Caption");
        when(messageSource.getMessage("methodTextField.caption", null, "", Locale.ENGLISH)).thenReturn("Method Text Field Caption");
        when(messageSource.getMessage("methodTextField.description", null, "", Locale.ENGLISH)).thenReturn("Method Text Field Description");

        new Translator(this).translate(Locale.ENGLISH, messageSource);

        assertEquals("Private Label Caption", privateLabel.getCaption());
        assertEquals("Public Label Caption", publicLabel.getCaption());
        assertEquals("Text Field Caption", textField.getCaption());
        assertEquals("Text Field Description", textField.getDescription());
        assertEquals("Method Label Caption", methodLabel.getCaption());
        assertEquals("Method Text Field Caption", methodTextField.getCaption());
        assertEquals("Method Text Field Description", methodTextField.getDescription());
    }


}

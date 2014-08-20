Vaadin4Spring Internationalization Support
==========================================

Internationalization in a Vaadin4Spring application is very much handled by the I18N features of Spring itself.
However, to make it a bit easier to use, the addon provides a helper class, namely [I18N](I18N.java).

The ```I18N``` can be injected into any Spring managed bean and uses the locale of the currently active UI
when looking up messages from the application context.

Example:

```java
 @Autowired
 I18N i18n; 
 ...
 myLabel = new Label(i18n.get("myMessageKey", "My argument"));
```

# Experimental Stuff

In addition to the ```I18N``` class, there are also some experimental classes that should be used with care (meaning
they might not work at all, they could change dramatically or even be dropped from future versions of the add-on).

Please check the source code and JavaDocs for more information:

* [TranslatedProperties](TranslatedProperties.java)
* [TranslatedProperty](TranslatedProperty.java)
* [Translator](Translator.class)
* [ComponentProperties](support/ComponentProperties.java)

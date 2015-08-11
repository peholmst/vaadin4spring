The Vaadin4Spring I18N Extensions
=================================

Internationalization in a Vaadin4Spring application is very much handled by the I18N features of Spring itself.
However, to make it a bit easier to use, the addon provides a helper class, namely [I18N](src/main/java/org/vaadin/spring/i18n/I18N.java).

The ```I18N``` can be injected into any Spring managed bean and uses the locale of the currently active UI
when looking up messages from the application context.

Example:

```java
 @Autowired
 I18N i18n; 
 ...
 myLabel = new Label(i18n.get("myMessageKey", "My argument"));
```

# Composite Message Source

A composite message source is an implementation of the Spring ```MessageSource``` interface
that resolves messages by querying all beans in the application context that implement the
[MessageProvider](src/main/java/org/vaadin/spring/i18n/MessageProvider.java) interface. 

The idea behind this is to make it possible to build modular UI applications that all share a single message source.
Each module would have its own message provider, and would use either ```I18N``` or the application context directly
to retrieve localized strings. The modules would be completely separated on a source code level, but during runtime they
would all run inside a single application context.

## Enabling the Composite Message Source

You can enable the composite message source by adding the ```@EnableCompositeMessageSource``` annotation to your
Spring configuration. This will add the message source to your application context.

After this, create as many ```MessageProvider``` beans as you want. You can either implement the interface from
scratch, or use the [ResourceBundleMessageProvider](src/main/java/org/vaadin/spring/i18n/ResourceBundleMessageProvider.java). The latter reads messages from
resource bundles with a specific basename and encoding (check the JavaDocs for more information).

## Example

In the main application:

```java
@EnableAutoConfiguration
@EnableCompositeMessageSource
@ComponentScan
@Configuration
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    ...
}
```

In the UI module:

```java
@Configuration
public class MyModule {
    @Bean
    MessageProvider communicationMessages() {
        return new ResourceBundleMessageProvider("mymodule.messages"); // Will use UTF-8 by default
    }
    ...
}
```

Finally a tip: If you are building a modular application, it is good practice to prepend the messages with some kind
of module identifier, for example a Java package name. That way you can avoid naming conflicts when additional modules
are added in the future.


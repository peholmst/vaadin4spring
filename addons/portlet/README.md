Vaadin4Spring Portlet Support
===

This add-on helps you to start quick development of your portlets with Vaadin and Spring.

Create a new UI class in your project and annotate it with `@VaadinPortletUI` e.g. like this:
```java
@VaadinPortletUI
public class MyVaadinPortletUI extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new Label("Hello! This is a simple portlet with Spring support!"));
    }
}
```
You should also add information about your portlet to `portlet.xml`:
```xml
<portlet>
    <description>An example portlet to show Spring integration to Vaadin</description>
    <portlet-name>Vaadin + Spring Portlet Example</portlet-name>
    <display-name>Vaadin + Spring Portlet</display-name>

    <!-- Map portlet to a servlet -->
    <portlet-class>org.vaadin.spring.portlet.SpringAwareVaadinPortlet</portlet-class>
    <init-param>
        <name>UI</name>
        <!-- The application class with package name -->
        <value>com.example.myportlet.MyVaadinPortletUI</value>
    </init-param>

    <!-- Supported portlet modes and content types -->
    <supports>
        <mime-type>text/html</mime-type>
        <portlet-mode>VIEW</portlet-mode>
        <portlet-mode>edit</portlet-mode>
        <portlet-mode>help</portlet-mode>
    </supports>

    <!-- Not always required but Liferay requires these -->
    <portlet-info>
        <title>Vaadin + Spring Portlet</title>
        <keywords>vaadin</keywords>
    </portlet-info>
</portlet>
```
Read more information about portal integration in [Book of Vaadin](https://vaadin.com/book/-/page/portal.html).

The simplest way to add Spring initialization in your application is a class which will extend
`AbstractContextLoaderInitializer`:
```java
public class YourSpringWebContextInitializer extends AbstractContextLoaderInitializer {

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(YourApplicationConfiguration.class);
        return applicationContext;
    }
}
```
`YourApplicationConfiguration` should also import `VaadinConfiguration` to enable Vaadin UI Scope and other
features of `spring-vaadin` add-on:
```java
@Configuration
@Import(VaadinConfiguration.class)
@ComponentScan(basePackages = {"com.example.myportlet" /*, ...other packages with Spring beans... */})
public class YourApplicationConfiguration {
    //...
    //Your condiguration of DataSource, SessionFactory, TransactionManager, etc.
    //...
}
```

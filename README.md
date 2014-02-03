Vaadin + Spring = AWESOME
=========================

This add-on adds support for Vaadin to Spring and Spring Boot, or Spring support to Vaadin,
depending on your point of view ;-).

## Quick start ##

First, you need to get this add-on installed into your local Maven repository:

1. Clone this repository: ```$ git clone https://github.com/peholmst/vaadin4spring.git```
2. Change into the root directory of the working copy and run: ```mvn clean install```

When this is done, you need to create your actual application project. The easiest way is to go to http://start.spring.io and
fill in the necessary details. Make sure you at least select the **Web** style. Then download the generated project as a **Maven Project** and
open it up in your favorite IDE.

Now it is time to add this add-on. Add the following dependencies to your ```pom.xml``` file:

```xml
<dependency>
  <groupId>org.vaadin.spring</groupId>
  <artifactId>spring-boot-vaadin</artifactId>
  <version>0.0.1</version>
</dependency>
<dependency>
   <groupId>com.vaadin</groupId>
   <artifactId>vaadin-themes</artifactId>
   <version>7.1.10</version>
</dependency>
<dependency>
   <groupId>com.vaadin</groupId>
   <artifactId>vaadin-client-compiled</artifactId>
   <version>7.1.10</version>
</dependency>
```

Finally, you can create your Vaadin UI. Create a new UI class in your project and annotate it with ```@VaadinUI``` e.g. like this:

```java
@VaadinUI
public class MyVaadinUI extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new Label("Hello! I'm the root UI!"));
    }
}
```

Run the ```main(String[])``` method in the generated ```Application``` class and point your browser to http://localhost:8080 . Voila!

## Configuring the Vaadin servlet ##

By default, the Vaadin servlet is mapped to ```/*```. If you are only exposing your application as a web UI, this is good enough, but if you want
REST endpoints as well, you need to change this mapping. You can do this by creating a file named ```application.properties``` in the
```src/main/resources```directory and add the following property to it: ```vaadin.servlet.urlMapping=/myUI/*```

The Vaadin servlet init parameters can also be configured in this way. Just prefix the name of the init parameter with ```vaadin.servlet.params.```. For
example, to turn on production mode, you would add ```vaadin.servlet.params.productionMode=true``` to the file.

## The UI Scope ##

This add-on provides a custom scope that binds the bean to the current UI instance. You can use it by adding the ```@UIScope``` annotation to the bean.
The lifecycle of the bean will automatically follow the lifecycle of the UI instance, so you can also use ```@PostConstruct``` and ```@PreDestroy```.
Please note, however, that UI scoped beans cannot currently be ```ApplicationListener```s. Also, no Vaadin components can be proxied.

## Navigator API integration ##

This add-on provides support for the Vaadin Navigator API. To use it:

1. Annotate your views with ```@VaadinView``` and make them either ```ui``` scoped or ```prototype``` scoped.
2. Inject an instance of ```SpringViewProvider``` into your UI and add it as a view provider to your ```Navigator``` instance.

Please see the JavaDocs and the sample application for more information.

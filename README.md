Vaadin + Spring = AWESOME
=========================

This add-on adds support for Vaadin to Spring and Spring Boot, or Spring support to Vaadin,
depending on your point of view ;-).

## Quick start ##

There are two ways of getting this add-on. You can either clone this repository and build it yourself,
or download a snapshot from Sonatype. To build the add-on yourself, follow these steps:

1. Clone this repository: ```$ git clone https://github.com/peholmst/vaadin4spring.git```
2. Change into the root directory of the working copy and run: ```mvn clean install```

If you prefer to download the add-on from Sonatype, add this repository to your ```pom.xml``` file (please note that I
currently push new snapshots to Sonatype manually, which means you might not always get the latest version):

```xml
<repository>
  <id>sonatype-snapshots</id>
  <url>https://oss.sonatype.org/content/repositories/snapshots</url>
  <snapshots>
    <enabled>true</enabled>
  </snapshots>
  <releases>
    <enabled>false</enabled>
  </releases>
</repository>
```

When this is done, you need to create your actual application project. The easiest way is to go to
http://start.spring.io and fill in the necessary details. Make sure you at least select the **Web** style. Then download
the generated project as a **Maven Project** and open it up in your favorite IDE.

Now it is time to add this add-on. Add the following dependencies to your ```pom.xml``` file:

```xml
<dependency>
  <groupId>org.vaadin.spring</groupId>
  <artifactId>spring-boot-vaadin</artifactId>
  <version>0.0.2-SNAPSHOT</version>
</dependency>
<dependency>
   <groupId>com.vaadin</groupId>
   <artifactId>vaadin-themes</artifactId>
   <version>7.1.13</version>
</dependency>
<dependency>
   <groupId>com.vaadin</groupId>
   <artifactId>vaadin-client-compiled</artifactId>
   <version>7.1.13</version>
</dependency>
```

Finally, you can create your Vaadin UI. Create a new UI class in your project and annotate it with ```@VaadinUI``` e.g.
like this:

```java
@VaadinUI
public class MyVaadinUI extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new Label("Hello! I'm the root UI!"));
    }
}
```

Run the ```main(String[])``` method in the generated ```Application``` class and point your browser to
http://localhost:8080 . Voila!

## Configuring the Vaadin servlet ##

By default, the Vaadin servlet is mapped to ```/*```. If you are only exposing your application as a web UI, this is
good enough, but if you want REST endpoints as well, you need to change this mapping. You can do this by creating a file
named ```application.properties``` in the ```src/main/resources``` directory and add the following property to it: ```vaadin.servlet.urlMapping=/myUI/*```

The Vaadin servlet init parameters can also be configured in this way. Just prefix the name of the init parameter with
```vaadin.servlet.params.```. For example, to turn on production mode, you would add
```vaadin.servlet.params.productionMode=true``` to the file.

## Customizing the Vaadin servlet ##

If you want to customize the Vaadin servlet, just extend ```org.vaadin.spring.servlet.SpringAwareVaadinServlet``` and
make it available as a Spring bean. The add-on will automatically discover your custom servlet and use that in place
of the default servlet.

## Serving static content ##

Static content from ```/VAADIN/*``` are served by a separate ```VaadinServlet``` that has no UIs at all. The
servlet cannot be customized, but it can be configured in the same way as the Vaadin servlet (see above). The
init parameters must be prefixed with ```vaadin.static.servlet.params.```.

If anybody wonders why a separate servlet is used, it was added after it became possible to use both TouchKit and
"desktop" Vaadin in the same Spring application. Deciding which servlet to serve the static content was a bit tricky, so
adding a separate servlet was the easiest solution. This might change in the future, if I come up with something
better.

## The UI Scope ##

This add-on provides a custom scope that binds the bean to the current UI instance. You can use it by adding the
 ```@UIScope``` annotation to the bean. The lifecycle of the bean will automatically follow the lifecycle of the UI
instance, so you can also use ```@PostConstruct``` and ```@PreDestroy```. Please note, however, that UI scoped beans
cannot currently be ```ApplicationListener```s. Also, no Vaadin components can be proxied.

## Navigator API integration ##

This add-on provides support for the Vaadin Navigator API. To use it:

1. Annotate your views with ```@VaadinView``` and make them either ```ui``` scoped or ```prototype``` scoped.
2. Inject an instance of ```SpringViewProvider``` into your UI and add it as a view provider to your ```Navigator```
instance.

Please see the JavaDocs and the sample application for more information.

## The Event Bus ##

This add-on provides its own event bus framework, with the intention of complementing Spring's own event publisher.
The JavaDocs of the ```org.vaadin.spring.events.EventBus``` interface should be enough to get you started, but here is a
summary of the key features:

1. Event buses can be scoped to the current UI, the current session or the entire application.
2. Event buses are chained in such a way that application events are propagated to the session event bus and the UI
event bus, and session events are propagated to the UI event bus.
3. Events published by Spring's event publisher are automatically propagated to the application event bus.
4. You can use qualifiers to decide which of the event buses to inject. If no qualifiers are present, the UI event bus
is injected.
5. You can use the UI event bus to both publish and subscribe to events of any scope.
6. You need to explicitly subscribe to and unsubscribe from an event bus.

## Internationalization ##

This add-on provides some helper classes to make it easier to use Spring's ```MessageSource```s in Vaadin applications.
The most stable one, that you can use in your applications already, is the ```org.vaadin.spring.i18n.I18N``` class.
Please read the JavaDocs for more information.

There are also some other I18N classes, but they are still very experimental so I won't write about them here just yet.
;-)

## TouchKit Support ##

Initial TouchKit support has been added in the ```spring-vaadin-touchkit``` module. You can enable it by adding the
module's JAR-file to your class path and adding the ```@EnableTouchKitServlet``` annotation to your Boot application
class. If you want to enable the "desktop" Vaadin servlet as well, add the ```@EnableVaadinServlet``` annotation. In this
case, remember to make sure the servlet's URL mappings are not conflicting. If you forget both annotations, only
the servlet for serving static content will be registered and your application will not work.

TouchKit UI classes need to use the ```org.vaadin.spring.touchkit.TouchKitUI``` annotation to be detected by the
UI provider.

You can configure the TouchKit servlet in the same way as the "desktop" Vaadin servlet, by using the
```touchKit.servlet.params.``` init parameter prefix. The URL mapping is specified using the
```touchKit.servlet.urlMapping``` parameter.

If you want to customize the TouchKit servlet, extend
```org.vaadin.spring.touchkit.servlet.SpringAwareTouchKitServlet``` and make it available as a Spring bean.

Finally, please note that TouchKit is a commercial Vaadin product. It can, however, also be used in AGPL projects.

## The Stuff Module ##

The Stuff module contains different classes and utilities that I often end up re-implementing in my projects. Do not
use any of them in production until the supported features are listed in this README.

## Security Support ##

The Spring Security integration is still in very early stages of development. Do not assume it will work, and don't use
it in your projects until the supported features are listed in this README.

## Testing

The testing module has its own REAMDE, you can find it [here](spring-vaadin-test/README.md).

Vaadin + Spring = AWESOME
=========================

This add-on adds support for Vaadin to Spring and Spring Boot, or Spring support to Vaadin,
depending on your point of view ;-). 

This project is not yet an official Vaadin Spring add-on, but a Vaadin R&D prototype that I work on whenever I have time. I'm also using the add-on in several customer projects to make sure it meets the requirements of a real-world project.

**Please note! As of February 2015, Vaadin is working on an official Spring add-on which will be a small subset of Vaadin4Spring. 
Once the official add-on is released, Vaadin4Spring will be converted into a set of add-ons that provide features that the
official add-on does not have. You can follow the progress of the official add-on here: https://github.com/vaadin/spring**

## Quick start ##

There are three ways of getting this add-on. You can either clone this repository and build it yourself,
download the latest release from Maven central, or download a snapshot from Sonatype. 
To build the add-on yourself, follow these steps:

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

If you are downloading a release from Maven central, there is no need to add this repository.

When this is done, you need to create your actual application project. The easiest way is to go to
http://start.spring.io and fill in the necessary details. Make sure you at least select the **Web** style. Then download
the generated project as a **Maven Project** and open it up in your favorite IDE.

Now it is time to add this add-on. Add the following dependencies to your ```pom.xml``` file:

```xml
<dependency>
  <groupId>org.vaadin.spring</groupId>
  <artifactId>spring-boot-vaadin</artifactId>
  <version>[LATEST VERSION]</version>
</dependency>
<dependency>
   <groupId>com.vaadin</groupId>
   <artifactId>vaadin-themes</artifactId>
   <version>[LATEST VAADIN VERSION]</version>
</dependency>
<dependency>
   <groupId>com.vaadin</groupId>
   <artifactId>vaadin-client-compiled</artifactId>
   <version>[LATEST VAADIN VERSION]</version>
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

### Supported configuration parameters ###

At the time of writing, the following configuration parameters are supported:

- ```vaadin.servlet.params.productionMode```
- ```vaadin.servlet.params.resourceCacheTime```
- ```vaadin.servlet.params.heartbeatInterval```
- ```vaadin.servlet.params.closeIdleSessions```
- ```vaadin.servlet.params.widgetset```
- ```vaadin.servlet.params.Resources```

### Customizing the Vaadin servlet ###

If you want to customize the Vaadin servlet, just extend ```org.vaadin.spring.servlet.SpringAwareVaadinServlet``` and
make it available as a Spring bean. The add-on will automatically discover your custom servlet and use that in place
of the default servlet.

## Serving static content ##

Static content from ```/VAADIN/*``` are served by a separate ```VaadinServlet``` that has no UIs at all. The
servlet cannot be customized, but it can be configured in the same way as the Vaadin servlet (see above). The
init parameters must be prefixed with ```vaadin.static.servlet.params.```.

If anybody wonders why a separate servlet is used, it was added after it became possible to use both TouchKit and
"desktop" Vaadin in the same Spring application. Deciding which servlet to serve the static content was a bit tricky, so
adding a separate servlet was the easiest solution. This will hopefully change in the future, if/when I come up with something
better.

## Provided Scopes ##

Vaadin4Spring provides three custom scopes that can be used in your Vaadin applications. In addition to these scopes,
there are also occasions where the prototype scope can be useful.

### The Vaadin Session Scope ###

This scope binds the bean to the lifecycle of the Vaadin session (as opposed to the HTTP session; it is possible to
have multiple Vaadin sessions within the same HTTP session). You use it by adding the ```@VaadinSessionScope``` annotation
to your class. 

**Intended use case:** Storing session specific context information that needs to be shared across all the UI instances of
the session.

**Known limitations:**

- Do not use this scope for Vaadin UI components, since a UI component can only belong to exactly one UI instance.
- Beans in this scope cannot be ```ApplicationListener```s.

### The Vaadin UI Scope ###

This scope binds the bean to the lifecycle of a Vaadin UI instance. This is also the scope of the UI instance itself.
You use it by adding the ```@VaadinUIScope``` annotation to your class.

**Intended use case:** The UI instance itself, all components (both UI and non-UI) that share the lifecycle of the UI.
 
**Known limitations:**

- Vaadin UI components cannot be proxied.
- Beans in this scope cannot be ```ApplicationListener```s.

### The Vaadin View Scope ###

This scope binds the bean to the lifecycle of a Vaadin View (from the Navigation API). The lifecycle starts when a user 
navigates into the view and ends when the user leaves the view. In addition, the scope is active while the view is being created. In theory,
this means that there can be two view scopes active at the same time since the new view is created before the old view
is deactivated. Normally, this should not cause any conflicts but you should be aware of it anyway, especially if you
have multiple threads that might interact with the navigation API at the same time.

You use this scope by adding the ```@VaadinViewScope``` to your View class. In addition, you need to configure your
application to use Spring4Vaadin's view provider (see below for details).

**Intended use case:** Views, all components (both UI and non-UI) that share the lifecycle of a view.

**Known limitations:**

- Vaadin UI components cannot be proxied.
- Beans in this scope cannot be ```ApplicationListener```s.
- The view scope will only work if the View instance itself is also view scoped and it is being created by a ```SpringViewProvider```.

### When to use the Spring prototype scope? ###

The prototype scope works perfectly with Vaadin applications. You can use it both for UI and non-UI components. You
only need to be aware of the fact that prototype scoped beans *are not lifecycle managed*. This means for example that
you will not be able to use ```@PreDestroy``` methods to clean up resources, stop background threads, etc.
 
If you decide you will need to use the prototype scope for UI components, and you need to do resource allocation and release, 
it is recommended to handle these in the ```attach()``` and ```detach()``` methods of your component. This will mean
that resources are allocated when the component is added to a UI and released when the component is removed or the UI is destroyed.
Be aware, however, that if your application is deployed as a WAR in a servlet container, and you undeploy the application, no ```detach()``` methods
will be called.

Since the prototype scope is many times useful in a Vaadin application, Vaadin4Spring provides a ```@PrototypeScope``` annotation
for convenience.

**Intended use case:** UI components that are used in many places of your application

## Vaadin Navigation API support ##

This add-on provides support for the Vaadin Navigator API by making it possible to add new views by simply annotating them.
To set it up, follow these instructions:

1. Annotate your views with ```@VaadinView``` and put them into either the ```@VaadinUIScope```, the ```@VaadinViewScope``` or the ```@PrototypeScope```. 
2. Inject an instance of ```SpringViewProvider``` into your UI and add it as a view provider to your ```Navigator```
instance.

Please see the JavaDocs and the [sample application](samples/navigation-sample) for more information.

## The Event Bus ##

Vaadin4Spring provides an event bus that can be used in addition to the Spring provided event bus. The event bus
supports scoping events to the current UI instance, the current Vaadin session or the entire application.

More information can be found [here](spring-vaadin-eventbus/README.md).

## Internationalization (i18n) ##

Internationalization in a Vaadin4Spring application is very much handled by
the I18N features of Spring itself. However, Spring4Vaadin provides some helper classes
to make it easier.

More information can be found [here](spring-vaadin-i18n/README.md).

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

## The Side Bar ##

The side bar can be used as a main menu in applications with many views. It is implemented as a Vaadin accordion and divided into sections. Every section contains clickable menu items. Both sections and menu items are added declaratively using annotations.

More information can be found [here](spring-vaadin-sidebar/README.md).

## Security Support ##
#### Experimental ####
The Spring Security integration is still in very early stages of development. Do not assume it will work.
More Information can be found [here](spring-vaadin-security/README.md)

## Testing

Vaadin4Spring provides helpers to test UI scoped objects, especially when
following the Model, Controller or Presenter patterns.

The testing module has its own README, you can find it
[here](spring-vaadin-test/README.md).

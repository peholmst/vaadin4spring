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

Run the ```main(String[])``` methods in the generated ```Application``` class and point your browser to http://localhost:8080. Voila!

## Configuring the Vaadin servlet ##

To be written

## The UI Scope ##

To be written

## Navigator API integration ##

To be written

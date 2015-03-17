Vaadin Model-View-Presenter
===========================

This is a completion of the work started by Josh and Petter to add a "Presenter" to the mix of goodies
offered in [spring-vaadin](https://github.com/peholmst/vaadin4spring/tree/master/spring-vaadin/src/main/java/org/vaadin/spring/navigator).

This project adds a base class and an annotation:

* [Presenter](https://github.com/fastnsilver/vaadin4spring/blob/master/spring-vaadin-mvp/src/main/java/org/vaadin/spring/navigator/Presenter.java)
* [VaadinPresenter](https://github.com/fastnsilver/vaadin4spring/blob/master/spring-vaadin-mvp/src/main/java/org/vaadin/spring/navigator/VaadinPresenter.java)

## Quick Start ##

Add the following Maven dependency to your Vaadin / Spring boot application's POM...

    <dependency>
        <groupId>org.vaadin.spring</groupId>
        <artifactId>spring-vaadin-mvp</artifactId>
    </dependency>
    

Then for examples of `Presenter` implementation, have a look at those found in the `presenter` package of the [mvp-sample](https://github.com/fastnsilver/vaadin4spring/tree/master/samples/mvp-sample/src/main/java/org/vaadin/spring/samples/mvp/ui/presenter) application.
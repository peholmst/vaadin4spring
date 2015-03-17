Add-ons for Vaadin Spring
=========================

This directory contains add-ons that use the Vaadin Spring add-on to implement different features that might be useful
to your application. They will most likely not end up in the official add-on.

## The Event Bus ##
   
Vaadin4Spring provides an event bus that can be used in addition to the event published provided by Spring. The event bus
supports scoping events to the current View instance, current UI instance, current Vaadin session or the entire application.
   
More information can be found [here](eventbus/README.md).
   
## Internationalization (I18N) ##
   
Internationalization in a Vaadin4Spring application is very much handled by the I18N features of Spring itself. 
However, Spring4Vaadin provides some helper classes to make it easier.
   
More information can be found [here](i18n/README.md).
     
## The Side Bar ##
   
The side bar can be used as a main menu in applications with many views. It is implemented as a Vaadin accordion and divided into 
sections. Every section contains clickable menu items. Both sections and menu items are added declaratively using annotations.
   
More information can be found [here](sidebar/README.md).

## Model-View-Presenter support

There are two community contributed add-ons for implementing MVP. They take different approaches, so have a 
look at both and pick the one you like the most:

* [MVP](mvp/README.md) by Chris Phillipson
* [Simple MVP](simple-mvp/README.md) by Nicolas Frankel

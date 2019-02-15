Vaadin4Spring Extensions and Addons
===================================

NOTE: Vaadin Flow version is under development in flow branch.

This project started as a prototype of an official Vaadin Spring add-on. Now there is [one](http://github.com/vaadin/spring)
and as of Vaadin4Spring 0.0.6, *this project is to be considered an add-on for the official Spring add-on*. This means that all the
scopes, the UI provider, the navigator support etc. has been moved to the official add-on, and removed from this project.

So, what is left then?

I have decided to split this project up into three parts: [extensions](extensions/README.md), [addons](addons/README.md)
and [samples](samples/README.md).

The **extensions** part contains extensions of the core functionality of the official Vaadin Spring add-on. It also acts as a
prototype for features that might eventually end up in the official add-on as well. Basically, this is all that is left
of the old ```spring-vaadin``` and ```spring-vaadin-boot``` modules.

The **addons** part contains more or less stand-alone components that can be used in Vaadin Spring applications, but likely 
will not be merged into the official add-on (at least not any time soon). This includes the event bus, I18N support and side bar,
among other things.

Finally, the **samples** part contains small sample applications that demonstrate either the official Vaadin Spring add-on,
or some parts of Vaadin4Spring.

Please familiarize yourself with the READMEs of each part for more information.

## Please note!

I am currently in the process of migrating the old project to the new directory structure and not all modules have
been migrated yet. It is not my intention to delete anything that has not been migrated to the official add-on,
so if your module has not been migrated yet, just be patient. Also, some of the READMEs of the already migrated modules
have not been updated yet.

There is a separate [branch](https://github.com/peholmst/vaadin4spring/tree/legacy) for the old codebase (pre 0.0.6)
in case you need it. However, if you have any pull requests, I kindly ask you to do them against the new codebase,
if only possible.

vaadin4spring-demo
==============

Template for a full-blown Vaadin application that only requires a Servlet 3.0 container to run (no other JEE dependencies).


Project Structure
=================

The project consists of the following three modules:

- parent project: common metadata and configuration
- vaadin4spring-demo-widgetset: widgetset, custom client side code and dependencies to widget add-ons
- vaadin4spring-demo-ui: main application module, development time
- vaadin4spring-demo-production: module that produces a production mode WAR for deployment

The production mode module recompiles the widgetset (obfuscated, not draft), activates production mode for Vaadin with a context parameter in web.xml and contains a precompiled theme. The ui module WAR contains an unobfuscated widgetset, and is meant to be used at development time only.

Workflow
========

To compile the entire project, run "mvn install" in the parent project.

Other basic workflow steps:

- getting started
- compiling the whole project
  - run "mvn install" in parent project
- developing the application
  - edit code in the ui module
  - run "mvn jetty:run" in ui module
  - open http://localhost:8080/
- developing the theme
  - run the application as above
  - edit the theme in the ui module
  - optional: see below for precompiling the theme
  - reload the application page
- client side changes or add-ons
  - edit code/POM in widgetset module
  - run "mvn install" in widgetset module
  - if a new add-on has an embedded theme, run "mvn vaadin:update-theme" in the ui module
- debugging client side code
  - run "mvn vaadin:run-codeserver" in widgetset module
  - activate Super Dev Mode in the debug window of the application
- creating a production mode war
  - run "mvn -Pproduction package" in the production mode module or in the parent module
- testing the production mode war
  - run "mvn -Pproduction jetty:run-war" in the production mode module


Using a precompiled theme
-------------------------

When developing the UI module, Vaadin can compile the theme on the fly on every
application reload, or the theme can be precompiled to speed up page loads.

To precompile the theme run "mvn vaadin:compile-theme" in the ui module. Note, though,
that once the theme has been precompiled, any theme changes will not be visible until
the next theme compilation or running the "mvn clean" target.

When developing the theme, running the application in the "run" mode (rather than
in "debug") in the IDE can speed up consecutive on-the-fly theme compilations
significantly.

The production module always automatically precompiles the theme for the production WAR.

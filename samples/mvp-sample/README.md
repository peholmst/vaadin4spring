MVP Sample
==========

This is just a sample application that employs all the "good stuff" from Vaadin Spring Boot libraries, with a
focus on MVP architecture.

## Quick Start ##

Clone this repository `git clone https://github.com/fastnsilver/vaadin4spring.git`

Build the "libraries" with Maven `mvn clean install`

Change directories into `samples`

Build the "applications" with Maven `mvn clean install`

To run the `mvp-sample` application, change directories into `mvp-sample`, and execute:

    java -jar mvp-sample-x.x.x-SNAPSHOT.war
    
where x.x.x is the current `SNAPSHOT` version

Launch your browser and visit

    http://localhost:8080/mui
    
Primary navigation is driven by clicking on nodes in left-hand tree-pane.

Click on `DSR`.

Note that the right-hand area will display a list of tabs; this is the secondary navigation.

Click on `DSR Hourly Updates`.

Note that the header (an area above the left and right-hand panes) gets populated with some controls.

The controls drive what will ultimately be displayed in the right-hand pane. (Typically a grid or form).
Also, note the controls are chained with `ValueChangeListener`s. (E.g., selecting a `Market Day` will limit the list of available `Participant`s).

See [MockParticipantDAO](https://github.com/fastnsilver/vaadin4spring/blob/master/samples/mvp-sample/src/main/java/org/vaadin/spring/samples/mvp/ui/mock/MockParticipantDAO.java) for which participants are "effective" on a particular date.
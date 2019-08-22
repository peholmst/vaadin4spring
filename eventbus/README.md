The Vaadin4Spring Event Bus
===========================

**Do not use this event bus in new projects.** This version is published to make it easier to port existing
applications that use Vaadin4Spring from Vaadin 8 to Vaadin 14.

## Security Issue with the Application Scoped Event Bus

You can run into serious security problems with the application scoped event bus if you are not careful. Events are
dispatched synchronously to all listeners in the thread that originally fired the event. This means that **all
event listeners will run within the security context of the user that fired the event**. 

Because of this I seriously discourage developers from using the application scoped event bus in their projects.
I can't fix this issue either since that would break existing software that relies on the events being dispatched
in this way. 

There are discussions of creating a completely new event bus for Vaadin 14+ that would not have this design flaw.
 
The Vaadin4Spring Event Bus
===========================

This add-on provides its own event bus framework, with the intention of complementing Spring's own event publisher.

**Please note, that the Event Bus API changed in version 0.0.5.** From now on, you have to declare which event bus
to inject by using a specific interface (previously, everything was ```EventBus``` and you used an annotation to specify
which bus to get). The reasons for this change were:
 
* to make it easier to fetch the correct event bus from the application context programmatically, and
* to work around a problem that arose when not all of the scopes were active at the time of injection of the event bus

# Event Bus Scopes

Currently, there are four types of event buses that are attached to different scopes:

1. The View event bus is specific to the current view instance.
2. The UI event bus is specific to the current UI instance. If there is a view active, and it uses a view 
   event bus, events published on this event bus will propagate to the view event bus as well.
3. The Session event bus is specific to the current Vaadin session. Events published on this event bus will propagate
   to all UI event buses that are part of the session.
4. The Application event bus is global to the web application. Events published on this event bus will propagate to
   all session event buses (and all UI event buses).

Events published by Spring's own event publisher **are not automatically propagated** to the application event bus.
If this is what you want (the default behaviour up to and including version 0.0.3), you can easily enable it by
creating a singleton instance of ```ApplicationContextEventBroker```:

```java
 @Autowired
 EventBus.ApplicationEventBUs eventBus;
 ...
 @Bean
 ApplicationContextEventBroker applicationContextEventBroker() {
     return new ApplicationContextEventBroker(eventBus);
 }
```    

# Injecting the Event Bus

The event bus is automatically enabled when you enable the Spring4Vaadin add-on. You can just autowire in an instance
of any of the subinterfaces of ```EventBus```, depending on which event bus you want, like this:

```java
 @Autowired
 EventBus.ApplicationEventBus applicationEventBus;
 
 @Autowired
 EventBus.SessionEventBus sessionEventBus;
 
 @Autowired
 EventBus.UIEventBus uiEventBus;
 
 @Autowired
 EventBus.ViewEventBus viewEventBus; 
```

By default, you will inject the actual instance of the event bus. If you want to inject a proxy instead, you can use:

```java
 @Autowired
 @EventBusProxy
 EventBus.SessionEventBus eventBus;
```

All event buses except the application event bus can be proxied.

# Publishing Events

You can publish any kind of object as an event on the event bus by invoking one of the ```publish(...)``` methods.
Currently, you can either choose to publish the event on the event bus itself, or on any of its parent event buses.
For example, even though you have injected the UI event bus, you can still use it to publish events on the
session or application event buses.

Example:

```java
 @Autowired
 EventBus.UIEventBus myUIScopedEventBus;
 ...
 myUIScopedEventBus.publish(this, "This will be published on the UI scoped event bus");
 myUIScopedEventBus.publish(EventScope.SESSION, this, "This will be published on the session scoped event bus");
```

# Receiving Events

To be able to receive events from an event bus, you must explicitly subscribe to it using any of 
the ```subscribe(...)```methods. A subscriber can be created in three ways:

1. Implement the ```EventBusListener``` interface. The type of event (the payload) you are interested in is deduced from
   the type parameter of the listener.
2. Create at least one method that conforms to the following signature: ```myMethodName(Event<MyPayloadType>)``` and
   annotate it with ```@EventBusListenerMethod```.
3. Create at least one method that conforms to the following signature: ```myMethodName(MyPayloadType)``` and
   annotate it with ```@EventBusListenerMethod```.

When subscribing to an event bus, you can also define whether you want to receive propagating events as well. By default,
this is true, which means that events published on the parent event buses will also be delivered to the subscriber. If
false, only events published on that particular event bus will be delivered.

## Known Limitations

Currently, you cannot use JDK 8 lambdas to subscribe to events. This has to do with the way
the payload is currently deduced (issue #44).

# More Information

The JavaDocs of the event bus classes and interfaces are pretty thorough, so please have a look at them (and of course
the code itself) for more information about how things work.

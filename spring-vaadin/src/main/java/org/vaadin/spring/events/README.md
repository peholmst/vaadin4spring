The Vaadin4Spring Event Bus
===========================

This add-on provides its own event bus framework, with the intention of complementing Spring's own event publisher.

# Event Bus Scopes

Currently, there are three types of event buses that are attached to different scopes:

1. The UI scoped event bus is specific to the current UI instance.
2. The Session scoped event bus is specific to the current HTTP session. Events published on this event bus will propagate
   to all UI scoped event buses that are part of the session.
3. The Application scoped event bus is global to the web application. Events published on this event bus will propagate to
   all session scoped event buses (and all UI scoped event buses).

Events published by Spring's own event publisher are automatically propagated to the application scoped event bus.

# Injecting the Event Bus

The event bus is automatically enabled when you enable the Spring4Vaadin add-on. You can just autowire in an instance
of the ```EventBus``` interface, like this:

```java
 @Autowired
 EventBus eventBus;
```

By default, this will inject the actual instance of the UI scoped event bus. You can tweak the injection using the
```@EventBusScope``` annotation. For example, if you want a proxy that delegates to the session scoped event bus
of the currently executing thread, you could use:

```java
 @Autowired
 @EventBusScope(value = EventScope.SESSION, proxy = true)
 EventBus eventBus;
```

# Publishing Events

You can publish any kind of object as an event on the event bus by invoking one of the ```publish(...)``` methods.
Currently, you can either choose to publish the event on the event bus itself, or on any of its parent event buses.
In other words, even though you have injected the UI scoped event bus, you can still use it to publish events on the
session or application scoped event buses.

Example:

```java
 @Autowired
 EventBus myUIScopedEventBus;
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

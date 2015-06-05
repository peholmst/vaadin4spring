# Simple Model-View-Presenter

Some features of the current Vaadin Spring implementation are very opinionated and IMHO have definite disadvantages:

* It relies on autowiring, which makes it extremely hard to **unit test** *Presenter* classes. Corollary, the provided test class is not a unit test, but an **integration test**
* It relies on component scanning, which prevents explicit dependency injection
* It takes responsibility of creating the *View* 
* It enforces the implementation of the `View` interface, whether required or not
* It couples the *Presenter* and the *View*, whereas one can have a single *Presenter* for different *View* implementation
* It requires to explicitly call the `init()` method of the *Presenter*, as the annotation of this super class is never called

This module aims to provide the same capabilities but in a different way, so that the above cons are negated. As there's no such thing as a free lunch, the trade-off is that it makes things more verbose.



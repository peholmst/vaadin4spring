# Testing UI Scoped Objects

Spring has a nice way of doing integration tests by making it possible to set up an application context, and
then inject beans from it into the test case. For Vaadin applications, this approach does not really work since
there are a lot of background services that would have to be mocked for everything to work properly. However,
there are still valid use cases where you want to test a UI-scoped bean, for example a Model, a Controller
or a Presenter.

You can do this by adding the ```@VaadinAppConfiguration``` to your test class, like this:

```java
@RunWith(SpringJUnit4ClassRunner.class)
@VaadinAppConfiguration
@ContextConfiguration(classes = MyIntegrationTestConfig.class)
public class MyTest {
     @Autowired MyUIScopedController myController;
     ...
}
```

When running this test, the UI and VaadinSession scopes will be changed so that they behave correctly in the
context of the test, even though there is no active session nor a UI. This makes it possible to inject both UI-scoped 
and VaadinSession scoped beans into the test and perform operations on them (including
the event buses). For an example, see [this test case](src/test/java/org/vaadin/spring/test/ExampleIntegrationTest.java).

## When the Test Execution Listener does not work

Sometimes, you might need to test UI scoped objects using a separate test runner. In this case you might have to set up
the scopes manually if the required test execution listeners aren't registered properly using 
the ```@VaadinAppConfiguration``` annotation.

Create set up (```@Before```) and tear down (```@After```) methods and invoke the ```VaadinScopes.setUp()``` 
and ```VaadinScopes.tearDown()``` methods in them, respectively (this is what the test execution listener does automatically).

You cannot access any scoped objects before the scopes have been set up. If you are still able to inject beans into
your test case, you cannot inject any scoped beans directly. Instead, inject your scoped beans as ```Provider```s,
and request the actual bean inside the test method. For an example, see 
[this test case](src/test/java/org/vaadin/spring/test/ExampleManualIntegrationTest.java).

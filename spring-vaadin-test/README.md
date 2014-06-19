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

When running this test, a mocked ```UI``` instance will be created and activated for each test method, making it possible
to inject both UI-scoped and session scoped beans into the test and performing operations on them (including
the event buses). For an example, see [this test case](src/test/java/org/vaadin/spring/test/ExampleIntegrationTest.java).

## When the Test Execution Listener does not work

Sometimes, you might need to test UI scoped objects using a separate test runner. In this case you might have to set up
the mocks manually if the required test execution listeners aren't registered properly using 
the ```@VaadinAppConfiguration``` annotation.

Create set up (```@Before```) and tear down (```@After```) methods and invoke the ```MockUI.setUp(ApplicationContext)``` 
and ```MockUI.tearDown()``` methods in them, respectively (this is what the test execution listener does automatically).
This still assumes that the HTTP session and HTTP request have been mocked properly (a Spring feature). If this does
not happen, you have to set up the session and request mocks manually.

You cannot access any scoped objects before the Mock UI has been set up. If you are still able to inject beans into
your test case, you cannot inject any scoped beans directly. Instead, inject your scoped beans as ```Provider```s,
and request the actual bean inside the test method. For an example, see 
[this test case](src/test/java/org/vaadin/spring/test/ExampleManualIntegrationTest.java).

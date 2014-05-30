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

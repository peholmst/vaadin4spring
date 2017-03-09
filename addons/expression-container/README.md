Vaadin Expression-Container
===========================

This project offers a special container that behaves a lot like Vaadin's BeanItemContainer but also
supports [Spring Expression Language](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html)
as property-ids, thus enabling vaadin applications to have code like this:

``` java

    class NumbersBean{
        int a;
        float b;
    }

    ExpressionContainer expressionContainer = new ExpressionContainer(NumbersBean.class);

    Item myNumbersBeanItem = expressionContainer.addItem(myNumbersBean);

    Property fiveTimesTheAValueProperty = item.getItemProperty("a * 5");

    Property aPlusBProperty = item.getItemProperty("a + b");
```

## Quick Start ##

Add the following Maven dependency to your Vaadin / Spring boot application's POM...

    <dependency>
        <groupId>org.vaadin.spring.addons</groupId>
        <artifactId>vaadin-spring-addon-expression-container</artifactId>
        <version>0.1-SNAPSHOT</version>
    </dependency>
    

Then for examples of `ExpressionContainer` implementation, have a look at those found in the  sample application.
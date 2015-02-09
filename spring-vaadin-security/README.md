Vaadin4Spring Security (Experimental)
==========================
<br>
## General ##
Spring security for a Vaadin application can be enabled by the annotation ```@EnableVaadinSecurity```.
<br><br>
```GlobalMethodSecurity``` is enabled by default, for ```@Secured``` annotations and ```@PreAuthorize``` annotations. ```@PostAuthorize``` is currently not supported.
<br><br>
Because ```@PreAuthorize``` annotations are enabled a default ```AccessDecisionManager``` is created.

#### Override ```AccessDecisionManager``` ####
A custom ```AccessDecisionManager``` can be implemented by defining a AccessDecisionManager with either the id of ```VaadinSecurityConfiguration.Beans.ACCESS_DECISION_MANAGER``` or by providing your custom AccessDecisionManager with the annotation ```@Bean(name = VaadinSecurityConfiguration.Beans.ACCESS_DECISION_MANAGER)```

## VaadinSecurityContext ###
The VaadinSecurityContext interface provides access to common spring security objects.

- ```ApplicationContext```
- ```AuthenticationManager```
- ```AccessDecisionManager```

#### Methods ####

- ```ApplicationContext getApplicationContext();```
- ```AuthenticationManager getAuthenticationManager();```
- ```AccessDecisionManager getAccessDecisionManager();```
- ```boolean hasAccessDecisionManager();```

#### Usage ####
The ```VaadinSecurityContext``` can be used by either use of ```@Autowired```. Or by the use of the ```VaadinSecurityContextAware``` interface. This works the same like the Spring ```ApplicationContextAware``` interface.

Example Autowired
```java
@Autowired
VaadinSecurityContext vaadinSecurityContext;
...
```

Example VaadinSecurityContextAware
```java
public class Dummy implements VaadinSecurityContextAware {

    private VaadinSecurityContext vaadinSecurityContext;

    @Override
    public void setVaadinSecurityContext(VaadinSecurityContext vaadinSecurityContext) {
        this.vaadinSecurityContext = vaadinSecurityContext;
    }

...
}
```

## SecuredUI / Single-UI##
To create a secured Vaadin UI, with single page security support; one can now extend from ```SecuredUI``` instead of the default ```UI```.
This gives you a default secured UI with a embedded ```SecuredNavigator```.

When using this 2 methods are required to implement.

- ```String defaultAuthenticationView()```
- ```String notFoundView()```

With ```defaultAuthenticationView()``` you must return the view name of the Authentication view which shows the login view to the user.

With ```notFoundView()``` you must return the view name of the view which shows some kind of 404 View not found.

See [Security Single-UI Sample](../samples/security-sample-single) for a working example.




## VaadinSecurity ##
The VaadinSecurity interface provides access to commonly required Spring Security operations in a Vaadin application. It also extends ```VaadinSecurityContext```.

#### Methods ####

-  ```boolean isAuthenticated();```
-  ```void login(Authentication authentication) throws AuthenticationException;```
-  ```void login(String username, String password) throws AuthenticationException;```
-  ```void logout();```
-  ```boolean hasAuthority(String authority);```
-  ```Authentication getAuthentication();```
-  ```boolean hasAccessToObject(Object securedObject, String... securityConfigurationAttributes);```
-  ```boolean hasAccessToSecuredObject(Object securedObject);```
-  ```boolean hasAccessToSecuredMethod(Object securedObject, String methodName, Class<?>... methodParameterTypes);```
-  ```boolean hasAuthorities(String... authorities);```
-  ```boolean hasAnyAuthority(String... authorities);```
-  ```void setSpringSecurityContextKey(String springSecurityContextKey);```

##### Methods Inherited from VaadinSecurityContext #####
- ```ApplicationContext getApplicationContext();```
- ```AuthenticationManager getAuthenticationManager();```
- ```AccessDecisionManager getAccessDecisionManager();```
- ```boolean hasAccessDecisionManager();```

#### Usage ####
```VaadinSecurity``` can be used by either use of ```@Autowired```. Or by the use of the ```VaadinSecurityAware``` interface. This works the same like the Spring ```ApplicationContextAware``` interface.

Example Autowired
```java
@Autowired
VaadinSecurity vaadinSecurity;
...
```

Example VaadinSecurityAware
```java
public class Dummy implements VaadinSecurityAware {

    private VaadinSecurity vaadinSecurity;

    @Override
    public void setVaadinSecurity(VaadinSecurity vaadinSecurity) {
        this.vaadinSecurity = vaadinSecurity;
    }

...
}
```

## SpringSecurityContextKey ##
Default Spring-Security uses a ```SPRING_SECURITY_CONTEXT_KEY``` with value of ```"SPRING_SECURITY_CONTEXT"```. This key is used throughout the springSecurityFilterChain. The ```HttpSessionSecurityContextRepository``` uses this key to search for a available ```Authentication``` object within the ```HttpSession```. 

Some users like to change this key.

#### Changing SpringSecurityContextKey ####
When you implement your own ```HttpSessionSecurityContextRepository``` or you overwrite the default implementation and you change the ```SPRING_SECURITY_CONTEXT_KEY```, you need to set the same key to ```VaadinSecurity```.

This can be done with the method ```.setSpringSecurityContextKey(String springSecurityContextKey)``` on the ```VaadinSecurity``` object.

This ensures that ```VaadinSecurity``` and the ```HttpSessionSecurityContextRepository``` don't go out of sync.



## Remember Me ##

Implementation Notice:<br>
When implementing remember me functionality the ```RememberMeServices``` bean has to be created before the ```@EnableVaadinSecurity```. 

The ```VaadinSecurity``` object has a non-required dependency on ```RememberMeServices```. 

If a ```RememberMeServices``` bean is found and created within the ```@Configuration``` class which has ```@EnableVaadinSecurity``` on it, you could end up with an exception.

The ```RememberMeServices``` bean need to be available before the ```@EnableVaadinSecurity``` annotation is called.


Within the same below the ```RememberMeServices``` bean is created before the ```@EnableVaadinSecurity``` annotation is called.

When using spring-boot using a ```@AutoConfigureBefore``` is also a good solution.


```java

@Configuration
@ComponentScan
public class SecurityConfiguration {

    @Autowired
    JdbcUserDetailsService userDetailsService;

    @Autowired
    DataSource dataSource;

    @Bean
    public PersistentTokenRepository jdbcTokenRepository() {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setCreateTableOnStartup(false);
        repository.setDataSource(dataSource);
        return repository;
    }

    @Bean
    public RememberMeServices persistentTokenBasedRememberMeServices() {
        VaadinPersistentTokenBasedRememberMeServices services = new VaadinPersistentTokenBasedRememberMeServices(
                "vaadin4spring",
                userDetailsService,
                jdbcTokenRepository());
        services.setCookieName("REMEMBERME");
        return services;
    }

    @Configuration
    @EnableVaadinSecurity
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter implements InitializingBean {

        @Autowired
        private VaadinSecurityContext vaadinSecurityContext;

        @Autowired
        private VaadinRedirectStrategy redirectStrategy;

        @Autowired
        JdbcUserDetailsService userDetailsService;

        @Autowired
        DataSource dataSource;


		// Autowire the Earlier created bean, so it can be used.
        @Autowired
        RememberMeServices rememberMeServices;

        @Override
        public void afterPropertiesSet() throws Exception {
            ...
        }

        @Bean(name = "authenticationManager")
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

    ...

```

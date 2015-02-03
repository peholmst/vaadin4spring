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

## Remember Me ##

Implementation Notice:<br>
When implementing remember me functionality the ```RememberMeServices``` bean has to be created before the ```@EnableVaadinSecurity```. The ```VaadinSecurity``` object has a non-required dependency on ```RememberMeServices```. If a ```RememberMeServices``` bean is found and created within the ```@Configuration``` class which has ```@EnableVaadinSecurity``` on it, you could end up with an exception because the ```@EnableVaadinSecurity``` creates the ```VaadinSecurity``` object and the ```RememeberServices``` bean might be created after the annotation.

The following configuration will not work, because the ```RememberMeServices``` bean is created within the class which has ```@EnableVaadinSecurity``` on it.


```java
@Configuration
@ComponentScan
public class SecurityConfiguration {

    ...

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

    ...
```

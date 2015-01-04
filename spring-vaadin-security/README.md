Vaadin4Spring Security (Experimental)
==========================
<br>
## General ##
Spring security for a Vaadin application can be enabled by the annotation ```@EnableVaadinSecurity```.
<br><br>
```GlobalMethodSecurity``` is enabled by default, for ```@Secured``` annotations and ```@PreAuthorize``` annotations. ```@PostAuthorize``` is currently not supported.
<br><br>
Because ```@PreAuthorize``` annotations are enabled a default ```AccessDecisionManager``` is created.

### Override ```AccessDecisionManager``` ###
A custom ```AccessDecisionManager``` can be implemented by defining a AccessDecisionManager with either the id of ```VaadinSecurityConfiguration.Beans.ACCESS_DECISION_MANAGER``` or by providing your custom AccessDecisionManager with the annotation ```@Bean(name = VaadinSecurityConfiguration.Beans.ACCESS_DECISION_MANAGER```

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
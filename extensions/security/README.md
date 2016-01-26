Vaadin4Spring Security (Experimental)
==========================

There are currently two ways of integrating Vaadin and Spring Security using Vaadin4Spring: Shared Security and Managed Security

## Shared Security

When using shared security, the Vaadin application will participate in an external Spring Security set up just like any web application.
Login and logout are handled by redirecting to specific URLs and the Vaadin URLs are protected by the Spring Security filters.

The [sample application](../../samples/security-sample-shared) demonstrates how shared security works in practice. Also check the JavaDocs and, of course,
the source code.

**When to use:**

- When you already have Spring Security set up
- When you share the session with Vaadin applications and non-Vaadin applications
- When you need to use a custom authentication method already supported by Spring 
- When you need to support remember me authentication

**Drawbacks:**

- You have to do some configuring to get everything up and running
- If you have a very customized Spring Security configuration, you might have to ditch the ```@EnableVaadinSharedSecurity``` configuration
  and set everything up manually.

### Quick set-up instructions

- Add the ```@EnableVaadinSharedSecurity``` annotation to your ```WebSecurityConfigurerAdapter``` class.
- Disable CSRF-protection in either Spring or Vaadin. If you have both turned on, your application will not work.
- Make the following objects available as managed Spring beans (if you are going to use any of them in your application):
  - ```AuthenticationManager``` for authentication.
  - ```AccessDecisionManager``` for authorization
  - ```RememberMeServices``` if you want to support Remember Me authentication. Also remember to plug this instance into Spring Security when configuring ```HttpSecurity```.
  - ```SessionAuthenticationStrategy``` if you want to use session authentication. Also remember to plug this instance into Spring Security when configuring ```HttpSecurity```.    
- Configure request authorization, login and logout URLs, etc.
- Add a ```VaadinSessionClosingLogoutHandler``` as a Spring logout handler.
- If you are using Spring Boot *and* plan to use a Vaadin UI for the login screen, you need to permit access to ```/vaadinServlet/UIDL/**```
  and ```/vaadinServlet/HEARTBEAT/**```. You also need to configure a ```LoginUrlAuthenticationEntryPoint``` that redirects to your login UI.
  - By default, you will be redirected to ```/``` after a successful login. If you want to change this, you need to declare another ```VaadinAuthenticationSuccessHandler```
  bean using the constant ```VaadinSharedSecurityConfiguration.VAADIN_AUTHENTICATION_SUCCESS_HANDLER_BEAN``` as the bean name. 
  - Login errors should be handled in your login UI. They will be thrown as exceptions when you invoke the login method of ```VaadinSharedSecurity``` and can be caught and displayed to the user.
- You can use web socket based push for your main Vaadin UI as soon as the user has been authenticated. This means that if you want to use a
  Vaadin UI for your login screen, that UI must not use web socket based push.
- Finally, you might want to ignore Spring Security completely for the Vaadin static resources, i.e. ```/VAADIN/**```.

## Managed Security

When using managed security, your Vaadin application will take care of the security integration itself. It will handle authentication, session management and
storing and retrieving of the security context. Spring Security will only kick in on the backend layer, using method security.

The [sample application](../../samples/security-sample-managed) demonstrates how managed security works in practice. 

**When to use:**

- When you want to handle security within a single Vaadin UI and don't need to share the session with non-Vaadin web applications
- When you don't want to configure Spring Security yourself

**Drawbacks:**

- Remember me authentication currently does not work since it requires access to cookies and you can't have that if you use web socket based push

### Quick set-up instructions

- Add the ```@EnableVaadinManagedSecurity``` annotation to your application or configuration class. This will enable global method security
  and make the necessary security beans available for injection.
- If you are using Spring Boot, exclude the default security auto configuration by using this annotation: ```@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class)```.
- Implement ```AuthenticationManagerConfigurer``` and annotate it with the ```@Configuration``` annotation. This allows you to configure how
  users are authenticated.
- Implement your UI, using the ```VaadinManagedSecurity``` service to interact with Spring Security.

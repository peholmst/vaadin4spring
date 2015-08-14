Vaadin4Spring Security (Experimental)
==========================

There are currently two ways of integrating Vaadin and Spring Security using Vaadin4Spring: Shared Security and Managed Security

## Shared Security

When using shared security, the Vaadin application will participate in an external Spring Security set up just like any web application.
Login and logout are handled by redirecting to specific URLs and the Vaadin URLs are protected by the Spring Security filters.

The [sample application](../../samples/security-sample-shared) demonstrates how shared security works in practice.

**When to use:**

- When you already have Spring Security set up
- When you share the session with Vaadin applications and non-Vaadin applications
- When you need to use a custom authentication method already supported by Spring 
- When you need to support remember me authentication

**Drawbacks:**

- You cannot use web socket based push, since it will bypass the Spring Security filter chain
- You have to do some configuring to get everything up and running

### Quick set-up instructions

- Add the ```@EnableVaadinSharedSecurity``` annotation to your ```WebSecurityConfigurerAdapter``` class.
- Disable CSRF-protection in either Spring or Vaadin. If you have both turned on, your application will not work.
- Configure request authorization, login and logout URLs, etc.
- If you are using Spring Boot and plan to use a Vaadin UI for the login screen, you need to permit access to ```/vaadinServlet/UIDL/**```
  and ```/vaadinServlet/HEARTBEAT/**```. You also need to configure a ```LoginUrlAuthenticationEntryPoint``` that redirects to your login UI,
  and define a ```VaadinAuthenticationSuccessHandler``` bean that redirects the user away from the login page after successful authentication.
  Please check the sample application for an example.
- Finally, you might want to ignore Spring Security completely for the Vaadin static resources, i.e. ```/VAADIN/**```.

## Managed Security

When using managed security, your Vaadin application will take care of the security integration itself. It will handle authentication, session management and
storing and retrieving of the security context. Spring Security will only kick in on the backend layer, using method security.

The [sample application](../../samples/security-sample-managed) demonstrates how managed security works in practice. **Please note that until
[bug 18206](https://dev.vaadin.com/ticket/18206) has been fixed, managed security DOES NOT WORK.** I have been using my own unofficial patched
version of Vaadin Spring during development. As soon as an official fix is released, I will update Vaadin4Spring to work with it.

**When to use:**

- When you want to handle security within a single Vaadin UI and don't need to share the session with non-Vaadin web applications
- When you don't want to configure Spring Security yourself
- When you want to use web socket based push

**Drawbacks:**

- Remember me authentication currently does not work since it requires access to cookies and you can't have that if you use web socket based push

### Quick set-up instructions

- Add the ```@EnableVaadinManagedSecurity``` annotation to your application or configuration class. This will enable global method security
  and make the necessary security beans available for injection.
- If you are using Spring Boot, exclude the default security auto configuration by using this annotation: ```@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class)```.
- Implement ```AuthenticationManagerConfigurer``` and annotate it with the ```@Configuration``` annotation. This allows you to configure how
  users are authenticated.
- Implement your UI, using the ```VaadinSecurity``` service to interact with Spring Security.


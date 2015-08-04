The Vaadin4Spring Side Bar
==========================

The side bar can be used as a main menu in applications with many views. The side bar is divided into sections and every section contains clickable menu items.
Both sections and menu items are added declaratively using annotations. 

At the moment, there are two implementations: 

* AccordionSideBar: a side bar implemented as a Vaadin accordion where the sections are tabs.
* ValoSideBar: a side bar implemented as a CSS layout using the new Valo theme.

# Enabling the Side Bar

To enable the side bar, add the ```@EnableSideBar``` annotation to your application configuration. The side bar requires the I18N addon, so you
might also have to add ```@EnableI18N``` unless you are using auto configuration.
After that, inject an instance of the side bar into your UI like this:

```java
 @Autowired
 ValoSideBar sideBar; // Could also use AccordionSideBar
 ....
 myLayout.addComponent(sideBar);
```

# Sections

Side bar sections are defined using the ```@SideBarSection```  and ```@SideBarSections``` annotations. These annotations
can be placed on any Spring managed bean. The side bar will automatically look them up from the application context
when it is initialized.

A section has the following properties:

1. A unique ID that is used to associate items with the section.
2. A caption. This caption can be either hard coded, or a code that is used to look up the actual caption from an
   ```I18N``` instance. In other words, captions can be localized, but changing the language on the fly is not supported.
3. A set of UIs that the section is available for. This is useful if your application context contains different UI classes
   that all use side bars, but want different sections to show up in different side bars. By default, a section shows up
   in all side bars of all UI classes.
4. An ordering number that is used to order the sections within the side bar.

Example:

```java
 @SideBarSections({
         @SideBarSection(id = Sections.PLANNING, caption = "Planning"),
         @SideBarSection(id = Sections.EXECUTION, caption = "Execution"),
         @SideBarSection(id = Sections.REPORTING, caption = "Reporting")
 })
 @Component
 public class Sections {
     public static final String PLANNING = "planning";
     public static final String EXECUTION = "execution";
     public static final String REPORTING = "reporting";
 }
```

# Items

Side bar items are defined using the ```@SideBarItem``` annotation.

All items share the following properties:

1. A section ID that declares which section the item should be added to.
2. A caption. This caption can be either hard coded, or a code that is used to look up the actual caption from an
   ```I18N``` instance. In other words, items can be localized, but changing the language on the fly is not supported.
3. An ordering number that is used to order the items within the side bar section.

## Action Items

An action item performs an operation when clicked by the user. An action item is a Spring managed bean that implements
the ```Runnable``` interface and is annotated with the ```@SideBarItem``` annotation. Any scope can be used, since
the bean is fetched from the application context every time the operation is invoked.

Example:

```java
 @SideBarItem(sectionId = Sections.EXECUTION,
         caption = "Operation 1")
 @Component
 public class ExecutionOperation1 implements Runnable {
 ...
 }
```
## View Items

A view item navigates to a specific view when clicked by the user. A view item is a Spring managed bean that implements
the ```View``` interface (from the Vaadin Navigator API) and is annotated with the ```@VaadinView``` annotation (in
addition to the ```@SideBarItem``` annotation, that is). Please remember to also specify the scope of the view
(UI or prototype).

Example:

```java
 @VaadinView(name = PlanningView1.VIEW_NAME)
 @SideBarItem(sectionId = Sections.PLANNING,
         caption = "View 1",
         order = 1)
 @UIScope
 public class PlanningView1 extends VerticalLayout implements View {
 ...
 }
```

## Item Icons

All items can also have icons. Currently you can choose between two types of icons: icons provided by the theme,
or FontAwesome icons.

### Theme Icons

Theme icons are images that are stored in the theme directory of your application. You use them by adding the
```@ThemeIcon``` or ```@LocalizedThemeIcon``` annotation to the item bean:

* The ```@ThemeIcon``` annotation takes the theme resource ID as a single attribute.
* The ```@LocalizedThemeIcon``` annotation takes a message key as a single attribute. This key is then used to look up
  the actual resource ID from an ```I18N``` instance.  In other words, item icons can be localized, but changing the 
  language on the fly is not supported.

Example:

```java
 @VaadinView(name = ReportingView1.VIEW_NAME)
 @SideBarItem(sectionId = Sections.REPORTING,
         caption = "View 1")
 @ThemeIcon("../runo/icons/64/folder.png")
 @UIScope
 public class ReportingView1 extends VerticalLayout implements View {
 ... 
 }
```

### FontAwesome Icons

Vaadin 7.2 introduced support for [FontAwesome](http://fortawesome.github.io/Font-Awesome/) icons by means of the
```FontAwesome``` enum class. You can use them as item icons by adding the ```@FontAwesomeIcon``` annotation to the
item bean.

Example:

```java
 @VaadinView(name = PlanningView1.VIEW_NAME)
 @SideBarItem(sectionId = Sections.PLANNING,
         caption = "View 1",
         order = 1)
 @FontAwesomeIcon(FontAwesome.ANDROID)
 @UIScope
 public class PlanningView1 extends VerticalLayout implements View {
 ...
 }
```

### Custom Icons

It is also possible to provide your own icons. Have a look at the JavaDocs of the [SideBarItemIcon](src/main/java/org/vaadin/spring/sidebar/annotation/SideBarItemIcon.java)
annotation and the [SideBarItemIconProvider](src/main/java/org/vaadin/spring/sidebar/SideBarItemIconProvider.java) interface for more information.

# Themeing

The AccordionSideBar includes some very basic styles optimized for the Reindeer theme. In most real world applications,
you want to change these. The styles you want to override can be found as constants in the [AccordionSideBar](src/main/java/org/vaadin/spring/sidebar/components/AccordionSideBar.java) class.

The ValoSideBar uses styles from the Valo theme only.
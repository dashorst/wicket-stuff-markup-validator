Wicket Stuff HTML5 Validator
============================

This project validates the output of your [Apache Wicket](http://wicket.apache.org)
application while running. It includes a error view showing each validation error,
with line-precise error messages.

<img src="https://raw.github.com/dashorst/wicket-stuff-markup-validator/gh-pages/images/screen1.png" alt="Screenshot" title="Screenshot of a validation error" maxwidth="854px" />

Validates the following w3c standards:

 * HTML 4.01 Strict
 * HTML 4.01 Transitional
 * XHTML 1.0 Strict
 * XHTML 1.0 Transitional
 * HTML 5

This project includes the HTML validator taken from [html
validator](http://validator.nu).

Usage
-----

Make sure you use Wicket 6.0.0 or newer. This validator does not run on earlier versions, but is compatible with newer versions. Java 6 is also a minimum requirement.

Add the Wicket Stuff validator to your POM as a dependency:

```xml
<dependency>
    <groupId>org.wicketstuff.htmlvalidator</groupId>
    <artifactId>wicketstuff-htmlvalidator</artifactId>
    <version>1.10</version>
</dependency>
```

Depending on your setup you need to use a different `scope` (for example test
or provided to prevent the validator to be deployed to production).

Add the following lines to your `Application`'s init method:

```java
@Override
protected void init() {
    super.init();
    getMarkupSettings().setStripWicketTags(true);
    getRequestCycleSettings().addResponseFilter(new HtmlValidationResponseFilter());
}
```

You might want to put a check for the configuration of your application 
around the addition of the response filter, to ensure that the filter doesn't run
in production mode:

```java
if (RuntimeConfigurationType.DEVELOPMENT == getConfigurationType()) {
    getRequestCycleSettings().addResponseFilter(new HtmlValidationResponseFilter());
}
```

License
-------

This project is distributed using the Apache 2 License (see LICENSE for more details
on other included software).

Build
-----

Building Wicket.Validator is not difficult: you need to have Java 6 and Maven 3 installed, 
and know how to checkout sources from github. Assuming you know how to use Git, you need 
to do the following on a command prompt:

```bash
$ git clone git://github.com/dashorst/wicket-stuff-markup-validator.git
$ cd wicket-stuff-markup-validator
$ mvn install
```

Then you can add the Wicket.Validator dependency to your own application using Maven.

Original sources
----------------

Most of the sources come from the htmlvalidator service, which has
its source code published on bitbucket.

Follow the build instructions found here: https://bitbucket.org/validator/build/src
to get all the necessary sources (patched and downloaded) onto your system.
Just make sure that the `checker` folder is next to the clone of the wicket validator folder.


The `whattf` module is derived from the syntax module:

* java code is taken from: https://bitbucket.org/validator/syntax/src/c7989b0788cb/non-schema
* src/main/resources is taken from: https://bitbucket.org/validator/syntax/src/c7989b0788cb73ad916980e122a470d730a51820/relaxng

Retrieve the sources:

    hg clone https://bitbucket.org/validator/syntax

Remove the current sources and copy the newly checked out one's:

    rm -rf wicketstuff-htmlvalidator-parent/whattf/src/main/java
    cp -r checker/syntax/non-schema/java/src wicketstuff-htmlvalidator-parent/whattf/src/main/java
    cp -r checker/syntax/relaxng/datatype/java/src/org wicketstuff-htmlvalidator-parent/whattf/src/main/java
    cp -r checker/validator/src/nu/validator/localentities wicketstuff-htmlvalidator-parent/whattf/src/main/resources/nu/validator
    
    rm -rf wicketstuff-htmlvalidator-parent/whattf/src/main/resources/relaxng/*
    cp checker/syntax/relaxng/* wicketstuff-htmlvalidator-parent/whattf/src/main/resources/relaxng

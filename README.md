Wicket Stuff HTML5 Validator
============================

This project validates the output of your [Apache Wicket](http://wicket.apache.org)
application while running. It includes a error view showing each validation error,
with line-precise error messages.

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

Make sure you use Wicket 1.5. This validator does not run on Wicket 1.4 or 
earlier. Java 6 is also a minimum requirement.

There is no release available at this time. You have to build a version locally yourself (using [Apache Maven](http://maven.apache.org).

Add the Wicket Stuff validator to your POM as a dependency:

    <dependency>
        <groupId>org.wicketstuff.htmlvalidator</groupId>
        <artifactId>htmlvalidator</artifactId>
        <version>1.5-SNAPSHOT</version>
    </dependency>

Depending on your setup you need to use a different `scope` (for example test
or provided to prevent the validator to be deployed to production).

Add the following lines to your `Application`'s init method:

    @Override
    protected void init() {
        super.init();
        getMarkupSettings().setStripWicketTags(true);
        getRequestCycleSettings().addResponseFilter(new HtmlValidationResponseFilter());
    }

You might want to put a check for the configuration of your application 
around the addition of the response filter, to ensure that the filter doesn't run
in production mode:

    if (RuntimeConfigurationType.DEVELOPMENT == getConfigurationType()) {
        getRequestCycleSettings().addResponseFilter(new HtmlValidationResponseFilter());
    }

License
-------

This project is distributed using the Apache 2 License (see LICENSE for more details
on other included software).

Build
-----

Building Wicket.Validator is not difficult: you need to have Java 6 and Maven 3 installed, 
and know how to checkout sources from github. Assuming you know how to use Git, you need 
to do the following on a command prompt:

    $ git clone git://github.com/dashorst/wicket-stuff-markup-validator.git
    $ cd wicket-stuff-markup-validator
    $ mvn install

Then you can add the Wicket.Validator dependency to your own application using Maven.


Wicket Stuff HTML5 Validator
============================

This project includes the HTML validator taken from [html
validator](http://validator.nu) and uses this to validate generated Wicket
markup while running your application (or running WicketTester tests).

In order to make this work, we had to provide jar files of several open source
projects required by the HTML validator code:

* [isorelax](http://sourceforge.net/projects/iso-relax) (taken from
  http://sourceforge.net/projects/iso-relax, release 20041111, MIT License)

* [jing](http://code.google.com/p/jing-trang) (taken from
  http://code.google.com/p/jing-trang, release 20091111, BSD License)

Since these recent releases are not available from the central Maven
repository, we included the sources and provide the jar files in our own
namespace.

Usage
-----

Make sure you use Wicket 1.5. This validator does not run on Wicket 1.4 or 
earlier.

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


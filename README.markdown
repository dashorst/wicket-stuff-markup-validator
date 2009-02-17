Wicket Stuff HTML Validator
===========================

This validator filter is based upon the work of the validator filter created by 
the Tuckey developers. This validator only validates XHTML based on the XHTML
DTDs. This package is licensed under the Gnu Public License.

Usage
-----

You can use it by adding the HtmlValidationResponseFilter to the Wicket 
request cycle filters in the following fashion:

	public class MyApplication extends WebApplication {
	    // ...
    
	    @Override
	    protected void init() {
	        // only enable the markup filter in DEVELOPMENT mode
	        if(DEVELOPMENT.equals(getConfigurationType())) {
	            getRequestCycleSettings()
	                .addResponseFilter(new HtmlValidationResponseFilter());
	        }
	    }
	}

And you're all set. Make sure you define a XHTML doctype in your pages, such
as:

	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	    <title>Foo</title>
	</head>
	<body>
	</body>
	</html>

Building
--------
This project requires Apache Maven (>= 2.0.9) to be built. Just perform a
mvn install and include the following in your project's pom:

	<dependency>
	    <groupId>org.wicketstuff</groupId>
	    <artifactId>htmlvalidator</artifactId>
	    <version>1.0</version>
	    <scope>test</scope>
	</dependency>

If you wish to deploy the markup filter, you should change the scope to compile.

Examples
--------
For examples, start the embedded Jetty server found in src/test/java

  
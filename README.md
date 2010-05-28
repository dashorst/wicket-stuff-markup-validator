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

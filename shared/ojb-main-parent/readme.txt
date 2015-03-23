
project intended to contain a mvn parent pom that can be the parent project of
ojb projects.

Inheritance will allow plugins in the parent pom to be inherited
by child projects without having to duplicate the plugin declaration in child
projects.

This parent pom is not also an aggregate pom.  This parent project is not used
to build child projects.  It is rather only used to provide inheritance.

This project uses Java 8, Karaf 3.0.4 and Camel 2.15.3 and CXF 3.0.6.

Here are the karaf commands to run:

feature:repo-add camel 2.15.3

feature:install cxf
feature:install camel
feature:install camel-cxf
feature:install camel-http4
feature:install camel-mail
feature:install cxf-ws-security 

install -s mvn:org.apache.servicemix.bundles/org.apache.servicemix.bundles.commons-lang
install -s mvn:org.apache.servicemix.bundles/org.apache.servicemix.bundles.commons-io

install -s mvn:org.ojbc.bundles.shared/ojb-resources-common
install -s mvn:org.ojbc.bundles.intermediaries/ndex-submission-service-intermediary
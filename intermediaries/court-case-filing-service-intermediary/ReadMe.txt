This project uses Java 8

Here are the karaf commands to run:

install -s mvn:org.apache.servicemix.bundles/org.apache.servicemix.bundles.commons-lang
install -s mvn:org.apache.servicemix.bundles/org.apache.servicemix.bundles.commons-io
install -s mvn:org.apache.httpcomponents/httpcore-osgi/4.2.5
install -s mvn:org.apache.httpcomponents/httpclient-osgi/4.2.5

install -s mvn:commons-pool/commons-pool
install -s mvn:org.apache.commons/com.springsource.org.apache.commons.dbcp/1.2.2.osgi
install -s mvn:org.osgi/org.osgi.enterprise/4.2.0
install -s mvn:com.h2database/h2/1.3.174
install -s mvn:com.mysql.jdbc/com.springsource.com.mysql.jdbc

feature:repo-add camel 2.14.3
feature:install camel-saxon
feature:install camel-cxf

KARAF WILL STOP INSTALLING THE BUNDLES AFTER THE COMMAND, JUST COPY / PASTE RUN WHAT IS BELOW

feature:install cxf-ws-security 
feature:install camel-mail

feature:install spring-jdbc/3.2.11.RELEASE_1

install -s mvn:org.ojbc.bundles.shared/ojb-resources-common
install -s mvn:org.ojbc.bundles.shared/ojb-common
install -s mvn:org.ojbc.bundles.shared/ojb-camel-common

install -s mvn:org.ojbc.bundles.intermediaries/court-case-filing-service-intermediary


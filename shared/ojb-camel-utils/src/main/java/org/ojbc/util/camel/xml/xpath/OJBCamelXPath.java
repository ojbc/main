package org.ojbc.util.camel.xml.xpath;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.w3c.dom.NodeList;

import org.apache.camel.component.bean.XPathAnnotationExpressionFactory;
import org.apache.camel.language.LanguageAnnotation;
import org.apache.camel.language.NamespacePrefix;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@LanguageAnnotation(language = "xpath", factory = XPathAnnotationExpressionFactory.class)
public @interface OJBCamelXPath {
    String value();

    // You can add the namespaces as the default value of the annotation
    NamespacePrefix[] namespaces() default {
    @NamespacePrefix(prefix = "nc20", uri = "http://niem.gov/niem/niem-core/2.0"),
    @NamespacePrefix(prefix = "psre", uri = "http://hijis.hawaii.gov/PersonSearchRequestExtension/1.0"),
    @NamespacePrefix(prefix = "psc", uri = "http://hijis.hawaii.gov/PersonSearchCriteria/1.0"),
    @NamespacePrefix(prefix = "wsaw3c", uri = "http://www.w3.org/2005/08/addressing"),
    @NamespacePrefix(prefix = "merge", uri = "http://nij.gov/IEPD/Exchange/EntityMergeRequestMessage/1.0"),
    @NamespacePrefix(prefix = "cjis", uri = "http://cjisservicesession3.stateless.session.ejb.obts.isdi.net"),
    @NamespacePrefix(prefix = "jxdm40", uri = "http://niem.gov/niem/domains/jxdm/4.0"),
    @NamespacePrefix(prefix = "jxdm41", uri = "http://niem.gov/niem/domains/jxdm/4.1"),
    @NamespacePrefix(prefix = "lexsdigest", uri = "http://usdoj.gov/leisp/lexs/digest/3.1"),
    @NamespacePrefix(prefix = "lexs", uri = "http://usdoj.gov/leisp/lexs/3.1"),
    @NamespacePrefix(prefix = "ndexia", uri = "http://fbi.gov/cjis/N-DEx/IncidentArrest/2.1"),
    @NamespacePrefix(prefix = "maine", uri = "http://www.maine.gov/dps/msp/MaineIncidentReportNDEx"),
    @NamespacePrefix(prefix = "wsnb2", uri = "http://docs.oasis-open.org/wsn/b-2"),
    @NamespacePrefix(prefix = "gjxdm303", uri = "http://www.it.ojp.gov/jxdm/3.0.3"),
    @NamespacePrefix(prefix = "s", uri = "http://niem.gov/niem/structures/2.0"),
    @NamespacePrefix(prefix = "smext", uri = "http://ojbc.org/IEPD/Extensions/Subscription/1.0"),
    @NamespacePrefix(prefix = "sm", uri = "http://ojbc.org/IEPD/Exchange/SubscriptionMessage/1.0"),
    @NamespacePrefix(prefix = "um", uri = "http://ojbc.org/IEPD/Exchange/UnsubscriptionMessage/1.0"),
    @NamespacePrefix(prefix = "arrestRprt", uri = "http://hijis.hawaii.gov/ArrestReport/1.0"),
    @NamespacePrefix(prefix = "bookingRprtExt", uri = "http://hijis.hawaii.gov/BookingReportExtension/1.0"),
    @NamespacePrefix(prefix = "vehext", uri = "http://ojbc.org/IEPD/Extensions/VehicleSearchRequest/1.0")
    };

    Class<?> resultType() default NodeList.class;
}
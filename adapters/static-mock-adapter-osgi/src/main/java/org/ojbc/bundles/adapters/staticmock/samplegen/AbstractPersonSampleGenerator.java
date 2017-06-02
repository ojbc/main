/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.bundles.adapters.staticmock.samplegen;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.w3c.dom.Document;

/**
 * The generic superclass of sample generators that are person-based.  That is, each sample instance represents a "record" for one person, like
 * a criminal history or a warrant.
 * 
 */
public abstract class AbstractPersonSampleGenerator extends AbstractSampleGenerator {

    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog(AbstractPersonSampleGenerator.class);

    protected AbstractPersonSampleGenerator() throws ParserConfigurationException, IOException {
        super();
    }

    /**
     * Generate a sample list of XML documents for this particular sample generator
     * 
     * @param recordCount
     *            the number of records to generate
     * @param baseDate
     *            the date to use as the basis for relative dates
     * @return the list of XML document
     * @throws Exception
     */
    public final List<Document> generateSample(int recordCount, DateTime baseDate) throws Exception {
        return generateSample(recordCount, baseDate, null);
    }

    /**
     * Generate a sample list of XML documents for this particular sample generator
     * 
     * @param recordCount
     *            the number of records to generate
     * @param baseDate
     *            the date to use as the basis for relative dates
     * @param state
     *            the state to simulate, or null to use all states
     * @return the list of XML document
     * @throws Exception
     */
    public final List<Document> generateSample(int recordCount, DateTime baseDate, String state) throws Exception {
        return generateSample(loadIdentities(recordCount, baseDate, state), baseDate, state);
    }

    /**
     * Implemented by derived types to generate the sample instances for the particular type of data of interest
     * @param people The randomly generated personal identities to use in the sample
     * @param baseDate the date to use as "today" in generating the samples
     * @param state the state to restrict the sample instances
     * @return a list of randomly generated instances, as DOM Documents
     * @throws Exception if something goes wrong
     */
    protected abstract List<Document> generateSample(Collection<PersonElementWrapper> people, DateTime baseDate, String state) throws Exception;

}

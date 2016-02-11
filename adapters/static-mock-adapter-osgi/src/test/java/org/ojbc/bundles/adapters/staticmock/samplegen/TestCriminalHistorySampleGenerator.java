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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.bundles.adapters.staticmock.samplegen;

import org.ojbc.bundles.adapters.staticmock.samplegen.AbstractPersonSampleGenerator;
import org.ojbc.bundles.adapters.staticmock.samplegen.CriminalHistorySampleGenerator;


/**
 * Test for the criminal history sample generator.
 *
 */
public class TestCriminalHistorySampleGenerator extends AbstractPersonSampleGeneratorTestCase {

    @Override
    protected AbstractPersonSampleGenerator createSampleGenerator() {
        return CriminalHistorySampleGenerator.getInstance();
    }

    @Override
    protected String getRootSchemaFileName() {
        return "Criminal_History.xsd";
    }

    @Override
    protected String getSchemaRootFolderName() {
        return "NIEM_2.1";
    }

    @Override
    protected String getIEPDRootPath() {
        return "ssp/Criminal_History_Query_Results/artifacts/service_model/information_model/Criminal_History-IEPD/xsd/";
    }

}

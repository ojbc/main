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

import java.util.ArrayList;
import java.util.List;

import org.ojbc.bundles.adapters.staticmock.samplegen.AbstractPersonSampleGenerator;
import org.ojbc.bundles.adapters.staticmock.samplegen.FirearmRegistrationSampleGenerator;


public class TestFirearmRegistrationSampleGenerator extends AbstractPersonSampleGeneratorTestCase {
    
    @Override
    protected AbstractPersonSampleGenerator createSampleGenerator() {
        return FirearmRegistrationSampleGenerator.getInstance();
    }

    @Override
    protected String getSchemaRootFolderName() {
        return "Subset/niem";
    }

    @Override
    protected String getIEPDRootPath() {
        return "ssp/Firearm_Registration_Query_Results_Service/artifacts/service_model/information_model/Firearm_Registration_Query_Results_IEPD/xsd/";
    }

    @Override
    protected String getRootSchemaFileName() {
        return "exchange_schema.xsd";
    }
    
    @Override
    protected List<String> getAdditionalSchemaRelativePaths() {
        List<String> schemaPaths = new ArrayList<String>();
        schemaPaths.add(getIEPDRootPath() + "impl/demostate/demostate-firearm-codes.xsd");
    	return schemaPaths;
    }

}

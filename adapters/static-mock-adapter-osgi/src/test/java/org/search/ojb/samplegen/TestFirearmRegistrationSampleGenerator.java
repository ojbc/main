package org.search.ojb.samplegen;


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
        return "service-specifications/Firearm_Registration_Query_Results_Service/artifacts/service_model/information_model/Firearm_Registration_Query_Results_IEPD/xsd/";
    }

    @Override
    protected String getRootSchemaFileName() {
        return "exchange_schema.xsd";
    }

}

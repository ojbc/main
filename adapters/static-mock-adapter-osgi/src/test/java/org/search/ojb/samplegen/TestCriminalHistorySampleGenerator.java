package org.search.ojb.samplegen;


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
        return "NIEM_2.0";
    }

    @Override
    protected String getIEPDRootPath() {
        return "service-specifications/Person_Query_Results_Handler_Service_-_Criminal_History/artifacts/service_model/information_model/Criminal_History-IEPD/xsd/";
    }

}

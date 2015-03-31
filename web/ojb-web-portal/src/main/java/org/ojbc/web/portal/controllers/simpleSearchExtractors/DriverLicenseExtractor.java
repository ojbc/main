package org.ojbc.web.portal.controllers.simpleSearchExtractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ojbc.web.model.person.search.PersonSearchRequest;

public class DriverLicenseExtractor extends SearchTermExtractorBase {

    private Pattern pattern;
    private String defaultStateOfIssue;
    
    public DriverLicenseExtractor() {
    }
    
    public void setDriversLicenseRegex(String driversLicenseRegex)
    {
        pattern = Pattern.compile(driversLicenseRegex);
    }
    
    public void setDefaultStateOfIssue(String defaultStateOfIssue)
    {
        this.defaultStateOfIssue = defaultStateOfIssue;
    }

    @Override
    protected boolean extractTermLocal(String token, PersonSearchRequest personSearchRequest) {
        Matcher matcher = pattern.matcher(token);
        if (matcher.matches()) {
            if (matcher.groupCount() != 3) {
                throw new IllegalStateException("Drivers License Extractors must have three regex groups");
            }
            if (matcher.group(1) != null) {
                personSearchRequest.setPersonDriversLicenseIssuer(matcher.group(1).substring(0, 2));
                personSearchRequest.setPersonDriversLicenseNumber(matcher.group(2));
            } else {
                personSearchRequest.setPersonDriversLicenseIssuer(defaultStateOfIssue);
                personSearchRequest.setPersonDriversLicenseNumber(token);
            }
            return true;
        }
        return false;

    }

}

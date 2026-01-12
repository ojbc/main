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
package org.ojbc.adapters.analyticaldatastore.processor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticaldatastore.dao.AnalyticalDatastoreDAO;
import org.ojbc.adapters.analyticaldatastore.dao.model.Incident;
import org.springframework.beans.factory.annotation.Autowired;

public class IncidentArrestAnalyticsVerificationProcessor {
    
    @Autowired
    private AnalyticalDatastoreDAO analyticsDao;
    
    private static final Log log = LogFactory.getLog(IncidentArrestAnalyticsVerificationProcessor.class);
    
    public boolean verifyIncidentDatabaseUdpate() {
        
        Incident latestIncident = analyticsDao.returnLatestIncident();
        
        if(latestIncident != null) {
            log.info("Latest Incident: " + latestIncident.toString());
            
            Date latestIncidentDate = latestIncident.getIncidentDate();
            
            log.info("Latest Incident Date: " + latestIncident.getIncidentDate());
            
            LocalDate convertedIncidentDate;
            
            if (latestIncidentDate instanceof java.sql.Date) {
                 convertedIncidentDate = ((java.sql.Date) latestIncidentDate).toLocalDate();
            } else {
                 convertedIncidentDate = latestIncidentDate.toInstant()
                             .atZone(ZoneId.systemDefault())
                             .toLocalDate();
            }
            
            if(ChronoUnit.DAYS.between(convertedIncidentDate, LocalDate.now()) > 1) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }
}

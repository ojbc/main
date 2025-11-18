package org.ojbc.adapters.analyticaldatastore.processor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.ojbc.adapters.analyticaldatastore.dao.AnalyticalDatastoreDAOImpl;
import org.ojbc.adapters.analyticaldatastore.dao.model.Incident;

public class IncidentArrestAnalyticsVerificationProcessor {
    
    private AnalyticalDatastoreDAOImpl analyticsDao;
    
    public boolean verifyIncidentDatabaseUdpate() {
        
        Incident latestIncident = analyticsDao.returnLatestIncident();
        
        if(latestIncident != null) {
            Date latestIncidentDate = latestIncident.getIncidentDate();
            
            LocalDate convertedIncidentDate = latestIncidentDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            
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

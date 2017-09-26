package org.ojbc.audit.enhanced.dao;

import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;


public interface EnhancedAuditDAO {

	public Integer saveFederalRapbackSubscription(FederalRapbackSubscription federalRapbackSubscription);
	
	public void updateFederalRapbackSubscriptionWithResponse(FederalRapbackSubscription federalRapbackSubscription) throws Exception;
	
	public FederalRapbackSubscription retrieveFederalRapbackSubscriptionFromTCN(String transactionControlNumber);
}

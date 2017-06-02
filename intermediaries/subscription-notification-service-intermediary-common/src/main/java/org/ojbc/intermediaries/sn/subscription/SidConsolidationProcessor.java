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
package org.ojbc.intermediaries.sn.subscription;

import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;

public class SidConsolidationProcessor {

    private static final Log log = LogFactory.getLog(SidConsolidationProcessor.class);

    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;

    /**
     * Main behavior method, invoked from the Camel route, to replace the current SID to the new SID. 
     * @param currentSid
     * @param newSid
     * @throws Exception
     */
    public void consolidateSid(@Header("currentSid") String currentSid, @Header("newSid") String newSid) throws Exception {
    	log.info("currentSid:" + StringUtils.trimToEmpty(currentSid));
    	log.info("newSid:" + StringUtils.trimToEmpty(newSid));
    	if (StringUtils.isBlank(currentSid) || StringUtils.isBlank(newSid)){
    		log.error("the currentSid and newSid are both required to do the SID consolidation");
    		throw new IllegalArgumentException("the currentSid and newSid are both required to do the SID consolidation"); 
    	}
        subscriptionSearchQueryDAO.consolidateSid(currentSid, newSid);
    }

	public SubscriptionSearchQueryDAO getSubscriptionSearchQueryDAO() {
		return subscriptionSearchQueryDAO;
	}

	public void setSubscriptionSearchQueryDAO(
			SubscriptionSearchQueryDAO subscriptionSearchQueryDAO) {
		this.subscriptionSearchQueryDAO = subscriptionSearchQueryDAO;
	}

}

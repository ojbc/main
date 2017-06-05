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
package org.ojbc.adapters.rapbackdatastore.processor;

import javax.annotation.Resource;

import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.springframework.stereotype.Service;

@Service
public class CriminalHistoryConsolidationProcessor {

    private static final Log log = LogFactory.getLog(CriminalHistoryConsolidationProcessor.class);

    @Resource
    private RapbackDAO rapbackDAO;

    /**
     * Main behavior method, invoked from the Camel route, to replace the current SID with the new SID.
     * and replace the current UCN to the new UCN. 
     * @param currentSid
     * @param newSid
     * @param currentUcn
     * @param newUcn
     * @throws Exception
     */
    public void consolidateCriminalHistory(@Header("currentSid") String currentSid, 
    		@Header("newSid") String newSid, 
    		@Header("currentUcn") String currentUcn, 
    		@Header("newUcn") String newUcn) throws Exception {
    	if (StringUtils.isNotBlank(currentSid) && StringUtils.isNotBlank(newSid)){
    		log.info("Consolidating SID " + currentSid + " with " + newSid);
    		rapbackDAO.consolidateSid(currentSid, newSid);
    	}
    	
    	if (StringUtils.isNotBlank(currentUcn) && StringUtils.isNotBlank(newUcn)){
    		log.info("Consolidating UCN " + currentUcn + " with " + newUcn);
    		rapbackDAO.consolidateUcn(currentUcn, newUcn);
    	}
    }

}

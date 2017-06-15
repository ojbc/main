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
package org.ojbc.bundles.adapters.fbi.ebts.processor;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class RapbackEventTextProcessor {

	private Logger log = Logger.getLogger(RapbackEventTextProcessor.class);
	
	private static final String UCN_CONSOLIDATION = "RB009";
	private static final String UCN_DELETION = "RB010";
	private static final String UCN_RESTORATION = "RB017";
	
	public Exchange processRapbackEventText(@Body Document rapbackNotification, Exchange ex) throws Exception
	{
		String rapbackEventText = XmlUtils.xPathStringSearch(rapbackNotification, "/nistbio:NISTBiometricInformationExchangePackage/nistbio:PackageDescriptiveTextRecord/nistbio:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ebts:RecordRapBackData/ebts:TransactionRapBackTriggeringEvent/ebts:RapBackEventText");
		
		//RB009 - A Consolidation of NGI Identities has resulted in the information for deleted identity (ies) (UCN(s) XXXXXXXXX being consolidated with the retained Identity UCN XXXXXXXXX.
		//RB010 - The Identity associated with UCN:%1 has been deleted from NGI, resulting in the deletion of your Rap Back Subscription ID#:%2
		//RB017 – The Deletion action has been reversed in NGI for UCN XXXXXXXXX.  Rap Back ID# xxxxxx has been restored to its previous state
		
		String deletedIdentity = "";
		String retainedIdentity = "";
		String deletedSubscription ="";
		String restoredIdentity ="";
		
		if(StringUtils.isNotBlank(rapbackEventText))
		{
			if (StringUtils.startsWith(rapbackEventText, UCN_CONSOLIDATION))
			{
				ex.getIn().setHeader("RBN_Action", "UCN_CONSOLIDATION");
				deletedIdentity = StringUtils.substringBetween(rapbackEventText, "UCN(s) ", " being consolidated with");
				retainedIdentity = StringUtils.substringBetween(rapbackEventText, "the retained Identity UCN ", ".");
			}	
			
			if (StringUtils.startsWith(rapbackEventText, UCN_DELETION))
			{
				ex.getIn().setHeader("RBN_Action", "UCN_DELETION");
				deletedIdentity = StringUtils.substringBetween(rapbackEventText, "The Identity associated with UCN:", " has been deleted from NGI");
				deletedSubscription = StringUtils.substringAfter(rapbackEventText, "Rap Back Subscription ID#:");
			}	

			if (StringUtils.startsWith(rapbackEventText, UCN_RESTORATION))
			{
				ex.getIn().setHeader("RBN_Action", "UCN_RESTORATION");
				restoredIdentity = StringUtils.substringBetween(rapbackEventText, "been reversed in NGI for UCN ", ".");
				deletedSubscription = StringUtils.substringBetween(rapbackEventText, "Rap Back ID# ", " has been restored to its previous state");
			}	

		}	
		
		log.info("DeletedIdentity: " + deletedIdentity);
		log.info("retainedIdentity " + retainedIdentity);
		log.info("deletedSubscription " + deletedSubscription);
		log.info("restoredIdentity " + restoredIdentity);
		
		ex.getIn().setHeader("deletedIdentity",deletedIdentity);
		ex.getIn().setHeader("retainedIdentity",retainedIdentity);
		ex.getIn().setHeader("restoredIdentity",restoredIdentity);
		ex.getIn().setHeader("deletedSubscription",deletedSubscription);
		
		return ex;
	}
	
}

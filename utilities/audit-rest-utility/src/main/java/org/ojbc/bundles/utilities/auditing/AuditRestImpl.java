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
package org.ojbc.bundles.utilities.auditing;

import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackNotification;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscriptionDetail;
import org.ojbc.audit.enhanced.dao.model.PrintResults;
import org.ojbc.audit.enhanced.dao.model.QueryRequestByDateRange;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.util.model.rapback.AgencyProfile;
import org.ojbc.util.model.rapback.ExpiringSubscriptionRequest;
import org.ojbc.util.model.rapback.Subscription;
import org.springframework.stereotype.Service;

@Service
public class AuditRestImpl implements AuditInterface {

	private static final Log log = LogFactory.getLog(AuditRestImpl.class);
	
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
	
	@Resource
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
	
	private static final String LOGIN_ACTION="login";
	
	private static final String LOGOUT_ACTION="logout";
	
	@Override
	public Response auditPrintResults(PrintResults printResults) {

		log.info("Print Results: " + printResults.toString());
		
		enhancedAuditDao.savePrintResults(printResults);
		
		return Response.status(Status.OK).entity(printResults).build();
	}

	@Override
	public Response auditUserLogin(UserInfo userInfo) {
		log.info("Audit user login info: " + userInfo.toString());
		
		Integer userInfoPk = enhancedAuditDao.saveUserInfo(userInfo);
		
		enhancedAuditDao.saveUserAuthentication(userInfoPk, LOGIN_ACTION);
		
		return Response.status(Status.OK).entity(userInfo).build();
	}

	@Override
	public Response auditUserLogout(UserInfo userInfo) {
		log.info("Audit user logout info: " + userInfo.toString());
		
		Integer userInfoPk = enhancedAuditDao.saveUserInfo(userInfo);
		
		enhancedAuditDao.saveUserAuthentication(userInfoPk, LOGOUT_ACTION);
		
		return Response.status(Status.OK).entity(userInfo).build();
	}

	@Override
	public List<FederalRapbackSubscription> searchForFederalRapbackSubscriptions(String subscriptionId) {
		log.info("Subscription ID: " + subscriptionId);
		
		List<FederalRapbackSubscription> federalRapbackSubscriptions = enhancedAuditDao.retrieveFederalRapbackSubscriptionFromStateSubscriptionId(subscriptionId);
		
		return federalRapbackSubscriptions;
	}

	@Override
	public List<Subscription> retrieveExpiringSubscriptions(ExpiringSubscriptionRequest request) {
		
		log.info("Days until expiration: " + request.getDaysUntilExpiry());
		log.info("ORIs: " + request.getOris());
		
		List<Subscription> subscriptions = subscriptionSearchQueryDAO.searchForExpiringAndInvalidSubscriptions(request.getOris(), request.getDaysUntilExpiry(), request.getSystemName());
		
		return subscriptions;
	}

	@Override
	public List<Subscription> retrieveExpiredSubscriptions(
			ExpiringSubscriptionRequest request) {
		log.info("Days until expiration: " + request.getDaysUntilExpiry());
		log.info("ORIs: " + request.getOris());
		
		List<Subscription> subscriptions = subscriptionSearchQueryDAO.searchForExpiredAndInvalidSubscriptions(request.getOris(), request.getDaysUntilExpiry(), request.getSystemName());
		
		return subscriptions;
	}

	@Override
	public List<AgencyProfile> retrieveAllAgencies() {
		
		List<AgencyProfile> agencyProfiles = subscriptionSearchQueryDAO.returnAllAgencies();
		
		
		return agencyProfiles;
	}

	@Override
	public List<FederalRapbackNotification> retrieveRapbackNotifications(QueryRequestByDateRange queryRequestByDateRange) {
		List<FederalRapbackNotification> federalRapbackNotifications = enhancedAuditDao.retrieveFederalNotifications(queryRequestByDateRange.getStartDate(), queryRequestByDateRange.getEndDate());
		return federalRapbackNotifications;
	}

	@Override
	public List<FederalRapbackNotification> searchForFederalRapbackNotifications(
			String subscriptionId) {
		List<FederalRapbackNotification> federalRapbackNotifications = enhancedAuditDao.retrieveFederalNotificationsBySubscriptionId(subscriptionId);
		return federalRapbackNotifications;
	}

	@Override
	public List<FederalRapbackSubscription> retrieveFederalRapbackSubscriptionErrors() {
		log.info("Retrieve Federal Subscription Errors.");
		
		List<FederalRapbackSubscription> federalRapbackSubscriptions = enhancedAuditDao.retrieveFederalRapbackSubscriptionErrors();
		
		return federalRapbackSubscriptions;	
	}

	@Override
	public FederalRapbackSubscriptionDetail returnFederalRapbackSubscriptionDetail(
			String subscriptionId) {

		FederalRapbackSubscriptionDetail federalRapbackSubscriptionDetail = new FederalRapbackSubscriptionDetail();
		
		//Return all federal subscription info for the state subscription
		List<FederalRapbackSubscription> federalRapbackSubscriptions = enhancedAuditDao.retrieveFederalRapbackSubscriptionFromStateSubscriptionId(subscriptionId);
	
		federalRapbackSubscriptionDetail.setFederalRapbackSubscriptions(federalRapbackSubscriptions);
		
		boolean latestMaintenanceRequestFound = false;
		
		//Results come back sorted in descending order by timestamp
		for (FederalRapbackSubscription federalRapbackSubscription : federalRapbackSubscriptions)
		{
			if (federalRapbackSubscription.getTransactionCategoryCodeRequest().equals("RBSCRM"))
			{
				federalRapbackSubscriptionDetail.setFbiSubscriptionSent(true);
				
				if (StringUtils.isNotBlank(federalRapbackSubscription.getTransactionCategoryCodeResponse()) && federalRapbackSubscription.getTransactionCategoryCodeResponse().equals("RBSR") && StringUtils.isNotBlank(federalRapbackSubscription.getFbiSubscriptionId()))
				{
					federalRapbackSubscriptionDetail.setFbiSubscriptionCreated(true);
				}	
				
				if (StringUtils.isNotBlank(federalRapbackSubscription.getTransactionStatusText()))
				{	
					federalRapbackSubscriptionDetail.setFbiSubscriptionErrorText(federalRapbackSubscription.getTransactionStatusText());
				}	
			}	
			
			if (!latestMaintenanceRequestFound)
			{
				if (federalRapbackSubscription.getTransactionCategoryCodeRequest().equals("RBMNT"))
				{
					federalRapbackSubscriptionDetail.setFbiRapbackMaintenanceSent(true);
					
					if (StringUtils.isNotBlank(federalRapbackSubscription.getTransactionCategoryCodeResponse()) && federalRapbackSubscription.getTransactionCategoryCodeResponse().equals("RBMNTR") && StringUtils.isNotBlank(federalRapbackSubscription.getFbiSubscriptionId()))
					{
						federalRapbackSubscriptionDetail.setFbiRapbackMaintenanceConfirmed(true);
					}	
					
					if (StringUtils.isNotBlank(federalRapbackSubscription.getTransactionStatusText()))
					{	
						federalRapbackSubscriptionDetail.setFbiRapbackMaintenanceErrorText(federalRapbackSubscription.getTransactionStatusText());
					}	
					
					latestMaintenanceRequestFound = true;
				}	
				
			}	
		}	
		
		return federalRapbackSubscriptionDetail;
	}
}
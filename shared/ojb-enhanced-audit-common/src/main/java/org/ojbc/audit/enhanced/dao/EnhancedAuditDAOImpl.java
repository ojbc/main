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
package org.ojbc.audit.enhanced.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.CrashVehicle;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackIdentityHistory;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackNotification;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackRenewalNotification;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
import org.ojbc.audit.enhanced.dao.model.FirearmSearchResult;
import org.ojbc.audit.enhanced.dao.model.FirearmsQueryResponse;
import org.ojbc.audit.enhanced.dao.model.FirearmsSearchRequest;
import org.ojbc.audit.enhanced.dao.model.IdentificationQueryResponse;
import org.ojbc.audit.enhanced.dao.model.IdentificationSearchReasonCodes;
import org.ojbc.audit.enhanced.dao.model.IdentificationSearchRequest;
import org.ojbc.audit.enhanced.dao.model.IdentificationSearchResult;
import org.ojbc.audit.enhanced.dao.model.IncidentSearchRequest;
import org.ojbc.audit.enhanced.dao.model.NotificationSent;
import org.ojbc.audit.enhanced.dao.model.PersonQueryCriminalHistoryResponse;
import org.ojbc.audit.enhanced.dao.model.PersonQueryWarrantResponse;
import org.ojbc.audit.enhanced.dao.model.PersonSearchRequest;
import org.ojbc.audit.enhanced.dao.model.PersonSearchResult;
import org.ojbc.audit.enhanced.dao.model.PrintResults;
import org.ojbc.audit.enhanced.dao.model.QueryRequest;
import org.ojbc.audit.enhanced.dao.model.SearchQualifierCodes;
import org.ojbc.audit.enhanced.dao.model.SubscriptionAction;
import org.ojbc.audit.enhanced.dao.model.SubscriptionQueryResponse;
import org.ojbc.audit.enhanced.dao.model.SubscriptionReasonCode;
import org.ojbc.audit.enhanced.dao.model.SubscriptionSearchResult;
import org.ojbc.audit.enhanced.dao.model.SystemsToSearch;
import org.ojbc.audit.enhanced.dao.model.TriggeringEvents;
import org.ojbc.audit.enhanced.dao.model.UserAcknowledgement;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.ojbc.audit.enhanced.dao.model.VehicleCrashQueryResponse;
import org.ojbc.audit.enhanced.dao.model.VehicleSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.AuditSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchResponse;
import org.ojbc.audit.enhanced.dao.rowmappers.auditsearch.FirearmSearchRequestRowMapper;
import org.ojbc.audit.enhanced.dao.rowmappers.auditsearch.IncidentSearchRequestRowMapper;
import org.ojbc.audit.enhanced.dao.rowmappers.auditsearch.PersonSearchRequestRowMapper;
import org.ojbc.audit.enhanced.dao.rowmappers.auditsearch.UserAuthenticationResposeRowMapper;
import org.ojbc.audit.enhanced.dao.rowmappers.auditsearch.VehicleSearchRequestRowMapper;
import org.ojbc.util.helper.DaoUtils;
import org.ojbc.util.sn.SubscriptionSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class EnhancedAuditDAOImpl implements EnhancedAuditDAO {

    @Autowired
	private JdbcTemplate jdbcTemplate;
    
    @Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
	private Map<String, Integer> searchQualifierCodes= new HashMap<String, Integer>();
	
	private Map<String, Integer> systemsToSearchByURI= new HashMap<String, Integer>();
	
	private Map<String, Integer> systemsToSearchByName= new HashMap<String, Integer>();
	
	private Map<String, Integer> subscriptionReasonCodes= new HashMap<String, Integer>();
	
	private Map<String, Integer> identificationReasonCodes= new HashMap<String, Integer>();
	
    private final Log log = LogFactory.getLog(this.getClass());

	@Override
	public Integer retrieveSystemToSearchIDFromURI(String uri) {
		
		if (systemsToSearchByURI.isEmpty())
		{	
			final String SELECT_STATEMENT="SELECT * from SYSTEMS_TO_SEARCH";
	
			List<SystemsToSearch> systemsToSearchlist = jdbcTemplate.query(SELECT_STATEMENT, new SystemsToSearchRowMapper());
			
			for (SystemsToSearch systemToSearch : systemsToSearchlist)
			{
				systemsToSearchByURI.put(systemToSearch.getSystemUri(),systemToSearch.getSystemsToSearchId());
			}	
		}	
		
		Integer id = systemsToSearchByURI.get(uri);
		
		return id;
	}

	@Override
	public Integer retrieveSubReasonCodeIdFromCode(String subscriptionReasonCodeString) {
		if (systemsToSearchByName.isEmpty())
		{	
			final String SELECT_STATEMENT="SELECT * from SUBSCRIPTION_REASON_CODE";
	
			List<SubscriptionReasonCode> subscriptionReasonCodeList = jdbcTemplate.query(SELECT_STATEMENT, new SubscriptionReasonCodeRowMapper());
			
			for (SubscriptionReasonCode subscriptionReasonCode : subscriptionReasonCodeList)
			{
				subscriptionReasonCodes.put(subscriptionReasonCode.getSubscritionReasonCode(), subscriptionReasonCode.getSubscriptionReasonCodeId());
			}	
		}	
		
		Integer id = subscriptionReasonCodes.get(subscriptionReasonCodeString);
		
		return id;	
	}
	
	@Override
	public Integer retrieveSystemToSearchIDFromSystemName(String systemName) {
		
		if (systemsToSearchByName.isEmpty())
		{	
			final String SELECT_STATEMENT="SELECT * from SYSTEMS_TO_SEARCH";
	
			List<SystemsToSearch> systemsToSearchlist = jdbcTemplate.query(SELECT_STATEMENT, new SystemsToSearchRowMapper());
			
			for (SystemsToSearch systemToSearch : systemsToSearchlist)
			{
				systemsToSearchByName.put(systemToSearch.getSystemName(),systemToSearch.getSystemsToSearchId());
			}	
		}	
		
		Integer id = systemsToSearchByName.get(systemName);
		
		return id;
	}
	
	@Override
	public Integer retrieveIdentificationReasonCodeFromDescription(
			String description) {
		if (identificationReasonCodes.isEmpty())
		{	
			final String SELECT_STATEMENT="SELECT * from IDENTIFICATION_SEARCH_REASON_CODE";
	
			List<IdentificationSearchReasonCodes> identificationSearchReasonCodes = jdbcTemplate.query(SELECT_STATEMENT, new IdentificationSearchReasonCodeRowMapper());
			
			for (IdentificationSearchReasonCodes identificationSearchReasonCode : identificationSearchReasonCodes)
			{
				identificationReasonCodes.put(identificationSearchReasonCode.getIdentificationReasonCodeDescription(),identificationSearchReasonCode.getIdentificationSearchReasonCodeId());
			}	
		}	
		
		Integer id = identificationReasonCodes.get(description);
		
		return id;
	}

	@Override
	public Integer saveIdentificationReasonCode(
			Integer identificationSearchReasonCodeId,
			Integer identificationSearchRequestId) {
		
        log.debug("Inserting rows into IDENTIFICATION_REASON_CODE_JOINER table");
        
        final String IDENTIFICATION_REASON_CODE_JOINER_INSERT="INSERT into IDENTIFICATION_REASON_CODE_JOINER "
        		+ "(IDENTIFICATION_SEARCH_REASON_CODE_ID, IDENTIFICATION_SEARCH_REQUEST_ID) "
        		+ "values (?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(IDENTIFICATION_REASON_CODE_JOINER_INSERT, new String[] {"IDENTIFICATION_REASON_CODE_JOINER_ID"});
        	            DaoUtils.setPreparedStatementVariable(identificationSearchReasonCodeId, ps, 1);
        	            DaoUtils.setPreparedStatementVariable(identificationSearchRequestId, ps, 2);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	        

	}

	@Override
	public Integer retrieveSearchQualifierCodeIDfromCodeName(String codeName) {
		
		final String SELECT_STATEMENT="SELECT * from SEARCH_QUALIFIER_CODES";

		if (searchQualifierCodes.isEmpty())
		{	
			List<SearchQualifierCodes> searchQualifierCodesList = jdbcTemplate.query(SELECT_STATEMENT, new SearchQualifierCodesRowMapper());
			
			for (SearchQualifierCodes searchQualifierCode : searchQualifierCodesList)
			{
				searchQualifierCodes.put(searchQualifierCode.getCodeName(),searchQualifierCode.getSearchQualifierCodesId());
			}
		}	
			
		Integer id = searchQualifierCodes.get(codeName);
		
		log.debug("Code Name: " + codeName + ", qualifier code id: " + id);
		
		return id;
	}
	

	@Override
	public PrintResults retrievePrintResultsfromMessageID(String messageId) {
		
		final String PRINT_RESULTS_SELECT="SELECT ui.*, pr.* FROM PRINT_RESULTS pr, USER_INFO ui WHERE ui.USER_INFO_ID = pr.USER_INFO_ID and MESSAGE_ID = ?";
		
		List<PrintResults> printResults = jdbcTemplate.query(PRINT_RESULTS_SELECT, new PrintResultsRowMapper(), messageId);
		return DataAccessUtils.singleResult(printResults);		
	}

	@Override
	public Integer saveFederalRapbackSubscription(
			FederalRapbackSubscription federalRapbackSubscription) {
        log.debug("Inserting row into FEDERAL_RAPBACK_SUBSCRIPTION table : " + federalRapbackSubscription);
        
        final String FEDERAL_RAPBACK_SUBSCRIPTION_INSERT="INSERT into FEDERAL_RAPBACK_SUBSCRIPTION "
        		+ "(REQUEST_SENT_TIMESTAMP, TRANSACTION_CONTROL_REFERENCE_IDENTIFICATION, PATH_TO_REQUEST_FILE,SUBSCRIPTION_CATEGORY_CODE, SID, TRANSACTION_STATUS_TEXT, STATE_SUBSCRIPTION_ID, TRANSACTION_CATEGORY_CODE_REQUEST) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
        

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(FEDERAL_RAPBACK_SUBSCRIPTION_INSERT, new String[] {"FEDERAL_RAPBACK_SUBSCRIPTION_ID"});
        	            DaoUtils.setPreparedStatementVariable(federalRapbackSubscription.getRequestSentTimestamp(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackSubscription.getTransactionControlReferenceIdentification(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackSubscription.getPathToRequestFile(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackSubscription.getSubscriptonCategoryCode(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackSubscription.getSid(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackSubscription.getTransactionStatusText(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackSubscription.getStateSubscriptionId(), ps, 7);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackSubscription.getTransactionCategoryCodeRequest(), ps, 8);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
    }
	
	@Override
	public Integer saveFederalRapbackRenewalNotification(
			FederalRapbackRenewalNotification federalRapbackRenewalNotification) {
		log.debug("Inserting row into FEDERAL_RAPBACK_RENEWAL_NOTIFICATION table : " + federalRapbackRenewalNotification);
		
        final String FEDERAL_RAPBACK_RENEWAL_INSERT="INSERT into FEDERAL_RAPBACK_RENEWAL_NOTIFICATION "
        		+ "(UCN, PERSON_FIRST_NAME, PERSON_MIDDLE_NAME, PERSON_LAST_NAME, PERSON_DOB, RECORD_CONTROLLING_AGENCY, RAPBACK_EXPIRATION_DATE, STATE_SUBSCRIPTION_ID, TRANSACTION_STATUS_TEXT, SID, PATH_TO_NOTIFICATION_FILE, NOTIFICATION_RECIEVED_TIMESTAMP) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(FEDERAL_RAPBACK_RENEWAL_INSERT, new String[] {"FEDERAL_RAPBACK_RENEWAL_NOTIFICATION_ID"});
        	            DaoUtils.setPreparedStatementVariable(federalRapbackRenewalNotification.getUcn(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackRenewalNotification.getPersonFirstName(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackRenewalNotification.getPersonMiddleName(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackRenewalNotification.getPersonLastName(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackRenewalNotification.getPersonDob(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackRenewalNotification.getRecordControllingAgency(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackRenewalNotification.getRapbackExpirationDate(), ps, 7);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackRenewalNotification.getStateSubscriptionId(), ps, 8);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackRenewalNotification.getTransactionStatusText(), ps, 9);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackRenewalNotification.getSid(), ps, 10);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackRenewalNotification.getPathToNotificationFile(), ps, 11);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackRenewalNotification.getNotificationRecievedTimestamp(), ps, 12);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	

	}	
	
	@Override
	public Integer savePrintResults(PrintResults printResults) {
        log.debug("Inserting row into PRINT_RESULTS table : " + printResults.toString());
        
        UserInfo userInfo = printResults.getUserInfo();
        
        Integer userInfoPk = null;
        
		//Look up user info here
		List<UserInfo> userInfoEntries = retrieveUserInfoFromFederationId(userInfo.getFederationId());
		
		if (userInfoEntries != null && userInfoEntries.size() > 0)
		{
			userInfoPk = userInfoEntries.get(0).getUserInfoId();
		}
		else
		{	
			userInfoPk = saveUserInfo(userInfo);
		}
        
        final String PRINT_RESULTS_INSERT="INSERT into PRINT_RESULTS "
        		+ "(SYSTEM_NAME, DESCRIPTION, MESSAGE_ID, SID, USER_INFO_ID) "
        		+ "values (?, ?, ?, ?, ?)";
        

        final Integer userInfoPkFinal = userInfoPk;
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(PRINT_RESULTS_INSERT, new String[] {"PRINT_RESULTS_ID"});
        	            DaoUtils.setPreparedStatementVariable(printResults.getSystemName(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(printResults.getDescription(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(printResults.getMessageId(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(printResults.getSid(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(userInfoPkFinal, ps, 5);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		
    }
	
	@Override
	public Integer saveUserAuthentication(Integer userInfoPk, String action) {
        log.debug("Inserting row into USER_LOGIN table : " + userInfoPk);
        
        final String USER_LOGIN_INSERT="INSERT into USER_LOGIN "
        		+ "(USER_INFO_ID, ACTION) "
        		+ "values (?,?)";
        

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(USER_LOGIN_INSERT, new String[] {"USER_LOGIN_ID"});
        	            DaoUtils.setPreparedStatementVariable(userInfoPk, ps, 1);
        	            DaoUtils.setPreparedStatementVariable(action, ps, 2);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		
	}
	
	
	@Override
	public Integer savePersonSystemToSearch(Integer pearchSearchPk, Integer systemsToSearchPk) {
		
        log.debug("Inserting rows into PERSON_SYSTEMS_TO_SEARCH table : " + systemsToSearchPk.toString());
        
        final String PERSON_SYSTEMS_TO_SEARCH_INSERT="INSERT into PERSON_SYSTEMS_TO_SEARCH "
        		+ "(PERSON_SEARCH_REQUEST_ID, SYSTEMS_TO_SEARCH_ID) "
        		+ "values (?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(PERSON_SYSTEMS_TO_SEARCH_INSERT, new String[] {"USER_INFO_ID"});
        	            DaoUtils.setPreparedStatementVariable(pearchSearchPk, ps, 1);
        	            DaoUtils.setPreparedStatementVariable(systemsToSearchPk, ps, 2);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	        
	}	
	
	@Override
	public Integer saveVehicleSearchRequest(
			VehicleSearchRequest vehicleSearchRequest) {

        log.debug("Inserting row into VEHICLE_SEARCH_REQUEST table : " + vehicleSearchRequest);
        
        final String VEHICLE_SEARCH_INSERT="INSERT into VEHICLE_SEARCH_REQUEST "
        		+ "(MESSAGE_ID, ON_BEHALF_OF, PURPOSE, USER_INFO_ID, COLOR, VIN, PLATE_NUMBER, MAKE, MODEL, YEAR_RANGE_START, YEAR_RANGE_END) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(VEHICLE_SEARCH_INSERT, new String[] {"VEHICLE_SEARCH_REQUEST_ID"});
        	            DaoUtils.setPreparedStatementVariable(vehicleSearchRequest.getMessageId(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(vehicleSearchRequest.getOnBehalfOf(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(vehicleSearchRequest.getPurpose(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(vehicleSearchRequest.getUserInfofk(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(vehicleSearchRequest.getVehicleColor(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(vehicleSearchRequest.getVehicleIdentificationNumber(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(vehicleSearchRequest.getVehicleLicensePlate(), ps, 7);
        	            DaoUtils.setPreparedStatementVariable(vehicleSearchRequest.getVehicleMake(), ps, 8);
        	            DaoUtils.setPreparedStatementVariable(vehicleSearchRequest.getVehicleModel(), ps, 9);
        	            DaoUtils.setPreparedStatementVariable(vehicleSearchRequest.getVehicleYearRangeEnd(), ps, 10);
        	            DaoUtils.setPreparedStatementVariable(vehicleSearchRequest.getVehicleYearRangeStart(), ps, 11);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
		
	}
	
	@Override
	public Integer saveIncidentSearchRequest(
			IncidentSearchRequest incidentSearchRequest) {
        log.debug("Inserting row into INCIDENT_SEARCH_REQUEST table : " + incidentSearchRequest);
        
        final String INCIDENT_SEARCH_INSERT="INSERT into INCIDENT_SEARCH_REQUEST "
        		+ "(MESSAGE_ID, ON_BEHALF_OF, PURPOSE, USER_INFO_ID, CITY_TOWN, INCIDENT_NUMBER, INCIDENT_START_DATE, INCIDENT_END_DATE) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(INCIDENT_SEARCH_INSERT, new String[] {"INCIDENT_SEARCH_REQUEST_ID"});
        	            DaoUtils.setPreparedStatementVariable(incidentSearchRequest.getMessageId(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(incidentSearchRequest.getOnBehalfOf(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(incidentSearchRequest.getPurpose(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(incidentSearchRequest.getUserInfofk(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(incidentSearchRequest.getCityTown(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(incidentSearchRequest.getIncidentNumber(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(incidentSearchRequest.getStartDate(), ps, 7);
        	            DaoUtils.setPreparedStatementVariable(incidentSearchRequest.getEndDate(), ps, 8);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		
    }	
	
	@Override
	public Integer saveVehicleSystemToSearch(Integer vehicleSearchPk,
			Integer systemsToSearchPk) {
        log.debug("Inserting rows into VEHICLE_SYSTEMS_TO_SEARCH table : " + systemsToSearchPk.toString());
        
        final String VEHICLE_SYSTEMS_TO_SEARCH_INSERT="INSERT into VEHICLE_SYSTEMS_TO_SEARCH "
        		+ "(VEHICLE_SEARCH_REQUEST_ID, SYSTEMS_TO_SEARCH_ID) "
        		+ "values (?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(VEHICLE_SYSTEMS_TO_SEARCH_INSERT, new String[] {"VEHICLE_SYSTEMS_TO_SEARCH_ID"});
        	            DaoUtils.setPreparedStatementVariable(vehicleSearchPk, ps, 1);
        	            DaoUtils.setPreparedStatementVariable(systemsToSearchPk, ps, 2);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	 	
    }	
	
	@Override
	public Integer saveIncidentSystemToSearch(Integer incidentSearchPk,
			Integer systemsToSearchPk) {
		
        log.debug("Inserting rows into INCIDENT_SYSTEMS_TO_SEARCH table : " + systemsToSearchPk.toString());
        
        final String INCIDENT_SYSTEMS_TO_SEARCH_INSERT="INSERT into INCIDENT_SYSTEMS_TO_SEARCH "
        		+ "(INCIDENT_SEARCH_REQUEST_ID, SYSTEMS_TO_SEARCH_ID) "
        		+ "values (?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(INCIDENT_SYSTEMS_TO_SEARCH_INSERT, new String[] {"INCIDENT_SYSTEMS_TO_SEARCH_ID"});
        	            DaoUtils.setPreparedStatementVariable(incidentSearchPk, ps, 1);
        	            DaoUtils.setPreparedStatementVariable(systemsToSearchPk, ps, 2);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	 	
	}
	
	@Override
	public Integer saveUserInfo(UserInfo userInfo) {
		
        log.debug("Inserting row into USER_INFO table : " + userInfo);
        
        final String USER_INFO_INSERT="INSERT into USER_INFO "  
        		+ "(USER_FIRST_NAME,IDENTITY_PROVIDER_ID,EMPLOYER_NAME,USER_EMAIL_ADDRESS,USER_LAST_NAME,EMPLOYER_SUBUNIT_NAME,FEDERATION_ID,EMPLOYER_ORI) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(USER_INFO_INSERT, new String[] {"USER_INFO_ID"});
        	            DaoUtils.setPreparedStatementVariable(userInfo.getUserFirstName(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(userInfo.getIdentityProviderId(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(userInfo.getEmployerName(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(userInfo.getUserEmailAddress(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(userInfo.getUserLastName(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(userInfo.getEmployerSubunitName(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(userInfo.getFederationId(), ps, 7);
        	            DaoUtils.setPreparedStatementVariable(userInfo.getEmployerOri(), ps, 8);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	        
	}	
	

	@Override
	public Integer saveuserAcknowledgement(
			UserAcknowledgement userAcknowledgement) {
		
        log.debug("Inserting row into USER_ACKNOWLEDGEMENT table : " + userAcknowledgement);
        
        final String USER_ACKNOWLEDGEMENT_INSERT="INSERT into USER_ACKNOWLEDGEMENT "  
        		+ "(DECISION, USER_INFO_ID, SID, DECISION_TIMESTAMP) "
        		+ "values (?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(USER_ACKNOWLEDGEMENT_INSERT, new String[] {"USER_ACKNOWLEDGEMENT_ID"});
        	            DaoUtils.setPreparedStatementVariable(userAcknowledgement.isDecision(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(userAcknowledgement.getUserInfo().getUserInfoId(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(userAcknowledgement.getSid(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(userAcknowledgement.getDecisionDateTime(), ps, 4);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		
    }

	@Override
	public Integer saveFirearmsQueryResponse(
			FirearmsQueryResponse firearmsQueryResponse) {
		log.debug("Inserting row into FIREARMS_QUERY_RESULTS table : " + firearmsQueryResponse.toString());
		
        final String FIREARMS_QUERY_RESULTS_INSERT="INSERT into FIREARMS_QUERY_RESULTS "  
        		+ "(QUERY_REQUEST_ID, SYSTEM_NAME, MESSAGE_ID, QUERY_RESULTS_TIMEOUT_INDICATOR, QUERY_RESULTS_ERROR_INDICATOR, QUERY_RESULTS_ERROR_TEXT, REGISTRATION_NUMBER, LAST_NAME, MIDDLE_NAME, FIRST_NAME, COUNTY) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(FIREARMS_QUERY_RESULTS_INSERT, new String[] {"FIREARMS_QUERY_RESULTS_ID"});
        	            DaoUtils.setPreparedStatementVariable(firearmsQueryResponse.getQueryRequestId(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(firearmsQueryResponse.getSystemName(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(firearmsQueryResponse.getMessageId(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(firearmsQueryResponse.isQueryResultsTimeoutIndicator(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(firearmsQueryResponse.isQueryResultsErrorIndicator(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(firearmsQueryResponse.getQueryResultsErrorText(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(firearmsQueryResponse.getRegistrationNumber(), ps, 7);
        	            DaoUtils.setPreparedStatementVariable(firearmsQueryResponse.getLastName(), ps, 8);
        	            DaoUtils.setPreparedStatementVariable(firearmsQueryResponse.getMiddleName(), ps, 9);
        	            DaoUtils.setPreparedStatementVariable(firearmsQueryResponse.getFirstName(), ps, 10);
        	            DaoUtils.setPreparedStatementVariable(firearmsQueryResponse.getCounty(), ps, 11);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	        

	}	
	
	@Override
	public Integer savePersonQueryCriminalHistoryResponse(
			PersonQueryCriminalHistoryResponse personQueryCriminalHistoryResponse) {
		
		log.debug("Inserting row into CRIMINAL_HISTORY_QUERY_RESULTS table : " + personQueryCriminalHistoryResponse.toString());
		
        final String CRIMINAL_HISTORY_QUERY_RESULTS_INSERT="INSERT into CRIMINAL_HISTORY_QUERY_RESULTS "  
        		+ "(FIRST_NAME, MIDDLE_NAME, LAST_NAME, SID, FBI_ID, QUERY_REQUEST_ID, QUERY_RESULTS_ERROR_TEXT, QUERY_RESULTS_TIMEOUT_INDICATOR,QUERY_RESULTS_ERROR_INDICATOR,SYSTEM_NAME,MESSAGE_ID) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(CRIMINAL_HISTORY_QUERY_RESULTS_INSERT, new String[] {"CRIMINAL_HISTORY_QUERY_RESULTS_ID"});
        	            DaoUtils.setPreparedStatementVariable(personQueryCriminalHistoryResponse.getFirstName(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(personQueryCriminalHistoryResponse.getMiddleName(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(personQueryCriminalHistoryResponse.getLastName(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(personQueryCriminalHistoryResponse.getSid(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(personQueryCriminalHistoryResponse.getFbiId(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(personQueryCriminalHistoryResponse.getQueryRequestId(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(personQueryCriminalHistoryResponse.getQueryResultsErrorText(), ps, 7);
        	            DaoUtils.setPreparedStatementVariable(personQueryCriminalHistoryResponse.isQueryResultsTimeoutIndicator(), ps, 8);
        	            DaoUtils.setPreparedStatementVariable(personQueryCriminalHistoryResponse.isQueryResultsErrorIndicator(), ps, 9);
        	            DaoUtils.setPreparedStatementVariable(personQueryCriminalHistoryResponse.getSystemName(), ps, 10);
        	            DaoUtils.setPreparedStatementVariable(personQueryCriminalHistoryResponse.getMessageId(), ps, 11);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	        

	}	
	
	@Override
	public Integer savePersonQueryWarrantResponse(
			PersonQueryWarrantResponse personQueryWarrantResponse) {
		
		log.debug("Inserting row into WARRANT_QUERY_RESULTS table : " + personQueryWarrantResponse.toString());
		
        final String WARRANT_QUERY_RESULTS_INSERT="INSERT into WARRANT_QUERY_RESULTS "  
        		+ "(FIRST_NAME, MIDDLE_NAME, LAST_NAME, SID, FBI_ID, QUERY_REQUEST_ID, QUERY_RESULTS_ERROR_TEXT, QUERY_RESULTS_TIMEOUT_INDICATOR,QUERY_RESULTS_ERROR_INDICATOR,QUERY_RESULTS_ACCESS_DENIED_INDICATOR,SYSTEM_NAME,MESSAGE_ID) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(WARRANT_QUERY_RESULTS_INSERT, new String[] {"CRIMINAL_HISTORY_QUERY_RESULTS_ID"});
        	            DaoUtils.setPreparedStatementVariable(personQueryWarrantResponse.getFirstName(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(personQueryWarrantResponse.getMiddleName(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(personQueryWarrantResponse.getLastName(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(personQueryWarrantResponse.getSid(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(personQueryWarrantResponse.getFbiId(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(personQueryWarrantResponse.getQueryRequestId(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(personQueryWarrantResponse.getQueryResultsErrorText(), ps, 7);
        	            DaoUtils.setPreparedStatementVariable(personQueryWarrantResponse.isQueryResultsTimeoutIndicator(), ps, 8);
        	            DaoUtils.setPreparedStatementVariable(personQueryWarrantResponse.isQueryResultsErrorIndicator(), ps, 9);
        	            DaoUtils.setPreparedStatementVariable(personQueryWarrantResponse.isQueryResultsAccessDeniedIndicator(), ps, 10);
        	            DaoUtils.setPreparedStatementVariable(personQueryWarrantResponse.getSystemName(), ps, 11);
        	            DaoUtils.setPreparedStatementVariable(personQueryWarrantResponse.getMessageId(), ps, 12);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
	}
	
	@Override
	public Integer saveQueryRequest(QueryRequest queryRequest) {
		
        log.debug("Inserting row into QUERY_REQUEST table : " + queryRequest);
        
        final String QUERY_REQUEST_INSERT="INSERT into QUERY_REQUEST "
        		+ "(MESSAGE_ID,IDENTIFICATION_ID,USER_INFO_ID,SYSTEM_NAME) "
        		+ "values (?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(QUERY_REQUEST_INSERT, new String[] {"QUERY_REQUEST_ID"});
        	            DaoUtils.setPreparedStatementVariable(queryRequest.getMessageId(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(queryRequest.getIdentificationId(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(queryRequest.getUserInfofk(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(queryRequest.getIdentificationSourceText(), ps, 4);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	        
	}
	
	@Override
	public Integer savePersonSearchResult(PersonSearchResult personSearchResult) {
        log.debug("Inserting row into PERSON_SEARCH_RESULTS table : " + personSearchResult);
        
        final String PERSON_SEARCH_RESULT_INSERT="INSERT into PERSON_SEARCH_RESULTS "
        		+ "(PERSON_SEARCH_REQUEST_ID,SYSTEMS_TO_SEARCH_ID,SEARCH_RESULTS_ERROR_INDICATOR,SEARCH_RESULTS_ERROR_TEXT,SEARCH_RESULTS_TIMEOUT_INDICATOR,SEARCH_RESULTS_COUNT,SEARCH_RESULTS_ACCESS_DENIED_INDICATOR)"
        		+ "values (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(PERSON_SEARCH_RESULT_INSERT, new String[] {"PERSON_SEARCH_RESULTS_ID"});
        	            DaoUtils.setPreparedStatementVariable(personSearchResult.getPersonSearchRequestId(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(personSearchResult.getSystemSearchResultID(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(personSearchResult.getSearchResultsErrorIndicator(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(personSearchResult.getSearchResultsErrorText(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(personSearchResult.getSearchResultsTimeoutIndicator(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(personSearchResult.getSearchResultsCount(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(personSearchResult.getSearchResultsAccessDeniedIndicator(), ps, 7);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
	}

	@Override
	public Integer saveSubscriptionSearchResult(
			SubscriptionSearchResult subscriptionSearchResult) {
        log.debug("Inserting row into SUBSCRIPTION_SEARCH_RESULTS table : " + subscriptionSearchResult);
        
        final String SUBSCRIPTION_SEARCH_RESULT_INSERT="INSERT into SUBSCRIPTION_SEARCH_RESULTS "
        		+ "(SEARCH_RESULTS_TIMEOUT_INDICATOR, SEARCH_RESULTS_COUNT, SUBSCRIPTION_SEARCH_REQUEST_ID, SEARCH_RESULTS_ERROR_INDICATOR, SEARCH_RESULTS_ERROR_TEXT, SEARCH_RESULTS_ACCESS_DENIED_INDICATOR)"
        		+ "values (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(SUBSCRIPTION_SEARCH_RESULT_INSERT, new String[] {"SUBSCRIPTION_SEARCH_RESULTS_ID"});
        	            DaoUtils.setPreparedStatementVariable(subscriptionSearchResult.getSearchResultsTimeoutIndicator(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(subscriptionSearchResult.getSearchResultsCount(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(subscriptionSearchResult.getSubscriptionSearchRequestId(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(subscriptionSearchResult.getSearchResultsErrorIndicator(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(subscriptionSearchResult.getSearchResultsErrorText(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(subscriptionSearchResult.getSearchResultsAccessDeniedIndicator(), ps, 6);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
	}
	
	@Override
	public Integer saveFirearmSearchResult(
			FirearmSearchResult firearmSearchResult) {
        log.debug("Inserting row into FIREARMS_SEARCH_RESULTS table : " + firearmSearchResult);
        
        final String PERSON_SEARCH_RESULT_INSERT="INSERT into FIREARMS_SEARCH_RESULTS "
        		+ "(FIREARMS_SEARCH_REQUEST_ID,SYSTEMS_TO_SEARCH_ID,SEARCH_RESULTS_ERROR_INDICATOR,SEARCH_RESULTS_ERROR_TEXT,SEARCH_RESULTS_TIMEOUT_INDICATOR,SEARCH_RESULTS_COUNT,SEARCH_RESULTS_ACCESS_DENIED_INDICATOR)"
        		+ "values (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(PERSON_SEARCH_RESULT_INSERT, new String[] {"FIREARMS_SEARCH_RESULTS_ID"});
        	            DaoUtils.setPreparedStatementVariable(firearmSearchResult.getFirearmSearchRequestId(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(firearmSearchResult.getSystemSearchResultID(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(firearmSearchResult.getSearchResultsErrorIndicator(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(firearmSearchResult.getSearchResultsErrorText(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(firearmSearchResult.getSearchResultsTimeoutIndicator(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(firearmSearchResult.getSearchResultsCount(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(firearmSearchResult.getSearchResultsAccessDeniedIndicator(), ps, 7);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}
	
	@Override
	public Integer saveIdentificationSearchRequest(
			IdentificationSearchRequest identificationSearchRequest) {
		
        log.debug("Inserting row into IDENTIFICATION_SEARCH_REQUEST table : " + identificationSearchRequest);
        
        final String IDENTIFICATION_SEARCH_REQUEST_INSERT="INSERT into IDENTIFICATION_SEARCH_REQUEST "
        		+ "(FIRST_NAME, LAST_NAME, REPORTED_FROM_DATE, REPORTED_TO_DATE,USER_INFO_ID, MESSAGE_ID, IDENTIFICATION_RESULTS_STATUS, OTN) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
        

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(IDENTIFICATION_SEARCH_REQUEST_INSERT, new String[] {"IDENTIFICATION_SEARCH_REQUEST_ID"});
        	            DaoUtils.setPreparedStatementVariable(identificationSearchRequest.getFirstName(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(identificationSearchRequest.getLastName(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(identificationSearchRequest.getReportedFromDate(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(identificationSearchRequest.getReportedToDate(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(identificationSearchRequest.getUserInfoId(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(identificationSearchRequest.getMessageId(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(identificationSearchRequest.getIdentificationResultsStatus(), ps, 7);
        	            DaoUtils.setPreparedStatementVariable(identificationSearchRequest.getOtn(), ps, 8);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		
         
	}


	@Override
	public Integer saveidentificationSearchResponse(
			IdentificationSearchResult identificationSearchResult) {
		
		log.debug("Inserting row into IDENTIFICATION_SEARCH_RESULTS table : " + identificationSearchResult);
		
        final String IDENTIFICATION_SEARCH_RESULT_INSERT="INSERT into IDENTIFICATION_SEARCH_RESULTS "
        		+ "(MESSAGE_ID, IDENTIFICATION_SEARCH_REQUEST_ID, AVAILABLE_RESULTS)"
        		+ "values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(IDENTIFICATION_SEARCH_RESULT_INSERT, new String[] {"IDENTIFICATION_SEARCH_RESULTS_ID"});
        	            DaoUtils.setPreparedStatementVariable(identificationSearchResult.getMessageId(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(identificationSearchResult.getIdentificationSearchRequestId(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(identificationSearchResult.getAvailableResults(), ps, 3);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
	}
	
	@Override
	public Integer saveidentificationQueryResponse(
			IdentificationQueryResponse identificationQueryResponse) {

		log.debug("Inserting row into IDENTIFICATION_RESULTS_QUERY_DETAIL table : " + identificationQueryResponse.toString());
		
        final String IDENTIFICATION_QUERY_INSERT="INSERT into IDENTIFICATION_RESULTS_QUERY_DETAIL "
        		+ "(QUERY_REQUEST_ID, PERSON_FIRST_NAME, PERSON_MIDDLE_NAME,PERSON_LAST_NAME,OCA,SID,FBI_ID,ID_DATE,OTN, ORI) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(IDENTIFICATION_QUERY_INSERT, new String[] {"IDENTIFICATION_RESULTS_QUERY_DETAIL_ID"});
        	            DaoUtils.setPreparedStatementVariable(identificationQueryResponse.getQueryRequestId(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(identificationQueryResponse.getPersonFirstName(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(identificationQueryResponse.getPersonMiddleName(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(identificationQueryResponse.getPersonLastName(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(identificationQueryResponse.getOca(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(identificationQueryResponse.getSid(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(identificationQueryResponse.getFbiId(), ps, 7);
        	            DaoUtils.setPreparedStatementVariable(identificationQueryResponse.getIdDate(), ps, 8);
        	            DaoUtils.setPreparedStatementVariable(identificationQueryResponse.getOtn(), ps, 9);
        	            DaoUtils.setPreparedStatementVariable(identificationQueryResponse.getOri(), ps, 10);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
	}
	
	@Override
	public Integer saveSubscriptionSearchRequest(
			SubscriptionSearchRequest subscriptionSearchRequest) {
		
        log.debug("Inserting row into SUBSCRIPTION_SEARCH_REQUEST table : " + subscriptionSearchRequest);
        
        final String SUBSCRIPTION_SEARCH_REQUEST_INSERT="INSERT into SUBSCRIPTION_SEARCH_REQUEST "
        		+ "(MESSAGE_ID, FIRST_NAME, LAST_NAME, SID, FBI_ID, USER_INFO_ID, ACTIVE_SUBSCRIPTIONS_INDICATOR, ADMIN_SEARCH_REQUEST_INDICATOR) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(SUBSCRIPTION_SEARCH_REQUEST_INSERT, new String[] {"SUBSCRIPTION_SEARCH_REQUEST_INSERT_ID"});
        	            DaoUtils.setPreparedStatementVariable(subscriptionSearchRequest.getMessageId(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(subscriptionSearchRequest.getSubjectFirstName(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(subscriptionSearchRequest.getSubjectLastName(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(subscriptionSearchRequest.getSid(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(subscriptionSearchRequest.getUcn(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(subscriptionSearchRequest.getUserInfoFk(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(subscriptionSearchRequest.getActive(), ps, 7);
        	            DaoUtils.setPreparedStatementVariable(subscriptionSearchRequest.getAdminSearch(), ps, 8);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();			
	}	
	
	@Override
	public Integer savePersonSearchRequest(
			PersonSearchRequest personSearchRequest) {
		
        log.debug("Inserting row into PERSON_SEARCH_REQUEST table : " + personSearchRequest);
        
        final String PERSON_SEARCH_REQUEST_INSERT="INSERT into PERSON_SEARCH_REQUEST "
        		+ "(DOB_START_DATE, RACE, EYE_COLOR,HAIR_COLOR,DRIVERS_LICENSE_NUMBER,DOB_END_DATE,ON_BEHALF_OF,FIRST_NAME_QUALIFIER_CODE_ID,SID,MIDDLE_NAME,"
        		+ "LAST_NAME_QUALIFIER_CODE_ID,PURPOSE,LAST_NAME,FIRST_NAME,GENDER,MESSAGE_ID,USER_INFO_ID,DRIVERS_LICENSE_ISSUER,FBI_ID, SSN, HEIGHT, HEIGHT_MIN, HEIGHT_MAX) "
        		+ "values (?, ?, ?, ?, ?, ?,?, ?, ?,?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?,?,?)";
        

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(PERSON_SEARCH_REQUEST_INSERT, new String[] {"PERSON_SEARCH_REQUEST_ID"});
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getDobFrom(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getRaceCode(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getEyeCode(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getHairCode(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getDriverLicenseId(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getDobTo(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getOnBehalfOf(), ps, 7);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getFirstNameQualifier(), ps, 8);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getStateId(), ps, 9);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getMiddleName(), ps, 10);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getLastNameQualifier(), ps, 11);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getPurpose(), ps, 12);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getLastName(), ps, 13);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getFirstName(), ps, 14);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getGenderCode(), ps, 15);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getMessageId(), ps, 16);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getUserInfofk(), ps, 17);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getDriverLiscenseIssuer(), ps, 18);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getFbiNumber(), ps, 19);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getSsn(), ps, 20);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getHeight(), ps, 21);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getHeightMin(), ps, 22);
        	            DaoUtils.setPreparedStatementVariable(personSearchRequest.getHeightMax(), ps, 23);
        	            
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
	}		

	@Override
	public void updateFederalRapbackSubscriptionWithResponse(
			FederalRapbackSubscription federalRapbackSubscription)
			throws Exception {
		
		Map<String, Object> paramMap = new HashMap<String, Object>(); 
		
		final String FEDERAL_SUBSCRIPTION_UPDATE="UPDATE FEDERAL_RAPBACK_SUBSCRIPTION SET "
				+ "TRANSACTION_CATEGORY_CODE_RESPONSE = :transactionCategoryCodeResponse, "
				+ "RESPONSE_RECIEVED_TIMESTAMP = :responseRecievedTimestamp, "
				+ "PATH_TO_RESPONSE_FILE = :pathToResponseFile, "
				+ "TRANSACTION_STATUS_TEXT = :transactionStatusText, "
				+ "FBI_SUBSCRIPTION_ID = :fbiSubscriptionId "
				+ "WHERE FEDERAL_RAPBACK_SUBSCRIPTION_ID = :federalRapbackSubscriptionId";

		paramMap.put("transactionCategoryCodeResponse", federalRapbackSubscription.getTransactionCategoryCodeResponse()); 
		paramMap.put("responseRecievedTimestamp", convertToDatabaseColumn(federalRapbackSubscription.getResponseRecievedTimestamp())); 
		paramMap.put("pathToResponseFile", federalRapbackSubscription.getPathToResponseFile()); 
		paramMap.put("federalRapbackSubscriptionId", federalRapbackSubscription.getFederalRapbackSubscriptionId()); 
		paramMap.put("transactionStatusText", federalRapbackSubscription.getTransactionStatusText());
		paramMap.put("fbiSubscriptionId", federalRapbackSubscription.getFbiSubscriptionId());
		
		
		namedParameterJdbcTemplate.update(FEDERAL_SUBSCRIPTION_UPDATE, paramMap);

	}
	
	@Override
	public void updateSubscriptionActionWithResponse(
			SubscriptionAction subscriptionAction) {

		Map<String, Object> paramMap = new HashMap<String, Object>(); 
		
		final String SUBSCRIPTION_ACTIONS_UPDATE="UPDATE SUBSCRIPTION_ACTIONS SET "
				+ "SUCCESS_INDICATOR = :successIndicator "
				+ "WHERE SUBSCRIPTION_ACTIONS_ID = :subscriptionActionsId";

		final String SUBSCRIPTION_ACTIONS_WITH_SUBSCRIPRTIONID_UPDATE="UPDATE SUBSCRIPTION_ACTIONS SET "
				+ "STATE_SUBSCRIPTION_ID = :stateSubscriptionId, "
				+ "SUCCESS_INDICATOR = :successIndicator "
				+ "WHERE SUBSCRIPTION_ACTIONS_ID = :subscriptionActionsId";

		
		paramMap.put("successIndicator", subscriptionAction.isSuccessIndicator()); 
		paramMap.put("subscriptionActionsId", subscriptionAction.getSubscriptionActionId()); 
		
		if (StringUtils.isNotBlank(subscriptionAction.getStateSubscriptionId()))
		{
			paramMap.put("stateSubscriptionId", subscriptionAction.getStateSubscriptionId());
			namedParameterJdbcTemplate.update(SUBSCRIPTION_ACTIONS_WITH_SUBSCRIPRTIONID_UPDATE, paramMap);	
		}	
		else
		{
			namedParameterJdbcTemplate.update(SUBSCRIPTION_ACTIONS_UPDATE, paramMap);	
		}	
	}

	
	@Override
	public void updateFederalRapbackIdentityHistoryWithResponse(
			FederalRapbackIdentityHistory federalRapbackIdentityHistory)
			throws Exception {

		Map<String, Object> paramMap = new HashMap<String, Object>(); 
		
		final String FEDERAL_RAPBACK_IDENTITY_HISTORY_UPDATE="UPDATE FEDERAL_RAPBACK_IDENTITY_HISTORY SET "
				+ "TRANSACTION_CATEGORY_CODE_RESPONSE = :transactionCategoryCodeResponse, "
				+ "PATH_TO_RESPONSE_FILE = :pathToResponseFile, "
				+ "RESPONSE_RECIEVED_TIMESTAMP = :responseRecievedTimestamp "
				+ "WHERE TRANSACTION_CONTROL_REFERENCE_IDENTIFICATION = :transactionControlReferenceIdentification";

		paramMap.put("transactionCategoryCodeResponse", federalRapbackIdentityHistory.getTransactionCategoryCodeResponse()); 
		paramMap.put("responseRecievedTimestamp", convertToDatabaseColumn(federalRapbackIdentityHistory.getResponseReceivedTimestamp())); 
		paramMap.put("transactionControlReferenceIdentification", federalRapbackIdentityHistory.getTransactionControlReferenceIdentification());
		paramMap.put("pathToResponseFile", federalRapbackIdentityHistory.getPathToResponseFile());
		
		namedParameterJdbcTemplate.update(FEDERAL_RAPBACK_IDENTITY_HISTORY_UPDATE, paramMap);
		
	}	

	@Override
	public FederalRapbackSubscription retrieveFederalRapbackSubscriptionFromTCN(
			String transactionControlNumber) {
		final String SUBSCRIPTION_SELECT="SELECT * FROM FEDERAL_RAPBACK_SUBSCRIPTION WHERE TRANSACTION_CONTROL_REFERENCE_IDENTIFICATION = ?";
		
		List<FederalRapbackSubscription> federalRapbackSubscriptions = jdbcTemplate.query(SUBSCRIPTION_SELECT, new FederalRapbackSubscriptionRowMapper(), transactionControlNumber);
		return DataAccessUtils.singleResult(federalRapbackSubscriptions);	
	}
	
	@Override
	public Integer retrievePersonSearchIDfromMessageID(String messageId) {
		StringBuffer sqlStatement = new StringBuffer(); 
		
		sqlStatement.append("select psr.*, sss.SYSTEM_NAME from person_search_request psr, PERSON_SYSTEMS_TO_SEARCH pss, SYSTEMS_TO_SEARCH sss where psr.PERSON_SEARCH_REQUEST_ID = pss.PERSON_SEARCH_REQUEST_ID ");
		sqlStatement.append(" and sss.SYSTEMS_TO_SEARCH_ID  = pss.SYSTEMS_TO_SEARCH_ID and MESSAGE_ID = ?");
		
		Integer ret = null;
		
		List<PersonSearchRequest> personSearchRequests = jdbcTemplate.query(sqlStatement.toString(), new PersonSearchRequestRowMapper(), messageId);
		
		if (personSearchRequests == null)
		{
			throw new IllegalStateException("Unable to retrieve person search request ID");
		}
		
		if (personSearchRequests.size() == 0 ||  personSearchRequests.size() > 1)
		{
			throw new IllegalStateException("Query returned zero or more than person search request, size: " + personSearchRequests.size());
		}
		
		if (personSearchRequests.size() == 1)
		{
			ret = personSearchRequests.get(0).getPersonSearchRequestID();
		}	

		return ret;
	}
	
	@Override
	public Integer retrieveFirearmSearchIDfromMessageID(String messageId) {
		StringBuffer sqlStatement = new StringBuffer(); 
		
		sqlStatement.append("select fsr.*, sss.SYSTEM_NAME from firearms_search_request fsr, FIREARMS_SYSTEMS_TO_SEARCH fss, SYSTEMS_TO_SEARCH sss where fsr.FIREARMS_SEARCH_REQUEST_ID = fss.FIREARMS_SEARCH_REQUEST_ID ");
		sqlStatement.append(" and sss.SYSTEMS_TO_SEARCH_ID  = fss.SYSTEMS_TO_SEARCH_ID and fsr.MESSAGE_ID = ?");
		
		Integer ret = null;
		
		List<FirearmsSearchRequest> firearmSearchRequests = jdbcTemplate.query(sqlStatement.toString(), new FirearmSearchRequestRowMapper(), messageId);
		
		if (firearmSearchRequests == null)
		{
			throw new IllegalStateException("Unable to retrieve firearm search request ID");
		}
		
		if (firearmSearchRequests.size() == 0 ||  firearmSearchRequests.size() > 1)
		{
			throw new IllegalStateException("Query returned zero or more than firearm search request, size: " + firearmSearchRequests.size());
		}
		
		if (firearmSearchRequests.size() == 1)
		{
			ret = firearmSearchRequests.get(0).getFirearmSearchRequestID();
		}	

		return ret;	
	}
	
	@Override
	public Integer retrieveSubscriptionSearchIDfromMessageID(String messageId) {
		final String SQL_SELECT="SELECT * FROM SUBSCRIPTION_SEARCH_REQUEST WHERE MESSAGE_ID = ?";
		
		Integer ret = null;
		
		List<SubscriptionSearchRequest> subscriptionSearchRequests = jdbcTemplate.query(SQL_SELECT, new SubscriptionSearchRequestRowMapper(), messageId);
		
		if (subscriptionSearchRequests == null)
		{
			throw new IllegalStateException("Unable to retrieve subscription search request ID");
		}
		
		if (subscriptionSearchRequests.size() == 0 ||  subscriptionSearchRequests.size() > 1)
		{
			throw new IllegalStateException("Query returned zero or more than firearm search request, size: " + subscriptionSearchRequests.size());
		}
		
		if (subscriptionSearchRequests.size() == 1)
		{
			ret = subscriptionSearchRequests.get(0).getSubscriptionSearchRequestPk();
		}	

		return ret;	
	}
	
	@Override
	public Integer retrieveOrganizationIdentificationIDfromMessageID(
			String messageId) {
		final String IDENTIFICATION_SELECT="SELECT * FROM IDENTIFICATION_SEARCH_REQUEST WHERE MESSAGE_ID = ?";
		
		Integer ret = null;
		
		List<IdentificationSearchRequest> identificationSearchRequests = jdbcTemplate.query(IDENTIFICATION_SELECT, new IdentificationSearchRequestRowMapper(), messageId);
		
		if (identificationSearchRequests == null)
		{
			throw new IllegalStateException("Unable to retrieve identification search request ID");
		}
		
		if (identificationSearchRequests.size() == 0 ||  identificationSearchRequests.size() > 1)
		{
			throw new IllegalStateException("Query returned zero or more than identification search request, size: " + identificationSearchRequests.size());
		}
		
		if (identificationSearchRequests.size() == 1)
		{
			ret = identificationSearchRequests.get(0).getIdentificationSearchRequestId();
		}	

		return ret;	
	}	
	
	@Override
	public Integer retrievePersonQueryIDfromMessageID(String messageId) {
		final String SUBSCRIPTION_SELECT="SELECT * FROM QUERY_REQUEST WHERE MESSAGE_ID = ?";
		
		Integer ret = null;
		
		List<QueryRequest> personQueryRequests = jdbcTemplate.query(SUBSCRIPTION_SELECT, new PersonQueryRequestRowMapper(), messageId);
		
		if (personQueryRequests == null)
		{
			throw new IllegalStateException("Unable to retrieve person query request ID");
		}
		
		if (personQueryRequests.size() == 0 ||  personQueryRequests.size() > 1)
		{
			throw new IllegalStateException("Query returned zero or more than person query request, size: " + personQueryRequests.size());
		}
		
		if (personQueryRequests.size() == 1)
		{
			ret = personQueryRequests.get(0).getQueryRequestId();
		}	

		return ret;	
	}


	@Override
	public UserInfo retrieveUserInfoFromId(Integer userInfoPk) {
		final String USER_INFO_SELECT="SELECT * FROM USER_INFO WHERE USER_INFO_ID = ?";
		
		List<UserInfo> userInfo = jdbcTemplate.query(USER_INFO_SELECT, new UserInfoRowMapper(), userInfoPk);
		return DataAccessUtils.singleResult(userInfo);	
	}	
	
	@Override
	public List<UserInfo> retrieveUserInfoFromFederationId(String federationId) {
		final String USER_INFO_SELECT="SELECT * FROM USER_INFO WHERE FEDERATION_ID = ? order by USER_INFO_ID desc";
		
		List<UserInfo> userInfoList = jdbcTemplate.query(USER_INFO_SELECT, new UserInfoRowMapper(), federationId);
		return userInfoList;	
	}	

	@Override
	public List<FederalRapbackNotification> retrieveFederalNotifications(
			LocalDate startDate, LocalDate endDate) {
		
		//The query is set up to be a less than query so we add a day
		//So we are getting everything before midnight tomorrow
		if (endDate != null)
		{
			endDate = endDate.plusDays(1);
		}	
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		String startDateString = startDate.format(formatter) + " 00:00:00";
		String endDateString = endDate.format(formatter) + " 00:00:00";
		
		String notificationSelectStatement ="SELECT * FROM FEDERAL_RAPBACK_NOTIFICATION WHERE NOTIFICATION_RECIEVED_TIMESTAMP > '" + startDateString + "' AND NOTIFICATION_RECIEVED_TIMESTAMP < '" + endDateString +  "' order by NOTIFICATION_RECIEVED_TIMESTAMP desc";
		
		log.info("Retrieve Federal Notifications SQL: " + notificationSelectStatement);
		
		List<FederalRapbackNotification> federalRapbackNotifications = jdbcTemplate.query(notificationSelectStatement, new FederalRapbackNotificationRowMapper());
		
		addTriggeringEvents(federalRapbackNotifications);	
		
		return federalRapbackNotifications;	
		
	}
	
	@Override
	public List<NotificationSent> retrieveNotifications(LocalDate startDate,
			LocalDate endDate) {
		//The query is set up to be a less than query so we add a day
		//So we are getting everything before midnight tomorrow
		if (endDate != null)
		{
			endDate = endDate.plusDays(1);
		}	
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		String startDateString = startDate.format(formatter) + " 00:00:00";
		String endDateString = endDate.format(formatter) + " 00:00:00";
		
		String notificationSelectStatement ="SELECT * FROM NOTIFICATIONS_SENT WHERE TIMESTAMP > '" + startDateString + "' AND TIMESTAMP < '" + endDateString +  "' and subscription_owner != 'SYSTEM' order by TIMESTAMP desc";
		
		log.info("Retrieve Notifications Sent SQL: " + notificationSelectStatement);
		
		List<NotificationSent> notificationsSent = jdbcTemplate.query(notificationSelectStatement, new NotificationSentRowMapper());
		
		//add notification properties here
		addNotificationProperties(notificationsSent);
		
		return notificationsSent;	
	}
	
	private void addNotificationProperties(
			List<NotificationSent> notificationsSent) {
		
		for (NotificationSent notificationSent : notificationsSent)
		{
			Integer id = notificationSent.getNotificationSentId();
			
			String notificationSelectStatement ="select PROPERTY_VALUE from notification_properties where NOTIFICATIONS_SENT_ID=?";
			
			List<String> triggeringEvents = jdbcTemplate.queryForList(notificationSelectStatement, String.class, id);
			
			notificationSent.setTriggeringEvents(triggeringEvents);
			
		}	
		
	}

	@Override
	public List<FederalRapbackRenewalNotification> retrieveFederalRapbackRenewalNotifications(
			LocalDate startDate, LocalDate endDate) {
		//The query is set up to be a less than query so we add a day
		//So we are getting everything before midnight tomorrow
		if (endDate != null)
		{
			endDate = endDate.plusDays(1);
		}	
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		String startDateString = startDate.format(formatter) + " 00:00:00";
		String endDateString = endDate.format(formatter) + " 00:00:00";
		
		String notificationSelectStatement ="SELECT * FROM FEDERAL_RAPBACK_RENEWAL_NOTIFICATION WHERE NOTIFICATION_RECIEVED_TIMESTAMP > '" + startDateString + "' AND NOTIFICATION_RECIEVED_TIMESTAMP < '" + endDateString +  "' order by NOTIFICATION_RECIEVED_TIMESTAMP desc";
		
		log.info("Retrieve Federal Rapback Renewal Notifications SQL: " + notificationSelectStatement);
		
		List<FederalRapbackRenewalNotification> federalRapbackRenewalNotifications = jdbcTemplate.query(notificationSelectStatement, new FederalRapbackRenewalNotificationRowMapper());
		
		return federalRapbackRenewalNotifications;		
	}	
	
	@Override
	public List<FederalRapbackNotification> retrieveFederalNotificationsBySubscriptionId(
			String subscriptionId) {
		String notificationSelectStatement ="SELECT * FROM FEDERAL_RAPBACK_NOTIFICATION WHERE STATE_SUBSCRIPTION_ID = ? order by NOTIFICATION_RECIEVED_TIMESTAMP desc";
		
		log.info("Retrieve Federal Notifications SQL by state subscription ID: " + subscriptionId);
		
		List<FederalRapbackNotification> federalRapbackNotifications = jdbcTemplate.query(notificationSelectStatement, new FederalRapbackNotificationRowMapper(), subscriptionId);
		
		addTriggeringEvents(federalRapbackNotifications);	
		
		return federalRapbackNotifications;
	}
	
	@Override
	public List<FederalRapbackSubscription> retrieveFederalRapbackSubscriptionErrors() {
		final String SUBSCRIPTION_SELECT="select frs.* from FEDERAL_RAPBACK_SUBSCRIPTION frs, FEDERAL_RAPBACK_SUBSCRIPTION_ERRORS frse where "
				+ " frs.FEDERAL_RAPBACK_SUBSCRIPTION_ID = frse.FEDERAL_RAPBACK_SUBSCRIPTION_ID and frse.ERROR_REPORTED = true and frse.ERROR_RESOLVED= false";
		
		List<FederalRapbackSubscription> federalRapbackSubscriptions = jdbcTemplate.query(SUBSCRIPTION_SELECT, new FederalRapbackSubscriptionRowMapper());
		
		return federalRapbackSubscriptions;
	}

	@Override
	public Integer retrieveFederalRapbackSubscriptionError(
			String stateSubscriptionId) {
		String federalRapbackSubscriptionErrorStatement ="SELECT FEDERAL_RAPBACK_SUBSCRIPTION_ERRORS_ID FROM FEDERAL_RAPBACK_SUBSCRIPTION_ERRORS WHERE STATE_SUBSCRIPTION_ID = ? AND ERROR_RESOLVED = false  AND ERROR_REPORTED = true";
		
		log.info("Retrieve Federal Notifications SQL by state subscription ID: " + stateSubscriptionId);
		
		Integer federalRapbackSubscriptionErrorId = null;
		try {
			federalRapbackSubscriptionErrorId = jdbcTemplate.queryForObject(federalRapbackSubscriptionErrorStatement, Integer.class, stateSubscriptionId);
		} catch (DataAccessException e) {
			log.error("No federal rapback subscription association with this state subscription id: "  + stateSubscriptionId);
		}
		
		return federalRapbackSubscriptionErrorId;	
	}
	
	@Override
	public List<UserAcknowledgement> retrieveUserAcknowledgement(
			String federationId) {
		final String USER_ACK_SELECT="SELECT * FROM USER_ACKNOWLEDGEMENT ua, USER_INFO ui where ui.USER_INFO_ID = ua.USER_INFO_ID" +
									 " and ui.FEDERATION_ID=?";
		
		List<UserAcknowledgement> userAcknowledgements = jdbcTemplate.query(USER_ACK_SELECT, new UserAcknowledgementsRowMapper(), federationId);
		
		return userAcknowledgements;	
	}
	

	@Override
	public Integer saveFederalRapbackSubscriptionError(
			Integer federalSubcriptionId, String stateSubscriptionId) {
		log.debug("Inserting row into FEDERAL_RAPBACK_SUBSCRIPTION_ERRORS table for state subscription: " + stateSubscriptionId);
		
        final String RAPBACK_SUBSCRIPTION_ERROR="INSERT into FEDERAL_RAPBACK_SUBSCRIPTION_ERRORS "
        		+ "(FEDERAL_RAPBACK_SUBSCRIPTION_ID, ERROR_REPORTED, STATE_SUBSCRIPTION_ID, ERROR_RESOLVED) "
        		+ "values (?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(RAPBACK_SUBSCRIPTION_ERROR, new String[] {"FEDERAL_RAPBACK_SUBSCRIPTION_ERRORS_ID"});
        	            DaoUtils.setPreparedStatementVariable(federalSubcriptionId, ps, 1);
        	            DaoUtils.setPreparedStatementVariable(true, ps, 2);
        	            DaoUtils.setPreparedStatementVariable(stateSubscriptionId, ps, 3);
        	            DaoUtils.setPreparedStatementVariable(false, ps, 4);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		
    }

	@Override
	public Integer resolveFederalRapbackSubscriptionError(String stateSubscriptionId) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>(); 
		
		final String FEDERAL_SUBSCRIPTION_UPDATE="UPDATE FEDERAL_RAPBACK_SUBSCRIPTION_ERRORS SET "
				+ "ERROR_RESOLVED = :errorResolved "
				+ "WHERE STATE_SUBSCRIPTION_ID = :stateSubscriptionId "
				+ " AND ERROR_RESOLVED = false";

		paramMap.put("errorResolved", true); 
		paramMap.put("stateSubscriptionId", stateSubscriptionId); 
		
		Integer updatedRecordFk = namedParameterJdbcTemplate.update(FEDERAL_SUBSCRIPTION_UPDATE, paramMap);		
		
		return updatedRecordFk;
	}	
	
	private void addTriggeringEvents(
			List<FederalRapbackNotification> federalRapbackNotifications) {
		for (FederalRapbackNotification federalRapbackNotification : federalRapbackNotifications)
		{
			String transactionType = federalRapbackNotification.getTransactionType();
			
			if (StringUtils.isNotBlank(transactionType))
			{
				if (transactionType.equals("NOTIFICATION_MATCHING_SUBSCRIPTION"))
				{
					List<String> triggeringEvents = retrieveTriggeringEventsForNotification(federalRapbackNotification.getFederalRapbackNotificationId());
					federalRapbackNotification.setTriggeringEvents(triggeringEvents);
				}	
				
			}	
		}
	}
	


	@Override
	public List<FederalRapbackSubscription> retrieveFederalRapbackSubscriptionFromStateSubscriptionId(
			String stateSubscriptionId) {
		final String SUBSCRIPTION_SELECT="SELECT * FROM FEDERAL_RAPBACK_SUBSCRIPTION WHERE STATE_SUBSCRIPTION_ID = ? order by REQUEST_SENT_TIMESTAMP desc";
		
		List<FederalRapbackSubscription> federalRapbackSubscriptions = jdbcTemplate.query(SUBSCRIPTION_SELECT, new FederalRapbackSubscriptionRowMapper(), stateSubscriptionId);
		
		return federalRapbackSubscriptions;
	}
	
	@Override
	public List<TriggeringEvents> retrieveAllTriggeringEvents() {
		final String SELECT="SELECT * FROM TRIGGERING_EVENTS order by TRIGGERING_EVENT asc";
		
		List<TriggeringEvents> federalRapbackSubscriptions = jdbcTemplate.query(SELECT, new TriggeringEventRowMapper());
		
		return federalRapbackSubscriptions;
	}	

	@Override
	public Integer saveFederalRapbackNotification(
			FederalRapbackNotification federalRapbackNotification) {

		log.debug("Inserting row into FEDERAL_RAPBACK_NOTIFICATION table : " + federalRapbackNotification.toString());
		
        final String FEDERAL_RAPBACK_NOTIFICATION_INSERT="INSERT into FEDERAL_RAPBACK_NOTIFICATION "
        		+ "(PATH_TO_NOTIFICATION_FILE, STATE_SUBSCRIPTION_ID, RAPBACK_EVENT_TEXT, ORIGINAL_IDENTIFIER, UPDATED_IDENTIFIER, TRANSACTION_TYPE, RECORD_RAPBACK_ACTIVITY_NOTIFICATION_ID, NOTIFICATION_RECIEVED_TIMESTAMP) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(FEDERAL_RAPBACK_NOTIFICATION_INSERT, new String[] {"FEDERAL_RAPBACK_NOTIFICATION_ID"});
        	            DaoUtils.setPreparedStatementVariable(federalRapbackNotification.getPathToNotificationFile(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackNotification.getStateSubscriptionId(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackNotification.getRapBackEventText(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackNotification.getOriginalIdentifier(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackNotification.getUpdatedIdentifier(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackNotification.getTransactionType(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackNotification.getRecordRapBackActivityNotificationID(), ps, 7);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackNotification.getNotificationRecievedTimestamp(), ps, 8);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
	}	
	

	@Override
	public Integer saveFederalRapbackIdentityHistory(
			FederalRapbackIdentityHistory federalRapbackIdentityHistory) {
		log.debug("Inserting row into FEDERAL_RAPBACK_NOTIFICATION table : " + federalRapbackIdentityHistory.toString());
		
        final String FEDERAL_RAPBACK_IDENTITY_HISTORY_INSERT="INSERT into FEDERAL_RAPBACK_IDENTITY_HISTORY "
        		+ "(TRANSACTION_CATEGORY_CODE_REQUEST, REQUEST_SENT_TIMESTAMP, PATH_TO_RESPONSE_FILE, TRANSACTION_CONTROL_REFERENCE_IDENTIFICATION, PATH_TO_REQUEST_FILE, "
        		+ "RAPBACK_NOTIFICATION_ID, FBI_SUBSCRIPTION_ID, UCN) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(FEDERAL_RAPBACK_IDENTITY_HISTORY_INSERT, new String[] {"FEDERAL_RAPBACK_IDENTITY_HISTORY_ID"});
        	            DaoUtils.setPreparedStatementVariable(federalRapbackIdentityHistory.getTransactionCategoryCodeRequest(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackIdentityHistory.getRequestSentTimestamp(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackIdentityHistory.getPathToResponseFile(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackIdentityHistory.getTransactionControlReferenceIdentification(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackIdentityHistory.getPathToRequestFile(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackIdentityHistory.getFbiNotificationId(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackIdentityHistory.getFbiSubscriptionId(), ps, 7);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackIdentityHistory.getUcn(), ps, 8);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		
    }
	
	@Override
	public Integer saveSubscriptionAction(SubscriptionAction subscriptionAction) {
		final String VALIDATION_REQUEST_INSERT="INSERT into SUBSCRIPTION_ACTIONS "
        		+ "(USER_INFO_ID, STATE_SUBSCRIPTION_ID, FBI_SUBSCRIPTION_ID, START_DATE, END_DATE, VALIDATION_DUE_DATE, ACTION) "
        		+ "values (?, ?, ?, ?, ?, ?, ?)";
        

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(VALIDATION_REQUEST_INSERT, new String[] {"SUBSCRIPTION_ACTIONS_ID"});
        	            DaoUtils.setPreparedStatementVariable(subscriptionAction.getUserInfoFK(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(subscriptionAction.getStateSubscriptionId(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(subscriptionAction.getFbiSubscriptionId(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(subscriptionAction.getStartDate(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(subscriptionAction.getEndDate(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(subscriptionAction.getValidationDueDate(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(subscriptionAction.getAction(), ps, 7);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		
    }
	
	@Override
	public Integer saveTriggeringEvent(Integer federalRapbackNotificationId,
			Integer triggeringEventId) {
		log.debug("Inserting row into TRIGGERING_EVENTS_JOINER table, triggering event id: " + triggeringEventId + ", federal rapback notification id: " + federalRapbackNotificationId);
		
        final String TRIGGERING_EVENT_INSERT="INSERT into TRIGGERING_EVENTS_JOINER "
        		+ "(FEDERAL_RAPBACK_NOTIFICATION_ID, TRIGGERING_EVENTS_ID) "
        		+ "values (?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(TRIGGERING_EVENT_INSERT, new String[] {"TRIGGERING_EVENTS_JOINER_ID"});
        	            DaoUtils.setPreparedStatementVariable(federalRapbackNotificationId, ps, 1);
        	            DaoUtils.setPreparedStatementVariable(triggeringEventId, ps, 2);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
	}
	
	@Override
	public List<String> retrieveTriggeringEventsForNotification(
			Integer federalRapbackNotificationId) {
		
		final String SUBSCRIPTION_SELECT="SELECT te.TRIGGERING_EVENT FROM TRIGGERING_EVENTS te, TRIGGERING_EVENTS_JOINER tej "
				+ " WHERE te.TRIGGERING_EVENTS_ID=tej.TRIGGERING_EVENTS_ID and tej.FEDERAL_RAPBACK_NOTIFICATION_ID=?";
		
		List<String> triggeringEvents = jdbcTemplate.queryForList(SUBSCRIPTION_SELECT, String.class, federalRapbackNotificationId);
		
		return triggeringEvents;
				
	}
	

	@Override
	public Integer deleteFederalRapbackSubscriptionError(
			String stateSubscriptionId){
		
		final String DELETE_QUERY = "DELETE from FEDERAL_RAPBACK_SUBSCRIPTION_ERRORS where STATE_SUBSCRIPTION_ID = ?";
		
		int resultSize = this.jdbcTemplate.update(DELETE_QUERY, new Object[] { stateSubscriptionId });
		if (resultSize == 0)
		{
			log.error("No federal rapback subscription error found with subscription of: " + stateSubscriptionId);
		}		
		
		return resultSize;
	}
	
	private final class UserInfoRowMapper implements RowMapper<UserInfo> {
		public UserInfo mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			UserInfo userInfo = buildUserInfo(rs);
			return userInfo;
		}

		private UserInfo buildUserInfo(
				ResultSet rs) throws SQLException{

			UserInfo userInfo = new UserInfo();
			
			userInfo.setUserInfoId(rs.getInt("USER_INFO_ID"));
			userInfo.setEmployerName(rs.getString("EMPLOYER_NAME"));
			userInfo.setEmployerOri(rs.getString("EMPLOYER_ORI"));
			userInfo.setUserFirstName(rs.getString("USER_FIRST_NAME"));
			userInfo.setIdentityProviderId(rs.getString("IDENTITY_PROVIDER_ID"));
			userInfo.setUserEmailAddress(rs.getString("USER_EMAIL_ADDRESS"));
			userInfo.setUserLastName(rs.getString("USER_LAST_NAME"));
			userInfo.setEmployerSubunitName(rs.getString("EMPLOYER_SUBUNIT_NAME"));
			userInfo.setFederationId(rs.getString("FEDERATION_ID"));
			
			return userInfo;
		}
	}	
	
	private final class FederalRapbackSubscriptionRowMapper implements RowMapper<FederalRapbackSubscription> {
		public FederalRapbackSubscription mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			FederalRapbackSubscription federalRapbackSubscription = buildFederalRapbackSubscriptiont(rs);
			return federalRapbackSubscription;
		}

		private FederalRapbackSubscription buildFederalRapbackSubscriptiont(
				ResultSet rs) throws SQLException{

			FederalRapbackSubscription federalRapbackSubscription = new FederalRapbackSubscription();
			
			federalRapbackSubscription.setPathToRequestFile(rs.getString("PATH_TO_REQUEST_FILE"));
			federalRapbackSubscription.setPathToResponseFile(rs.getString("PATH_TO_RESPONSE_FILE"));
			federalRapbackSubscription.setRequestSentTimestamp(toLocalDateTime(rs.getTimestamp("REQUEST_SENT_TIMESTAMP")));
			federalRapbackSubscription.setResponseRecievedTimestamp(toLocalDateTime(rs.getTimestamp("RESPONSE_RECIEVED_TIMESTAMP")));
			federalRapbackSubscription.setTransactionCategoryCodeRequest(rs.getString("TRANSACTION_CATEGORY_CODE_REQUEST"));
			federalRapbackSubscription.setTransactionCategoryCodeResponse(rs.getString("TRANSACTION_CATEGORY_CODE_RESPONSE"));
			federalRapbackSubscription.setTransactionControlReferenceIdentification(rs.getString("TRANSACTION_CONTROL_REFERENCE_IDENTIFICATION"));
			federalRapbackSubscription.setFederalRapbackSubscriptionId(rs.getInt("FEDERAL_RAPBACK_SUBSCRIPTION_ID"));
			
			federalRapbackSubscription.setSubscriptonCategoryCode(rs.getString("SUBSCRIPTION_CATEGORY_CODE"));
			federalRapbackSubscription.setSid(rs.getString("SID"));
			federalRapbackSubscription.setTransactionStatusText(rs.getString("TRANSACTION_STATUS_TEXT"));
			federalRapbackSubscription.setStateSubscriptionId(rs.getString("STATE_SUBSCRIPTION_ID"));
			federalRapbackSubscription.setFbiSubscriptionId(rs.getString("FBI_SUBSCRIPTION_ID"));
			
			return federalRapbackSubscription;
		}
	}
	
	private final class SearchQualifierCodesRowMapper implements RowMapper<SearchQualifierCodes> {
		public SearchQualifierCodes mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			SearchQualifierCodes searchQualifierCode = buildSearchQualifierCode(rs);
			return searchQualifierCode;
		}

		private SearchQualifierCodes buildSearchQualifierCode(
				ResultSet rs) throws SQLException{

			SearchQualifierCodes searchQualifierCode = new SearchQualifierCodes();
			
			searchQualifierCode.setCodeName(rs.getString("CODE_NAME"));
			searchQualifierCode.setSearchQualifierCodesId(rs.getInt("SEARCH_QUALIFIER_CODES_ID"));
			
			return searchQualifierCode;
		}
	}
	
	private final class IdentificationSearchReasonCodeRowMapper implements RowMapper<IdentificationSearchReasonCodes> {
		public IdentificationSearchReasonCodes mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			IdentificationSearchReasonCodes identificationSearchReasonCodes = buildIdentificationSearchReasonCodes(rs);
			return identificationSearchReasonCodes;
		}

		private IdentificationSearchReasonCodes buildIdentificationSearchReasonCodes(
				ResultSet rs) throws SQLException{

			IdentificationSearchReasonCodes identificationSearchReasonCodes = new IdentificationSearchReasonCodes();
			
			identificationSearchReasonCodes.setIdentificationReasonCodeDescription(rs.getString("IDENTIFICATION_REASON_CODE_DESCRIPTION"));
			identificationSearchReasonCodes.setIdentificationSearchReasonCodeId(rs.getInt("IDENTIFICATION_SEARCH_REASON_CODE_ID"));
			
			return identificationSearchReasonCodes;
		}
	}

	private final class SubscriptionReasonCodeRowMapper implements RowMapper<SubscriptionReasonCode> {
		public SubscriptionReasonCode mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			SubscriptionReasonCode subscriptionReasonCode = buildSubscriptionReasonCode(rs);
			return subscriptionReasonCode;
		}

		private SubscriptionReasonCode buildSubscriptionReasonCode(
				ResultSet rs) throws SQLException{

			SubscriptionReasonCode subscriptionReasonCode = new SubscriptionReasonCode();
			
			subscriptionReasonCode.setSubscriptionReasonCodeId(rs.getInt("SUBSCRIPTION_REASON_CODE_ID"));
			subscriptionReasonCode.setSubscritionReasonCode(rs.getString("SUBSCRIPTION_REASON_CODE"));
			
			return subscriptionReasonCode;
		}
	}

	
	private final class SystemsToSearchRowMapper implements RowMapper<SystemsToSearch> {
		public SystemsToSearch mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			SystemsToSearch systemToSearch = buildSystemToSearch(rs);
			return systemToSearch;
		}

		private SystemsToSearch buildSystemToSearch(
				ResultSet rs) throws SQLException{

			SystemsToSearch systemsToSearch = new SystemsToSearch();
			
			systemsToSearch.setSystemName(rs.getString("SYSTEM_NAME"));
			systemsToSearch.setSystemsToSearchId(rs.getInt("SYSTEMS_TO_SEARCH_ID"));
			systemsToSearch.setSystemUri(rs.getString("SYSTEM_URI"));
			
			return systemsToSearch;
		}
	}
	
	private final class SubscriptionSearchRequestRowMapper implements RowMapper<SubscriptionSearchRequest> {
		public SubscriptionSearchRequest mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			SubscriptionSearchRequest subscriptionSearchRequest = buildSubscriptionSearchRequest(rs);
			return subscriptionSearchRequest;
		}

		private SubscriptionSearchRequest buildSubscriptionSearchRequest(
				ResultSet rs) throws SQLException{

			SubscriptionSearchRequest subscriptionSearchRequest = new SubscriptionSearchRequest();
			
			subscriptionSearchRequest.setMessageId(rs.getString("MESSAGE_ID"));
			subscriptionSearchRequest.setSubscriptionSearchRequestPk(rs.getInt("SUBSCRIPTION_SEARCH_REQUEST_ID"));
			
			return subscriptionSearchRequest;
		}
	}
	
	private final class IdentificationSearchRequestRowMapper implements RowMapper<IdentificationSearchRequest> {
		public IdentificationSearchRequest mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			IdentificationSearchRequest identificationSearchRequest = buildIdentificationSearchRequest(rs);
			return identificationSearchRequest;
		}

		private IdentificationSearchRequest buildIdentificationSearchRequest(
				ResultSet rs) throws SQLException{

			IdentificationSearchRequest identificationSearchRequest = new IdentificationSearchRequest();
			
			identificationSearchRequest.setMessageId(rs.getString("MESSAGE_ID"));
			identificationSearchRequest.setIdentificationSearchRequestId(rs.getInt("IDENTIFICATION_SEARCH_REQUEST_ID"));
			
			return identificationSearchRequest;
		}
	}
	
	private final class PersonQueryRequestRowMapper implements RowMapper<QueryRequest> {
		public QueryRequest mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			QueryRequest queryRequest = buildQueryRequest(rs);
			return queryRequest;
		}

		private QueryRequest buildQueryRequest(
				ResultSet rs) throws SQLException{

			QueryRequest queryRequest = new QueryRequest();
			
			queryRequest.setMessageId(rs.getString("MESSAGE_ID"));
			queryRequest.setQueryRequestId(rs.getInt("QUERY_REQUEST_ID"));
			
			return queryRequest;
		}
	}
	
	private final class PrintResultsRowMapper implements RowMapper<PrintResults> {
		public PrintResults mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			PrintResults printResults = buildPrintResults(rs);
			return printResults;
		}

		private PrintResults buildPrintResults(
				ResultSet rs) throws SQLException{

			PrintResults printResults = new PrintResults();
			
			printResults.setMessageId(rs.getString("MESSAGE_ID"));
			printResults.setSystemName(rs.getString("SYSTEM_NAME"));
			printResults.setDescription(rs.getString("DESCRIPTION"));
			printResults.setSid(rs.getString("SID"));
			
			UserInfo userInfo = new UserInfo();
			
			UserInfoRowMapper userInfoRowMapper = new UserInfoRowMapper();
			
			userInfo = userInfoRowMapper.buildUserInfo(rs);
			
			printResults.setUserInfo(userInfo);
			
			return printResults;
		}
	}
	
	private final class NotificationSentRowMapper implements RowMapper<NotificationSent> {
		public NotificationSent mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			NotificationSent notificationSent = buildNotificationSent(rs);
			return notificationSent;
		}

		private NotificationSent buildNotificationSent(
				ResultSet rs) throws SQLException{

			NotificationSent notificationSent = new NotificationSent();
			
			notificationSent.setNotificationSentId(rs.getInt("NOTIFICATIONS_SENT_ID"));
			notificationSent.setNotificationSentTimestamp(toLocalDateTime(rs.getTimestamp("TIMESTAMP")));
			notificationSent.setSubscriptionType(rs.getString("SUBSCRIPTION_TYPE"));
			notificationSent.setTopic(rs.getString("TOPIC"));
			notificationSent.setSubscriptionIdentifier(rs.getString("SUBSCRIPTION_IDENTIFIER"));
			notificationSent.setSubscriptionOwner(rs.getString("SUBSCRIPTION_OWNER"));
			notificationSent.setSubscriptionOwnerAgencyType(rs.getString("SUBSCRIPTION_OWNER_AGENCY_TYPE"));
			notificationSent.setSubscriptionOwnerEmailAddress(rs.getString("SUBSCRIPTION_OWNER_EMAIL_ADDRESS"));
			notificationSent.setNotifyingSystemName(rs.getString("NOTIFYING_SYSTEM_NAME"));
			notificationSent.setSubscribingSystemIdentifier(rs.getString("SUBSCRIBING_SYSTEM_IDENTIFIER"));
			notificationSent.setSubscriptionSubject(rs.getString("SUBSCRIPTION_SUBJECT"));
			notificationSent.setSubscriptionOwnerAgency(rs.getString("SUBSCRIPTION_OWNER_AGENCY"));
			
			return notificationSent;
		}
	}
	
	private final class FederalRapbackNotificationRowMapper implements RowMapper<FederalRapbackNotification> {
		public FederalRapbackNotification mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			FederalRapbackNotification federalRapbackNotification = buildFederalRapbackNotification(rs);
			return federalRapbackNotification;
		}

		private FederalRapbackNotification buildFederalRapbackNotification(
				ResultSet rs) throws SQLException{

			FederalRapbackNotification federalRapbackNotification = new FederalRapbackNotification();
			
			federalRapbackNotification.setFederalRapbackNotificationId(rs.getInt("FEDERAL_RAPBACK_NOTIFICATION_ID"));
			federalRapbackNotification.setNotificationRecievedTimestamp(toLocalDateTime(rs.getTimestamp("NOTIFICATION_RECIEVED_TIMESTAMP")));
			federalRapbackNotification.setOriginalIdentifier(rs.getString("ORIGINAL_IDENTIFIER"));
			federalRapbackNotification.setUpdatedIdentifier(rs.getString("UPDATED_IDENTIFIER"));
			federalRapbackNotification.setTransactionType(rs.getString("TRANSACTION_TYPE"));
			federalRapbackNotification.setPathToNotificationFile(rs.getString("PATH_TO_NOTIFICATION_FILE"));
			federalRapbackNotification.setRapBackEventText(rs.getString("RAPBACK_EVENT_TEXT"));
			federalRapbackNotification.setStateSubscriptionId(rs.getString("STATE_SUBSCRIPTION_ID"));
			federalRapbackNotification.setRecordRapBackActivityNotificationID(rs.getString("RECORD_RAPBACK_ACTIVITY_NOTIFICATION_ID"));
			
			return federalRapbackNotification;
		}
	}	
	
	private final class TriggeringEventRowMapper implements RowMapper<TriggeringEvents> {
		public TriggeringEvents mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			TriggeringEvents TriggeringEvent = buildTriggeringEvents(rs);
			return TriggeringEvent;
		}

		private TriggeringEvents buildTriggeringEvents(
				ResultSet rs) throws SQLException{

			TriggeringEvents triggeringEvent = new TriggeringEvents();
			
			triggeringEvent.setTriggeringEvent(rs.getString("TRIGGERING_EVENT"));
			triggeringEvent.setTriggeringEventsId(rs.getInt("TRIGGERING_EVENTS_ID"));
			
			return triggeringEvent;
		}
	}		
	
	private final class UserAcknowledgementsRowMapper implements RowMapper<UserAcknowledgement> {
		public UserAcknowledgement mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			UserAcknowledgement userAcknowledgement = buildUserAcknowledgement(rs);
			return userAcknowledgement;
		}

		private UserAcknowledgement buildUserAcknowledgement(
				ResultSet rs) throws SQLException{

			UserAcknowledgement userAcknowledgement = new UserAcknowledgement();
			
			userAcknowledgement.setSid(rs.getString("SID"));
			userAcknowledgement.setDecisionDateTime(toLocalDateTime(rs.getTimestamp("DECISION_TIMESTAMP")));
			userAcknowledgement.setLastUpdatedTime(toLocalDateTime(rs.getTimestamp("TIMESTAMP")));
			userAcknowledgement.setUserAcknowledgementId(rs.getInt("USER_ACKNOWLEDGEMENT_ID"));
			userAcknowledgement.setDecision(rs.getBoolean("DECISION"));
			
			UserInfo userInfo = new UserInfo();
			
			UserInfoRowMapper userInfoRowMapper = new UserInfoRowMapper();
			
			userInfo = userInfoRowMapper.buildUserInfo(rs);

			userAcknowledgement.setUserInfo(userInfo);
			
			return userAcknowledgement;
		}
	}
	
	private final class FederalRapbackRenewalNotificationRowMapper implements RowMapper<FederalRapbackRenewalNotification> {
		public FederalRapbackRenewalNotification mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			FederalRapbackRenewalNotification federalRapbackRenewalNotification = buildFederalRapbackRenewalNotification(rs);
			return federalRapbackRenewalNotification;
		}

		private FederalRapbackRenewalNotification buildFederalRapbackRenewalNotification(
				ResultSet rs) throws SQLException{

			FederalRapbackRenewalNotification federalRapbackRenewalNotification = new FederalRapbackRenewalNotification();
			
			federalRapbackRenewalNotification.setPersonDob(toLocalDate(rs.getDate("PERSON_DOB")));
			federalRapbackRenewalNotification.setRapbackExpirationDate(toLocalDate(rs.getDate("RAPBACK_EXPIRATION_DATE")));
			federalRapbackRenewalNotification.setStateSubscriptionId(rs.getString("STATE_SUBSCRIPTION_ID"));
			federalRapbackRenewalNotification.setUcn(rs.getString("UCN"));
			federalRapbackRenewalNotification.setPersonFirstName(rs.getString("PERSON_FIRST_NAME"));
			federalRapbackRenewalNotification.setPersonMiddleName(rs.getString("PERSON_MIDDLE_NAME"));
			federalRapbackRenewalNotification.setPersonLastName(rs.getString("PERSON_LAST_NAME"));
			federalRapbackRenewalNotification.setRecordControllingAgency(rs.getString("RECORD_CONTROLLING_AGENCY"));
			federalRapbackRenewalNotification.setTransactionStatusText(rs.getString("TRANSACTION_STATUS_TEXT"));
			federalRapbackRenewalNotification.setSid(rs.getString("SID"));
			federalRapbackRenewalNotification.setNotificationRecievedTimestamp(toLocalDateTime(rs.getTimestamp("NOTIFICATION_RECIEVED_TIMESTAMP")));
			federalRapbackRenewalNotification.setStateSubscriptionId(rs.getString("STATE_SUBSCRIPTION_ID"));
			federalRapbackRenewalNotification.setPathToNotificationFile(rs.getString("PATH_TO_NOTIFICATION_FILE"));
			
			return federalRapbackRenewalNotification;
		}
	}		
	
	
	private LocalDate toLocalDate(Date date){
		return date == null? null : date.toLocalDate();
	}
	
	private LocalDateTime toLocalDateTime(Timestamp timestamp){
		return timestamp == null? null : timestamp.toLocalDateTime();
	}
	
    public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
    	return (locDateTime == null ? null : Timestamp.valueOf(locDateTime));
    }

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public void setNamedParameterJdbcTemplate(
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public Integer saveFirearmsSearchRequest(
			FirearmsSearchRequest firearmsSearchRequest) {

		log.debug("Inserting rows into FIREARMS_SEARCH_REQUEST table : " + firearmsSearchRequest.toString());
		
        final String FIREARMS_SEARCH_REQUEST_INSERT="INSERT into FIREARMS_SEARCH_REQUEST "
        		+ "(SERIAL_NUMBER, SERIAL_NUMBER_QUALIFIER_CODE_ID, MAKE, MODEL, REGISTRATION_NUMBER, CURRENT_REGISTRATIONS_ONLY, FIREARMS_TYPE, USER_INFO_ID, PURPOSE, ON_BEHALF_OF, MESSAGE_ID) "
        		+ "values (?,?,?,?,?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(FIREARMS_SEARCH_REQUEST_INSERT, new String[] {"FIREARMS_SEARCH_REQUEST_ID"});
        	            DaoUtils.setPreparedStatementVariable(firearmsSearchRequest.getSerialNumber(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(firearmsSearchRequest.getSerialNumberQualifierCodeId(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(firearmsSearchRequest.getMake(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(firearmsSearchRequest.getModel(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(firearmsSearchRequest.getRegistrationNumber(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(firearmsSearchRequest.isCurrentRegistrationsOnly(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(firearmsSearchRequest.getFirearmsType(), ps, 7);
        	            DaoUtils.setPreparedStatementVariable(firearmsSearchRequest.getUserInfofk(), ps, 8);
        	            DaoUtils.setPreparedStatementVariable(firearmsSearchRequest.getPurpose(), ps, 9);
        	            DaoUtils.setPreparedStatementVariable(firearmsSearchRequest.getOnBehalfOf(), ps, 10);
        	            DaoUtils.setPreparedStatementVariable(firearmsSearchRequest.getMessageId(), ps, 11);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	  	

	}

	@Override
	public Integer saveFirearmsSystemToSearch(Integer firearmsSearchPk,
			Integer systemsToSearchPk) {
        log.debug("Inserting rows into FIREARMS_SYSTEMS_TO_SEARCH table : " + systemsToSearchPk.toString());
        
        final String FIREARMS_SYSTEMS_TO_SEARCH_INSERT="INSERT into FIREARMS_SYSTEMS_TO_SEARCH "
        		+ "(FIREARMS_SEARCH_REQUEST_ID, SYSTEMS_TO_SEARCH_ID) "
        		+ "values (?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(FIREARMS_SYSTEMS_TO_SEARCH_INSERT, new String[] {"USER_INFO_ID"});
        	            DaoUtils.setPreparedStatementVariable(firearmsSearchPk, ps, 1);
        	            DaoUtils.setPreparedStatementVariable(systemsToSearchPk, ps, 2);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	  	
    }

	@Override
	public Integer saveSubscriptionCategoryCodes(
			Integer subscriptionSearchRequestPk,
			Integer subscriptionReasonCodePk) {
        log.debug("Inserting rows into SUBSCRIPTION_REASON_CODE_JOINER table : " + subscriptionSearchRequestPk.toString());
        
        final String FIREARMS_SYSTEMS_TO_SEARCH_INSERT="INSERT into SUBSCRIPTION_REASON_CODE_JOINER "
        		+ "(SUBSCRIPTION_SEARCH_REQUEST_ID, SUBSCRIPTION_REASON_CODE_ID) "
        		+ "values (?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(FIREARMS_SYSTEMS_TO_SEARCH_INSERT, new String[] {"USER_INFO_ID"});
        	            DaoUtils.setPreparedStatementVariable(subscriptionSearchRequestPk, ps, 1);
        	            DaoUtils.setPreparedStatementVariable(subscriptionReasonCodePk, ps, 2);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	  	
     }

	@Override
	public Integer saveSubscriptionQueryResponse(
			SubscriptionQueryResponse subscriptionQueryResponse) {

        log.debug("Inserting row into SUBSCRIPTION_QUERY_RESULTS table : " + subscriptionQueryResponse);
        
        final String SUBSCRIPTION_QUERY_RESULTS_INSERT="INSERT into SUBSCRIPTION_QUERY_RESULTS "
        		+ "(QUERY_RESULTS_ERROR_INDICATOR, QUERY_REQUEST_ID, SYSTEM_NAME, QUERY_RESULTS_ACCESS_DENIED, QUERY_RESULTS_ERROR_TEXT, FBI_SUBSCRIPTION_ID, MESSAGE_ID, QUERY_RESULTS_TIMEOUT_INDICATOR, SUBSCRIPTION_QUALIFIER_ID)"
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(SUBSCRIPTION_QUERY_RESULTS_INSERT, new String[] {"SUBSCRIPTION_QUERY_RESULTS_ID"});
        	            DaoUtils.setPreparedStatementVariable(subscriptionQueryResponse.getQueryResultsErrorIndicator(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(subscriptionQueryResponse.getQueryRequestId(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(subscriptionQueryResponse.getSystemName(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(subscriptionQueryResponse.getQueryResultsAccessDeniedIndicator(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(subscriptionQueryResponse.getQueryResultsErrorText(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(subscriptionQueryResponse.getFbiSubscriptionId(), ps, 6);
        	            DaoUtils.setPreparedStatementVariable(subscriptionQueryResponse.getMessageId(), ps, 7);
        	            DaoUtils.setPreparedStatementVariable(subscriptionQueryResponse.getQueryResultsTimeoutIndicator(), ps, 8);
        	            DaoUtils.setPreparedStatementVariable(subscriptionQueryResponse.getSubscriptionQualifierId(), ps, 9);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
	}

	@Override
	public List<UserAuthenticationSearchResponse> retrieveUserAuthentication(UserAuthenticationSearchRequest searchRequest) {

		if (searchRequest.getStartTime() != null && searchRequest.getEndTime() == null)
		{
			searchRequest.setEndTime(searchRequest.getStartTime().plusDays(1));
		}	
		
		StringBuffer sqlStatement = new StringBuffer();
		
		sqlStatement.append("select * from USER_LOGIN ul, USER_INFO ui where ui.USER_INFO_ID = ul.USER_INFO_ID ");
		
		if (StringUtils.isNotBlank(searchRequest.getFirstName()))
		{
			sqlStatement.append(" and ui.USER_FIRST_NAME = ?");
		}	

		if (StringUtils.isNotBlank(searchRequest.getLastName()))
		{
			sqlStatement.append(" and ui.USER_LAST_NAME = ?");
		}	

		if (StringUtils.isNotBlank(searchRequest.getEmailAddress()))
		{
			sqlStatement.append(" and ui.USER_EMAIL_ADDRESS = ?");
		}	

		if (StringUtils.isNotBlank(searchRequest.getEmployerOri()))
		{
			sqlStatement.append(" and ui.EMPLOYER_ORI = ?");
		}	

		setUserAuditSearchDateConstraints(searchRequest, sqlStatement, "ul");
		
		log.info("User authentication select statement: " + sqlStatement.toString());
		
		List<UserAuthenticationSearchResponse> userAuthenticationSearchResponses = jdbcTemplate.query(sqlStatement.toString(), new UserAuthenticationResposeRowMapper());
		
		return userAuthenticationSearchResponses;
	}

	private void setUserAuditSearchDateConstraints(
			AuditSearchRequest searchRequest,
			StringBuffer sqlStatement, String alias) {
		if (searchRequest.getStartTime() != null && searchRequest.getEndTime() != null)
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			if (searchRequest.getStartTime() != null)
			{
				
				String startDateString = searchRequest.getStartTime().format(formatter);
				sqlStatement.append(" and " + alias + ".TIMESTAMP > '" + startDateString + "'");
			}	
	
			if (searchRequest.getEndTime() != null)
			{
				String endDateString = searchRequest.getEndTime().format(formatter);
				sqlStatement.append(" and " + alias + ".TIMESTAMP < '" + endDateString + "'");
				
			}	
		}
	}

	@Override
	public List<UserInfo> retrieveAllUsers() {
		final String USER_INFO_SELECT = "SELECT * FROM USER_INFO order by USER_LAST_NAME asc";

		List<UserInfo> userInfoList = jdbcTemplate.query(USER_INFO_SELECT,
				new UserInfoRowMapper());

		return userInfoList;
	}

	@Override
	public List<PersonSearchRequest> retrievePersonSearchRequest(
			AuditSearchRequest searchRequest) {
		
		StringBuffer sqlStatement = new StringBuffer(); 
		
		sqlStatement.append("select psr.*, sss.SYSTEM_NAME from person_search_request psr, PERSON_SYSTEMS_TO_SEARCH pss, SYSTEMS_TO_SEARCH sss where psr.PERSON_SEARCH_REQUEST_ID = pss.PERSON_SEARCH_REQUEST_ID ");
		sqlStatement.append(" and sss.SYSTEMS_TO_SEARCH_ID  = pss.SYSTEMS_TO_SEARCH_ID ");
		
		if (searchRequest.getStartTime() != null && searchRequest.getEndTime() == null)
		{
			searchRequest.setEndTime(searchRequest.getStartTime().plusDays(1));
		}	
		
		setUserAuditSearchDateConstraints(searchRequest, sqlStatement, "psr");
		
		sqlStatement.append(" order by timestamp desc");
		
		log.info(sqlStatement.toString());
		
		List<PersonSearchRequest> personSearchRequests = jdbcTemplate.query(sqlStatement.toString(), new PersonSearchRequestRowMapper());
		
		return personSearchRequests;
	}

	@Override
	public List<FirearmsSearchRequest> retrieveFirearmSearchRequest(
			AuditSearchRequest searchRequest) {
		StringBuffer sqlStatement = new StringBuffer(); 
		
		sqlStatement.append("select fsr.*, sss.SYSTEM_NAME from firearms_search_request fsr, FIREARMS_SYSTEMS_TO_SEARCH fss, SYSTEMS_TO_SEARCH sss where fsr.FIREARMS_SEARCH_REQUEST_ID = fss.FIREARMS_SEARCH_REQUEST_ID ");
		sqlStatement.append(" and sss.SYSTEMS_TO_SEARCH_ID  = fss.SYSTEMS_TO_SEARCH_ID ");
		
		if (searchRequest.getStartTime() != null && searchRequest.getEndTime() == null)
		{
			searchRequest.setEndTime(searchRequest.getStartTime().plusDays(1));
		}	
		
		setUserAuditSearchDateConstraints(searchRequest, sqlStatement, "fsr");
		
		sqlStatement.append(" order by timestamp desc limit 500");
		
		log.info(sqlStatement.toString());
		
		List<FirearmsSearchRequest> firearmsSearchRequests = jdbcTemplate.query(sqlStatement.toString(), new FirearmSearchRequestRowMapper());
		
		return firearmsSearchRequests;
	}

	@Override
	public List<VehicleSearchRequest> retrieveVehicleSearchRequest(
			AuditSearchRequest vehicleSearchRequest) {
		StringBuffer sqlStatement = new StringBuffer(); 
		
		sqlStatement.append("select vsr.*, sss.SYSTEM_NAME from vehicle_search_request vsr, VEHICLE_SYSTEMS_TO_SEARCH vss, SYSTEMS_TO_SEARCH sss where vsr.VEHICLE_SEARCH_REQUEST_ID = vss.VEHICLE_SEARCH_REQUEST_ID ");
		sqlStatement.append(" and sss.SYSTEMS_TO_SEARCH_ID  = vss.SYSTEMS_TO_SEARCH_ID ");
		
		if (vehicleSearchRequest.getStartTime() != null && vehicleSearchRequest.getEndTime() == null)
		{
			vehicleSearchRequest.setEndTime(vehicleSearchRequest.getStartTime().plusDays(1));
		}	
		
		setUserAuditSearchDateConstraints(vehicleSearchRequest, sqlStatement, "vsr");
		
		sqlStatement.append(" order by vsr.timestamp desc limit 500");
		
		log.info(sqlStatement.toString());
		
		List<VehicleSearchRequest> vehicleSearchRequests = jdbcTemplate.query(sqlStatement.toString(), new VehicleSearchRequestRowMapper());
		
		return vehicleSearchRequests;
	}

	@Override
	public List<IncidentSearchRequest> retrieveIncidentSearchRequest(
			AuditSearchRequest incidentAuditSearchRequest) {
		StringBuffer sqlStatement = new StringBuffer(); 
		
		sqlStatement.append("select isr.*, sss.SYSTEM_NAME from INCIDENT_SEARCH_REQUEST isr, INCIDENT_SYSTEMS_TO_SEARCH iss, SYSTEMS_TO_SEARCH sss where isr.INCIDENT_SEARCH_REQUEST_ID = iss.INCIDENT_SEARCH_REQUEST_ID ");
		sqlStatement.append(" and sss.SYSTEMS_TO_SEARCH_ID  = iss.SYSTEMS_TO_SEARCH_ID ");
		
		if (incidentAuditSearchRequest.getStartTime() != null && incidentAuditSearchRequest.getEndTime() == null)
		{
			incidentAuditSearchRequest.setEndTime(incidentAuditSearchRequest.getStartTime().plusDays(1));
		}	
		
		setUserAuditSearchDateConstraints(incidentAuditSearchRequest, sqlStatement, "isr");
		
		sqlStatement.append(" order by isr.timestamp desc limit 500");
		
		log.info(sqlStatement.toString());
		
		List<IncidentSearchRequest> incidentSearchRequests = jdbcTemplate.query(sqlStatement.toString(), new IncidentSearchRequestRowMapper());
		
		return incidentSearchRequests;	
	}

	@Override
	public Integer saveVehicleQueryCrashResponse(VehicleCrashQueryResponse vehicleCrashQueryResponse) {
		log.debug("Inserting row into VEHICLE_CRASH_QUERY_RESULTS table : " + vehicleCrashQueryResponse.toString());
		
        final String VEHICLE_CRASH_QUERY_RESULTS_INSERT="INSERT into VEHICLE_CRASH_QUERY_RESULTS "  
        		+ "(QUERY_REQUEST_ID, QUERY_RESULTS_ERROR_TEXT, QUERY_RESULTS_TIMEOUT_INDICATOR,QUERY_RESULTS_ERROR_INDICATOR,SYSTEM_NAME,MESSAGE_ID) "
        		+ "values (?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(VEHICLE_CRASH_QUERY_RESULTS_INSERT, new String[] {"VEHICLE_CRASH_QUERY_RESULTS_ID"});
        	            DaoUtils.setPreparedStatementVariable(vehicleCrashQueryResponse.getQueryRequestId(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(vehicleCrashQueryResponse.getQueryResultsErrorText(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(vehicleCrashQueryResponse.isQueryResultsTimeoutIndicator(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(vehicleCrashQueryResponse.isQueryResultsErrorIndicator(), ps, 4);
        	            DaoUtils.setPreparedStatementVariable(vehicleCrashQueryResponse.getSystemName(), ps, 5);
        	            DaoUtils.setPreparedStatementVariable(vehicleCrashQueryResponse.getMessageId(), ps, 6);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	 	
		
     }

	@Override
	public Integer saveCrashVehicle(CrashVehicle crashVehicle) {
		log.debug("Inserting row into CRASH_VEHICLE table : " + crashVehicle.toString());
		
        final String CRASH_VEHICLE_INSERT="INSERT into CRASH_VEHICLE "  
        		+ "(VEHICLE_CRASH_QUERY_RESULTS_ID, VEHICLE_MAKE, VEHICLE_MODEL, VEHICLE_IDENTIFICATION_NUMBER) "
        		+ "values (?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(CRASH_VEHICLE_INSERT, new String[] {"CRASH_VEHICLE_ID"});
        	            DaoUtils.setPreparedStatementVariable(crashVehicle.getVehicleCrashQueryResultsId(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(crashVehicle.getVehicleMake(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(crashVehicle.getVehicleModel(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(crashVehicle.getVehicleIdentificationNumber(), ps, 4);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		}

}

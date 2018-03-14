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
import org.ojbc.audit.enhanced.dao.model.FederalRapbackNotification;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
import org.ojbc.audit.enhanced.dao.model.FirearmsQueryResponse;
import org.ojbc.audit.enhanced.dao.model.IdentificationQueryResponse;
import org.ojbc.audit.enhanced.dao.model.IdentificationSearchReasonCodes;
import org.ojbc.audit.enhanced.dao.model.IdentificationSearchRequest;
import org.ojbc.audit.enhanced.dao.model.IdentificationSearchResult;
import org.ojbc.audit.enhanced.dao.model.PersonQueryCriminalHistoryResponse;
import org.ojbc.audit.enhanced.dao.model.PersonQueryWarrantResponse;
import org.ojbc.audit.enhanced.dao.model.PersonSearchRequest;
import org.ojbc.audit.enhanced.dao.model.PersonSearchResult;
import org.ojbc.audit.enhanced.dao.model.PrintResults;
import org.ojbc.audit.enhanced.dao.model.QueryRequest;
import org.ojbc.audit.enhanced.dao.model.SearchQualifierCodes;
import org.ojbc.audit.enhanced.dao.model.SystemsToSearch;
import org.ojbc.audit.enhanced.dao.model.TriggeringEvents;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.ojbc.util.helper.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
		
		final String PRINT_RESULTS_SELECT="SELECT * FROM PRINT_RESULTS WHERE MESSAGE_ID = ?";
		
		List<PrintResults> printResults = jdbcTemplate.query(PRINT_RESULTS_SELECT, new PrintResultsRowMapper(), messageId);
		return DataAccessUtils.singleResult(printResults);		
	}

	@Override
	public Integer saveFederalRapbackSubscription(
			FederalRapbackSubscription federalRapbackSubscription) {
        log.debug("Inserting row into FEDERAL_RAPBACK_SUBSCRIPTION table : " + federalRapbackSubscription);
        
        final String FEDERAL_RAPBACK_SUBSCRIPTION_INSERT="INSERT into FEDERAL_RAPBACK_SUBSCRIPTION "
        		+ "(REQUEST_SENT_TIMESTAMP, TRANSACTION_CONTROL_REFERENCE_IDENTIFICATION, PATH_TO_REQUEST_FILE,SUBSCRIPTION_CATEGORY_CODE, SID, TRANSACTION_STATUS_TEXT, STATE_SUBSCRIPTION_ID, FBI_SUBSCRIPTION_ID, TRANSACTION_CATEGORY_CODE_REQUEST) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        

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
        	            DaoUtils.setPreparedStatementVariable(federalRapbackSubscription.getFbiSubscriptionId(), ps, 8);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackSubscription.getTransactionCategoryCodeRequest(), ps, 9);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
    }
	
	@Override
	public Integer savePrintResults(PrintResults printResults) {
        log.debug("Inserting row into PRINT_RESULTS table : " + printResults.toString());
        
        final String PRINT_RESULTS_INSERT="INSERT into PRINT_RESULTS "
        		+ "(SYSTEM_NAME, DESCRIPTION, MESSAGE_ID) "
        		+ "values (?, ?, ?)";
        

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(PRINT_RESULTS_INSERT, new String[] {"PRINT_RESULTS_ID"});
        	            DaoUtils.setPreparedStatementVariable(printResults.getSystemName(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(printResults.getDescription(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(printResults.getMessageId(), ps, 3);
        	            
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
	public Integer saveUserInfo(UserInfo userInfo) {
		
        log.debug("Inserting row into USER_INFO table : " + userInfo);
        
        final String USER_INFO_INSERT="INSERT into USER_INFO "  
        		+ "(USER_FIRST_NAME,IDENTITY_PROVIDER_ID,EMPLOYER_NAME,USER_EMAIL_ADDRESS,USER_LAST_NAME,EMPLOYER_SUBUNIT_NAME,FEDERATION_ID) "
        		+ "values (?, ?, ?, ?, ?, ?, ?)";
        
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
		
        final String FIREARMS_QUERY_RESULTS_INSERT="INSERT into CRIMINAL_HISTORY_QUERY_RESULTS "  
        		+ "(FIRST_NAME, MIDDLE_NAME, LAST_NAME, SID, FBI_ID, QUERY_REQUEST_ID, QUERY_RESULTS_ERROR_TEXT, QUERY_RESULTS_TIMEOUT_INDICATOR,QUERY_RESULTS_ERROR_INDICATOR,SYSTEM_NAME,MESSAGE_ID) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(FIREARMS_QUERY_RESULTS_INSERT, new String[] {"CRIMINAL_HISTORY_QUERY_RESULTS_ID"});
        	            //DaoUtils.setPreparedStatementVariable(firearmsQueryResponse.getFirstName(), ps, 1);
        	            
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
        		+ "(QUERY_REQUEST_ID, PERSON_FIRST_NAME, PERSON_MIDDLE_NAME,PERSON_LAST_NAME,OCA,SID,FBI_ID,ID_DATE,OTN) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
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
        
        final String FEDERAL_RAPBACK_SUBSCRIPTION_INSERT="INSERT into PERSON_SEARCH_REQUEST "
        		+ "(DOB_START_DATE, RACE, EYE_COLOR,HAIR_COLOR,DRIVERS_LICENSE_NUMBER,DOB_END_DATE,ON_BEHALF_OF,FIRST_NAME_QUALIFIER_CODE_ID,SID,MIDDLE_NAME,"
        		+ "LAST_NAME_QUALIFIER_CODE_ID,PURPOSE,LAST_NAME,FIRST_NAME,GENDER,MESSAGE_ID,USER_INFO_ID,DRIVERS_LICENSE_ISSUER,FBI_ID, SSN, HEIGHT, HEIGHT_MIN, HEIGHT_MAX) "
        		+ "values (?, ?, ?, ?, ?, ?,?, ?, ?,?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?,?,?)";
        

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(FEDERAL_RAPBACK_SUBSCRIPTION_INSERT, new String[] {"PERSON_SEARCH_REQUEST_ID"});
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
				+ "TRANSACTION_STATUS_TEXT = :transactionStatusText "
				+ "WHERE FEDERAL_RAPBACK_SUBSCRIPTION_ID = :federalRapbackSubscriptionId";

		paramMap.put("transactionCategoryCodeResponse", federalRapbackSubscription.getTransactionCategoryCodeResponse()); 
		paramMap.put("responseRecievedTimestamp", convertToDatabaseColumn(federalRapbackSubscription.getResponseRecievedTimestamp())); 
		paramMap.put("pathToResponseFile", federalRapbackSubscription.getPathToResponseFile()); 
		paramMap.put("federalRapbackSubscriptionId", federalRapbackSubscription.getFederalRapbackSubscriptionId()); 
		paramMap.put("transactionStatusText", federalRapbackSubscription.getTransactionStatusText());
		
		namedParameterJdbcTemplate.update(FEDERAL_SUBSCRIPTION_UPDATE, paramMap);

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
		final String SUBSCRIPTION_SELECT="SELECT * FROM PERSON_SEARCH_REQUEST WHERE MESSAGE_ID = ?";
		
		Integer ret = null;
		
		List<PersonSearchRequest> personSearchRequests = jdbcTemplate.query(SUBSCRIPTION_SELECT, new PersonSearchRequestRowMapper(), messageId);
		
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
	public List<FederalRapbackNotification> retrieveFederalNotificationsBySubscriptionId(
			String subscriptionId) {
		String notificationSelectStatement ="SELECT * FROM FEDERAL_RAPBACK_NOTIFICATION WHERE STATE_SUBSCRIPTION_ID = ? order by NOTIFICATION_RECIEVED_TIMESTAMP desc";
		
		log.info("Retrieve Federal Notifications SQL by state subscription ID: " + subscriptionId);
		
		List<FederalRapbackNotification> federalRapbackNotifications = jdbcTemplate.query(notificationSelectStatement, new FederalRapbackNotificationRowMapper(), subscriptionId);
		
		addTriggeringEvents(federalRapbackNotifications);	
		
		return federalRapbackNotifications;
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
        		+ "(PATH_TO_NOTIFICATION_FILE, STATE_SUBSCRIPTION_ID, RAPBACK_EVENT_TEXT, ORIGINAL_IDENTIFIER, UPDATED_IDENTIFIER, TRANSACTION_TYPE, NOTIFICATION_RECIEVED_TIMESTAMP) "
        		+ "values (?, ?, ?, ?, ?, ?, ?)";
        
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
        	            DaoUtils.setPreparedStatementVariable(federalRapbackNotification.getNotificationRecievedTimestamp(), ps, 7);
        	            
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
			userInfo.setUserFirstName(rs.getString("USER_FIRST_NAME"));
			userInfo.setIdentityProviderId(rs.getString("IDENTITY_PROVIDER_ID"));
			//userInfo.setu(rs.getString("USER_INFO_ID"));
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
	
	private final class PersonSearchRequestRowMapper implements RowMapper<PersonSearchRequest> {
		public PersonSearchRequest mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			PersonSearchRequest personSearchRequest = buildPersonSearchRequest(rs);
			return personSearchRequest;
		}

		private PersonSearchRequest buildPersonSearchRequest(
				ResultSet rs) throws SQLException{

			PersonSearchRequest personSearchRequest = new PersonSearchRequest();
			
			personSearchRequest.setMessageId(rs.getString("MESSAGE_ID"));
			personSearchRequest.setPersonSearchRequestID(rs.getInt("PERSON_SEARCH_REQUEST_ID"));
			
			return personSearchRequest;
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
			
			return printResults;
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

}

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
package org.ojbc.intermediaries.sn.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.Header;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.ojbc.intermediaries.sn.notification.NotificationConstants;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.intermediaries.sn.util.NotificationBrokerUtils;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * Interface to persisted subscription information...that is, a database.  Each method on this class accepts parameters and then retrieves matching
 * subscriptions from persistent storage and returns DAOs to the caller.
 *
 */
public class SubscriptionSearchQueryDAO {

    private static final String CIVIL_SUBSCRIPTION_REASON_CODE = "I";

	private static final String BASE_QUERY_STRING = "SELECT s.id, s.topic, s.startDate, s.endDate, s.lastValidationDate, "
			+ "s.subscribingSystemIdentifier, s.subscriptionOwner, s.subscriptionOwnerEmailAddress, s.subjectName, "
			+ "si.identifierName, s.subscription_category_code, s.agency_case_number, s.ori, "
			+ "si.identifierValue, nm.notificationAddress, "
			+ "nm.notificationMechanismType, fs.* "
			+ "FROM subscription s LEFT JOIN fbi_rap_back_subscription fs ON fs.subscription_id = s.id, "
			+ "		notification_mechanism nm, "
			+ "		subscription_subject_identifier si "
			+ "WHERE nm.subscriptionId = s.id and si.subscriptionId = s.id ";
	
    private static final DateTimeFormatter DATE_FORMATTER_YYYY_MM_DD = DateTimeFormat.forPattern("yyyy-MM-dd");
    
    private static final Log log = LogFactory.getLog(SubscriptionSearchQueryDAO.class);

    private JdbcTemplate jdbcTemplate;
    private SubscriptionResultsSetExtractor resultSetExtractor;
    private boolean baseNotificationsOnEventDate = true;
    private boolean fbiSubscriptionMember = false;
    
    public SubscriptionSearchQueryDAO() {
        resultSetExtractor = new SubscriptionResultsSetExtractor();
        setValidationDueDateStrategy(new DefaultValidationDueDateStrategy());
        setGracePeriodStrategy(new DefaultGracePeriodStrategy());
        setValidationExemptionFilter(new DefaultValidationExemptionFilter());
    }

    public void setValidationExemptionFilter(ValidationExemptionFilter validationExemptionFilter) {
        resultSetExtractor.setValidationExemptionFilter(validationExemptionFilter);
    }

    public void setValidationDueDateStrategy(ValidationDueDateStrategy validationDueDateStrategy) {
        resultSetExtractor.setValidationDueDateStrategy(validationDueDateStrategy);
    }
    
    ValidationDueDateStrategy getValidationDueDateStrategy() {
        return resultSetExtractor.getValidationDueDateStrategy();
    }

    public void setGracePeriodStrategy(GracePeriodStrategy gracePeriodStrategy) {
        resultSetExtractor.setGracePeriodStrategy(gracePeriodStrategy);
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Search for subscriptions by the person who owns them.
     * @param subscriptionOwner the federation-wide unique identifier for the person that owns the subscriptions
     * @return the list of matching subscription objects
     */
    public List<Subscription> searchForSubscriptionsBySubscriptionOwner(@Header("saml_FederationID") String subscriptionOwner) {

        String sqlQuery = BASE_QUERY_STRING + " and s.subscriptionOwner=? and s.active =1";

        List<Subscription> subscriptions = this.jdbcTemplate.query(sqlQuery, new Object[] {
            subscriptionOwner
        }, resultSetExtractor);

        return subscriptions;
    }

    /**
     * Determine the number of subscriptions owned by the specified owner.
     * @param subscriptionOwner the federation-wide unique identifier for the person that owns the subscriptions
     * @return the count of subscriptions that person owns
     */
    public int countSubscriptionsInSearch(@Header("saml_FederationID") String subscriptionOwner) {

        String sqlQuery = "select count(*) from subscription where subscriptionOwner=? and active =1";

        int subscriptionCountForOwner = this.jdbcTemplate.queryForObject(sqlQuery, Integer.class, subscriptionOwner);

        return subscriptionCountForOwner;
    }

    /**
     * Retrieve the single subscription owned by the specified person, where the subscription has the specified ID
     * @param subscriptionOwner the federation-wide unique identifier for the person that owns the subscriptions. If there is no matching
     * subscription, an IllegalStateException is thrown.
     * @param id the identifier for the subscription
     * @return the matching subscription
     */
    public Subscription queryForSubscription(@Header("saml_FederationID") String subscriptionOwner, @Header("subscriptionQueryId") String id) {
        String sqlQuery = BASE_QUERY_STRING + " and s.subscriptionOwner=? and s.id=? and s.active = 1";

        List<Subscription> subscriptions = this.jdbcTemplate.query(sqlQuery, new Object[] {
            subscriptionOwner, id
        }, resultSetExtractor);

        if (subscriptions.size() != 1) {
            throw new IllegalStateException("Query did not return the correct number of results.");
        }

        Subscription subscriptionToReturn = subscriptions.get(0); 
        setSubscriptionProperties(subscriptionToReturn);
        
        return subscriptionToReturn; 
    }

	public Subscription findSubscriptionByFbiSubscriptionId(String fbiRelatedSubscriptionId){
		
		String sql = "SELECT s.id, s.topic, s.startDate, s.endDate, s.lastValidationDate, s.subscribingSystemIdentifier, s.subscriptionOwner, s.subscriptionOwnerEmailAddress, s.subjectName, "
                + "s.ori, si.identifierName, s.subscription_category_code, s.agency_case_number, si.identifierValue, nm.notificationAddress, nm.notificationMechanismType, "
                + "fbi_sub.* "
                + "FROM subscription s, notification_mechanism nm, subscription_subject_identifier si, FBI_RAP_BACK_SUBSCRIPTION fbi_sub "
                + "WHERE nm.subscriptionId = s.id and si.subscriptionId = s.id AND fbi_sub.subscription_id = s.id "
                + "AND fbi_sub.FBI_SUBSCRIPTION_ID = ?";
        List<Subscription> subscriptions = this.jdbcTemplate.query(sql, resultSetExtractor, fbiRelatedSubscriptionId);
        
        Subscription subscription = DataAccessUtils.singleResult(subscriptions);
        
        setSubscriptionProperties(subscription);
        
		return subscription;
	}

	public Subscription findSubscriptionWithFbiInfoBySubscriptionId(String subscriptionId){
		
		String sql = "SELECT s.id, s.topic, s.startDate, s.endDate, s.lastValidationDate, s.subscribingSystemIdentifier, s.subscriptionOwner, s.subscriptionOwnerEmailAddress, s.subjectName, "
                + "si.identifierName, s.subscription_category_code, s.agency_case_number, s.ori, si.identifierValue, nm.notificationAddress, nm.notificationMechanismType, "
                + "fbi_sub.* "
                + "FROM subscription s, notification_mechanism nm, subscription_subject_identifier si, FBI_RAP_BACK_SUBSCRIPTION fbi_sub "
                + "WHERE nm.subscriptionId = s.id and si.subscriptionId = s.id AND fbi_sub.subscription_id = s.id "
                + "AND s.id = ?";
        List<Subscription> subscriptions = this.jdbcTemplate.query(sql, resultSetExtractor, subscriptionId);
        
        Subscription subscription = DataAccessUtils.singleResult(subscriptions);
        
        setSubscriptionProperties(subscription);
        
		return subscription;
	}
	
	private void setSubscriptionProperties(Subscription subscription) {
		if (subscription != null){
            Map<String, String> subscriptionProperties = getSubscriptionProperties(String.valueOf(subscription.getId()));
            subscription.setSubscriptionProperties(subscriptionProperties);
        }
	}
	
    /**
     * This method is retained for backwards compatibility and will pass null for subject identifiers.
     * The method it delegates to will use the subject identifiers defined in the notification request.
     * 
     * @param notificationRequest
     * @return
     */
    public List<Subscription> searchForSubscriptionsMatchingNotificationRequest(NotificationRequest notificationRequest) {
    
    	return searchForSubscriptionsMatchingNotificationRequest(notificationRequest, null);
    }
    
    /**
     * Search for subscriptions for the subjects using the Notification Request which contains specified identifiers, and that are/were active on the specified event date.  
     * This is essentially how you determine the applicable subscriptions for a specified event, so that you can then notify about that event.  This method will filter out
     * any subscriptions that are passed the validation due date.  This method allows the user to override the subject identifiers.  This is useful when querying for 
     * alternateSubjectIdentifiers.
     * @param notificationRequest notification request containing all notification message components mapped to POJO
     * @return the list of subscriptions to be notified of the event
     */
    public List<Subscription> searchForSubscriptionsMatchingNotificationRequest(NotificationRequest notificationRequest, Map<String, String> alternateSubjectIdentifiers) {

    	//Retrieve fields from notification request
    	DateTime eventDate = notificationRequest.getNotificationEventDate();
    	
    	Map<String, String> subjectIdentifiers = null;
    	
    	if (alternateSubjectIdentifiers != null)
    	{
    		subjectIdentifiers = alternateSubjectIdentifiers;
    	}	
    	else
    	{	
    		subjectIdentifiers = notificationRequest.getSubjectIdentifiers();
    	}
    	
    	//log.debug("baseNotificationsOnEventDate=" + baseNotificationsOnEventDate);
        DateTime notificationCompareDate = baseNotificationsOnEventDate ? eventDate : new DateTime();
        String notificationCompareDateString = DATE_FORMATTER_YYYY_MM_DD.print(notificationCompareDate);
        //log.debug("notificationCompareDateString=" + notificationCompareDateString);
        //log.debug("event Date=" + DATE_FORMATTER_YYYY_MM_DD.print(eventDate));
        Object[] criteriaArray = new Object[] {
            DATE_FORMATTER_YYYY_MM_DD.print(eventDate), notificationCompareDateString, notificationCompareDateString, notificationCompareDateString, notificationRequest.getTopic()
        };

        String queryString = BASE_QUERY_STRING + " and s.startDate <=? and ((s.startDate <=? and s.endDate >?) or (s.startDate <=? and s.endDate is null)) and s.topic=? and active=1 and ";
        queryString += (" (" + buildCriteriaSql(subjectIdentifiers.size()) + " or s.id in (select subscriptionId from subscription_subject_identifier where identifierValue='*')) ");

        Object[] subjectIdArray = buildCriteriaArray(subjectIdentifiers);
        criteriaArray = ArrayUtils.addAll(criteriaArray, subjectIdArray);
        
        List<Subscription> subscriptions = this.jdbcTemplate.query(queryString, criteriaArray, resultSetExtractor);
        log.debug("Found " + subscriptions.size() + " subscriptions:" + subscriptions);
        List<Subscription> wildcardFilteredSubscriptions = new ArrayList<Subscription>();
        
        for (Subscription s : subscriptions) {
            
            Map<String, String> subscriptionSubjectIdentifiers = s.getSubscriptionSubjectIdentifiers();
            if (subscriptionSubjectIdentifiers.values().contains("*")) {
                if (subscriptionSubjectIdentifiers.size() > 1) {
                    boolean keep = true;
                    for (String identifierName : subscriptionSubjectIdentifiers.keySet()) {
                        if (!("*".equals(subscriptionSubjectIdentifiers.get(identifierName)))) {
                            keep = keep && subscriptionSubjectIdentifiers.get(identifierName).equals(subjectIdentifiers.get(identifierName));
                        }
                    }
                    if (keep) {
                        wildcardFilteredSubscriptions.add(s);
                    }
                } else {
                    wildcardFilteredSubscriptions.add(s);
                }
            } else {
                wildcardFilteredSubscriptions.add(s);
            }
            
        }
        
        subscriptions = wildcardFilteredSubscriptions;

        List<Subscription> subscriptionToFilter = new ArrayList<Subscription>();
                
        //Check Validation Due Date Period Here
        for (Subscription subscription : subscriptions) {
        	
        	DateTime validationDueDate = subscription.getValidationDueDate();
        	//log.debug("validationDueDate=" + validationDueDate);
        	//No validation due date, continue
        	if (validationDueDate == null)
        	{
        		continue;
        	}	

        	//Check the current date against the validation due date.  If the current date is past the validation due date, filter the subscription.
        	if (Days.daysBetween(notificationCompareDate, validationDueDate).getDays() < 0)
        	{
        		//Add to list for Removing subscriptions
        		subscriptionToFilter.add(subscription);
        		//log.debug("Filtering sub=" + subscription);
        	}	
        	
        }
        
        log.debug("Here are the " + subscriptionToFilter.size() + " subscriptions to filter: " + subscriptionToFilter.toString());
        subscriptions.removeAll(subscriptionToFilter);
        
        return subscriptions;
    }

    public boolean isBaseNotificationsOnEventDate() {
        return baseNotificationsOnEventDate;
    }

    public void setBaseNotificationsOnEventDate(boolean baseNotificationsOnEventDate) {
        this.baseNotificationsOnEventDate = baseNotificationsOnEventDate;
    }

    /**
     * Retrieve a list of subscriptions for the specified subscription ID.  The list will have one item if there is a match for that ID, or zero
     * items if there is no match.
     * @param id the unique subscription ID
     * @return the list of subscriptions for that ID
     */
    public List<Subscription> queryForSubscription(String id) {

        List<Subscription> ret = new ArrayList<Subscription>();

        if (StringUtils.isNotEmpty(id)) {
            Object[] criteriaArray = new Object[] {
                id.trim()
            };
            String queryString = BASE_QUERY_STRING + " and s.id=?";
            ret = this.jdbcTemplate.query(queryString, criteriaArray, resultSetExtractor);

        }

        return ret;

    }

    /**
     * Retrieves subscriptions that matches the specified subscribing system, the specified owner, and the specified subject
     * 
     * Either one of topic, subscribingSystemId, owner OR subjectIdentifiers must be entered
     * 
     * @param subscribingSystemId
     * @param subscriptionOwner the federation-wide unique identifier for the person that owns the subscriptions. If there is no matching owner
     * @param subjectIdentifiers the identifiers for the subject of the event
     * @return
     */
    public List<Subscription> queryForSubscription(String topic, String subscribingSystemId, String owner, Map<String, String> subjectIdentifiers) {

        List<Subscription> ret = new ArrayList<Subscription>();
        
        List<String> criteriaList = new ArrayList<String>();
        
        StringBuffer staticCriteria = new StringBuffer();

        if (StringUtils.isNotBlank(subscribingSystemId))
        {
        	criteriaList.add(subscribingSystemId.trim());
        	staticCriteria.append(" and s.subscribingSystemIdentifier=?");
        }	
        
        if (StringUtils.isNotBlank(owner))
        {
        	criteriaList.add(owner.trim());
        	staticCriteria.append(" and s.subscriptionOwner = ?");
        }	
        
        if (StringUtils.isNotBlank(topic))
        {
        	criteriaList.add(topic.trim());
        	staticCriteria.append(" and s.topic=? ");
        }	

        Object[] criteriaArray = criteriaList.toArray(new Object[criteriaList.size()]);

        criteriaArray = ArrayUtils.addAll(criteriaArray, SubscriptionSearchQueryDAO.buildCriteriaArray(subjectIdentifiers));

        String queryString = BASE_QUERY_STRING + staticCriteria.toString()
                + " and " + SubscriptionSearchQueryDAO.buildCriteriaSql(subjectIdentifiers.size());
        ret = this.jdbcTemplate.query(queryString, criteriaArray, resultSetExtractor);

        return ret;

    }
    
    public Integer subscribe(@Header("subscriptionRequest")SubscriptionRequest request){
    	Number subscriptionId = subscribe(request, new LocalDate());
    	return subscriptionId.intValue();
    }
    /**
     * Create a subscription (or update an existing one) given the input parameters
     * @param request
     * @param creationDateTime
     * @return the ID of the created (or updated) subscription
     */
    public Number subscribe(SubscriptionRequest request, LocalDate creationDateTime) {
    	
    	log.debug("Entering subscribe method");
    	log.debug("SubscriptionRequest: " + request);
    	
    	Number ret = null;
    	
    	Date startDate = getStartDate(request.getStartDateString());
    	// Many subscription message will not have end dates so we will need to
    	// allow nulls
    	Date endDate = getSqlDateFromString(request.getEndDateString());
    	
    	java.util.Date creationDate = creationDateTime.toDateTimeAtStartOfDay().toDate();
    	
    	
    	String fullyQualifiedTopic = NotificationBrokerUtils.getFullyQualifiedTopic(request.getTopic());
    	
    	List<Subscription> subscriptions = getSubscriptions(request.getSubscriptionSystemId(), 
    			fullyQualifiedTopic, request.getSubjectIdentifiers(), request.getSystemName(), request.getSubscriptionOwner());
    	
    	// No Record exist, insert a new one
    	if (subscriptions.size() == 0) {
    		
    		ret = saveSubscription(request, startDate,
    				endDate, creationDate, fullyQualifiedTopic);    
    	}
    	
    	// A subscriptions exists, let's update it
    	if (subscriptions.size() == 1) {
    		log.debug("Ensure that SIDs match before updating subscription");
    		log.debug("Updating existing subscription");
    		log.debug("Updating row in subscription table");
    		
    		long subscriptionID = subscriptions.get(0).getId();
    		
    		updateSubscription(request.getSubjectName(), request.getSubscriptionOwner(),
    				request.getSubscriptionOwnerEmailAddress(), startDate, endDate,
    				creationDate, fullyQualifiedTopic, subscriptionID);
    		
    		updateEmailAddresses(new ArrayList<String>(request.getEmailAddresses()), subscriptionID);
    		
    		updateSubscriptionProperties(request.getSubscriptionProperties(), subscriptionID);	
    		
    		ret = subscriptionID;
    		
    	}
    	
    	if (ret != null && CIVIL_SUBSCRIPTION_REASON_CODE.equals(request.getReasonCategoryCode())){
    		subscribeIdentificationTransaction(ret, request.getAgencyCaseNumber(), request.getEndDateString());
    	}
    	
    	return ret;
    	
    }
    
	private Date getSqlDateFromString(String dateString) {
		// Create SQL date from the end date string
		Date endDate = null;
        if (StringUtils.isNotBlank(dateString)) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
			try {
			    java.util.Date utilEndDate = formatter.parse(dateString.trim());
			    endDate = new Date(utilEndDate.getTime());
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
        }
		return endDate;
	}

    /**
     * If startDateString is blank, use current date
     * @param startDateString
     * @return
     */
	private Date getStartDate(String startDateString) {
		Date startDate = null;
        if (StringUtils.isNotBlank(startDateString)) {
            startDate = getSqlDateFromString(startDateString);
        } else {
            startDate = new Date(System.currentTimeMillis());
        }
		return startDate;
	}

	private void updateSubscription(String offenderName, String subscriptionOwner,
			String subscriptionOwnerEmailAddress, Date startDate, Date endDate,
			java.util.Date creationDate, String fullyQualifiedTopic,
			long subscriptionID) {
		this.jdbcTemplate.update("update subscription set topic=?, startDate=?, endDate=?, subjectName=?, active=1, subscriptionOwner=?, subscriptionOwnerEmailAddress=?, lastValidationDate=? where id=?", new Object[] {
		    fullyQualifiedTopic.trim(), startDate, endDate, offenderName.trim(), subscriptionOwner, subscriptionOwnerEmailAddress, creationDate, subscriptionID
		});
	}

	private void updateSubscriptionProperties(Map<String, String> subscriptionProperties,
			long subscriptionID) {
		String existingSubscriptionIDString = String.valueOf(subscriptionID);
		
		Map<String, String> subscriptionPropertiesFromExistingSubscription = getSubscriptionProperties(existingSubscriptionIDString);
		
		if(updateSubscriptionProperties(subscriptionProperties, subscriptionPropertiesFromExistingSubscription))
		{
			deleteSubscriptionProperties(existingSubscriptionIDString);
			
			saveSubscriptionProperties(subscriptionProperties, subscriptionID); 

		}
	}

	private void updateEmailAddresses(List<String> emailAddresses, long subscriptionID) {
        log.debug("Updating row in notification_mechanism table");

        // We will delete all email addresses associated with the subscription and re-add them
        this.jdbcTemplate.update("delete from notification_mechanism where subscriptionId = ?", subscriptionID);

	    saveEmailAddresses(emailAddresses, subscriptionID);
	}

	private void saveEmailAddresses(List<String> emailAddresses, long subscriptionID) {
		this.jdbcTemplate.batchUpdate(
    		"insert into notification_mechanism (subscriptionId, notificationMechanismType, notificationAddress) values (?,?,?)", 
    		new BatchPreparedStatementSetter() { public void setValues(PreparedStatement ps, int i)
                    throws SQLException {
                ps.setLong(1, subscriptionID);
                ps.setString(2, NotificationConstants.NOTIFICATION_MECHANISM_EMAIL);
                ps.setString(3, emailAddresses.get(i));
            }
	            
            public int getBatchSize() {
                return emailAddresses.size();
            }
        });
	}

	private Number saveSubscription(SubscriptionRequest request, Date startDate, Date endDate,
			java.util.Date creationDate, String fullyQualifiedTopic) {
		
//		private Number saveSubscription(Map<String, String> subjectIdentifiers,
//				Map<String, String> subscriptionProperties,
//				Set<String> emailAddresses, String offenderName,
//				String subscribingSystemId, String reasonCategoryCode,
//				String subscriptionOwner, String subscriptionOwnerEmailAddress,
//				String agencyCaseNumber, Date startDate, Date endDate,
//				java.util.Date creationDate, String fullyQualifiedTopic) {
			
		Number ret;
		
		log.debug("Inserting row into subscription table");

		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(
			buildPreparedInsertStatementCreator(
                "insert into subscription ("
                + "topic, startDate, endDate, subscribingSystemIdentifier, subscriptionOwner, "
                + "subscriptionOwnerEmailAddress, subjectName, active, subscription_category_code, "
                + "lastValidationDate, agency_case_number, ori) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[] {
                    fullyQualifiedTopic, startDate, endDate, request.getSystemName(), request.getSubscriptionOwner(), 
                    request.getSubscriptionOwnerEmailAddress(), request.getSubjectName(), 1, request.getReasonCategoryCode(), 
                    creationDate, request.getAgencyCaseNumber(), request.getOri()
                }), keyHolder);

		ret = keyHolder.getKey();

		saveEmailAddresses(new ArrayList<String>(request.getEmailAddresses()), ret.longValue());
		
		log.debug("Inserting row(s) into subscription_subject_identifier table");

		for (Map.Entry<String, String> entry : request.getSubjectIdentifiers().entrySet()) {
		    this.jdbcTemplate.update("insert into subscription_subject_identifier (subscriptionId, identifierName, identifierValue) values (?,?,?)", keyHolder.getKey(), entry.getKey(),
		            entry.getValue());
		}

		saveSubscriptionProperties(request.getSubscriptionProperties(), keyHolder.getKey().longValue());
		return ret;
	}

	public int saveSubscriptionProperties(
			Map<String, String> subscriptionProperties, long subscriptionID) {
		
		int rowsSaved = 0;
		
		if (subscriptionProperties != null)
		{	
		    for (Map.Entry<String, String> entry : subscriptionProperties.entrySet()) {
		        int rowsUpdated = this.jdbcTemplate.update("insert into subscription_properties (subscriptionId, propertyname, propertyvalue) values (?,?,?)", subscriptionID, entry.getKey(),
		                entry.getValue());
		        
		        if (rowsUpdated == 1)
		        {
		        	rowsSaved++;
		        }	
		    }
		}
		
		return rowsSaved;
	}
    
    
    boolean updateSubscriptionProperties(Map<String, String> subscriptionPropertiesFromRequest, Map<String, String> subscriptionPropertiesFromExistingSubscription) {
    	
    	//No subscription properties, no update required
        if ((subscriptionPropertiesFromExistingSubscription == null) && (subscriptionPropertiesFromRequest == null))
        {
        	return false;
        }	
        
        //One is null, the other isn't, update required
        if ((subscriptionPropertiesFromExistingSubscription != null) && (subscriptionPropertiesFromRequest == null))
        {
        	return true;
        }	
        
        //One is null, the other isn't, update required
        if ((subscriptionPropertiesFromExistingSubscription == null) && (subscriptionPropertiesFromRequest != null))
        {
        	return true;
        }	
        
        //Different sizes, update required
        if (subscriptionPropertiesFromExistingSubscription.size() != subscriptionPropertiesFromRequest.size())
        {
        	return true;
        }	

        if (subscriptionPropertiesFromExistingSubscription.size() == subscriptionPropertiesFromRequest.size())
        {
        	for (Map.Entry<String, String> entry : subscriptionPropertiesFromExistingSubscription.entrySet()) {
        	    
        		//key in one map, not the other, update required
        		if (!subscriptionPropertiesFromRequest.containsKey(entry.getKey()))
        	    {
        			return true;
        	    }	
        		else	
        		{
        			//values are different, update required
        			if (!subscriptionPropertiesFromRequest.get(entry.getKey()).equals(entry.getValue()))
        			{
        				return true;
        			}	
        		}
        		
        	}
        }	
        
		return false;
	}

	private void subscribeIdentificationTransaction(Number subscriptionId, String transactionNumber, String endDateString ){
    	final String IDENTIFICATION_TRANSACTION_SUBSCRIBE = "UPDATE identification_transaction "
    			+ "SET subscription_id = ?, available_for_subscription_start_date = ? WHERE transaction_number = ? ";
    	
    	DateTime endDate = XmlUtils.parseXmlDate(endDateString);
    	endDate = endDate.plusDays(1);
    	this.jdbcTemplate.update(IDENTIFICATION_TRANSACTION_SUBSCRIBE, subscriptionId, endDate.toDate(), transactionNumber);
    }
    
    private void unsubscribeIdentificationTransaction(Integer subscriptionId){
    	final String IDENTIFICATION_TRANSACTION_UNSUBSCRIBE = "UPDATE identification_transaction "
    			+ "SET available_for_subscription_start_date = ? WHERE subscription_id = ? ";
    	
    	this.jdbcTemplate.update(IDENTIFICATION_TRANSACTION_UNSUBSCRIBE, Calendar.getInstance().getTime(), subscriptionId);
    }
    
    public int unsubscribe(String subscriptionSystemId, String topic, Map<String, String> subjectIds, String systemName, String subscriptionOwner) {

        int returnCount;
        
        String fullyQualifiedTopic = NotificationBrokerUtils.getFullyQualifiedTopic(topic);

        // If we have the subscriptionSystemId (the id PK key in the database, use that to unsubscribe). This typically comes from a subscription search
        if (StringUtils.isNotBlank(subscriptionSystemId)) {
            log.debug("unsubscribing manual subscription, subscritpion system ID: " + subscriptionSystemId);

            Object[] criteriaArray = new Object[] {
                fullyQualifiedTopic, subscriptionSystemId
            };
            String queryString = "update subscription s set s.active=0 where s.topic=? and s.id=?";
            returnCount = this.jdbcTemplate.update(queryString, criteriaArray);

            log.debug("fbiSubscriptionMember? " + BooleanUtils.toStringTrueFalse(fbiSubscriptionMember));
            if (fbiSubscriptionMember){
            	unsubscribeIdentificationTransaction(Integer.valueOf(subscriptionSystemId));
            }
            return returnCount;

        }
        // If we don't have the subscriptionSystemID, attempt to unsubscribe using the info available in the message. This typically comes from automated subscriptions.
        else {
            log.debug("unsubscribing auto subscription, not subscritpion system ID");

            Object[] criteriaArray = new Object[] {
                fullyQualifiedTopic, systemName, subscriptionOwner
            };
            criteriaArray = ArrayUtils.addAll(criteriaArray, SubscriptionSearchQueryDAO.buildCriteriaArray(subjectIds));
            String queryString = "update subscription s set s.active=0 where s.topic=? and s.subscribingSystemIdentifier=? and s.subscriptionOwner = ? and"
                    + SubscriptionSearchQueryDAO.buildCriteriaSql(subjectIds.size());

            log.debug("Query String: " + queryString);
            log.debug("Topic: " + fullyQualifiedTopic + " System Name: " + systemName + " subscription owner: " + subscriptionOwner);

            returnCount = this.jdbcTemplate.update(queryString, criteriaArray);
        }

        return returnCount;
    }

    private final String UPDATE_SUBJECT_IDENTIFER_BY_SUBSCRIPTION_ID = "UPDATE subscription_subject_identifier SET identifierValue = ? "
    		+ "WHERE identifierName = ? and identifierValue = ? and subscriptionId = ?"; 
    /**
     * Update the subscripiton_subject_identifier using the provided parameters
     * @param currentSid
     * @param newSid
     */
    public void updateSubscriptionSubjectIdentifier(String value, String newValue, String subscriptionId, String identiferName){
    	this.jdbcTemplate.update(UPDATE_SUBJECT_IDENTIFER_BY_SUBSCRIPTION_ID, newValue, identiferName, value, subscriptionId);
    }
    
    static Object[] buildCriteriaArray(Map<String, String> subjectIdentifiers) {
    	List<String> entryList = new ArrayList<String>();
    	for (Map.Entry<String, String> entry : subjectIdentifiers.entrySet()) {
    		entryList.add(entry.getKey());
    		entryList.add(entry.getValue());
    	}
    	
    	return entryList.toArray();
    }

    static String buildCriteriaSql(int subjectIdsCount) {
    	StringBuffer sql = new StringBuffer();
    	
    	sql.append(" s.id in");
    	
    	for (int i = 0; i < subjectIdsCount; i++) {
    		if (i > 0) {
    			sql.append(" and s.id in");
    		}
    		sql.append(" (select subscriptionId from subscription_subject_identifier where identifierName=? and identifierValue=?)");
    	}
    	
    	return sql.toString();
    }

    private List<Subscription> getSubscriptions(String subscriptionId, String topic, Map<String, String> subjectIds, String subscribingSystemId, String subscriptionOwner) {

        List<Subscription> ret = null;

        if (StringUtils.isNotEmpty(subscriptionId)) {
            ret = queryForSubscription(subscriptionId);
        } else {
            ret = queryForSubscription(topic, subscribingSystemId, subscriptionOwner, subjectIds);
        }
        
        return ret;

    }
    
    public List<String> getUniqueSubscriptionOwners()
    {
    	String queryString = "SELECT distinct(subscriptionOwner) FROM subscription where subscriptionOwner <> 'SYSTEM' order by subscriptionOwner";
    	
    	List<String> subscriptionOwners = (List<String>) jdbcTemplate.queryForList(queryString, String.class);
    	
    	return subscriptionOwners;
    }
    
    public Map<String, String > getSubscriptionProperties(String id)
    {
        String sqlQuery = "select * from subscription_properties where subscriptionId=? order by propertyName";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlQuery, new Object[] {id});
        		
        Map<String, String > ret = new HashMap<String, String >();
        
        if (rows != null)
        {
        	for (Map<String, Object> row : rows)
        	{
        		String propertyName = (String)row.get("propertyName");
        		String propertyValue = (String)row.get("propertyValue");
        				
        		ret.put(propertyName, propertyValue);
        	}	
        }	
        
        return ret;
    }
    
    public int deleteSubscriptionProperties(String id)
    {
        String sqlQuery = "delete from subscription_properties where subscriptionId=?";

        int rowsUpdated = jdbcTemplate.update(sqlQuery, new Object[] {id});
        		
        return rowsUpdated;
    }
    
    private PreparedStatementCreator buildPreparedInsertStatementCreator(final String sql, final Object[] params) {
        return new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] {
                    "id"
                });
                int paramIndex = 1;
                for (Object param : params) {
                    ps.setObject(paramIndex, param);
                    paramIndex++;
                }
                return ps;
            }
        };
    }

	public boolean isFbiSubscriptionMember() {
		return fbiSubscriptionMember;
	}

	public void setFbiSubscriptionMember(boolean fbiSubscriptionMember) {
		this.fbiSubscriptionMember = fbiSubscriptionMember;
	}

}

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
 * Copyright 2012-2015 Open Justice Broker Consortium
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
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.ojbc.intermediaries.sn.notification.NotificationConstants;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;
import org.ojbc.intermediaries.sn.util.NotificationBrokerUtils;
import org.ojbc.util.xml.XmlUtils;
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

	private static final String BASE_QUERY_STRING = "select s.id, s.topic, s.startDate, s.endDate, s.lastValidationDate, s.subscribingSystemIdentifier, s.subscriptionOwner, s.subjectName, "
                    + " si.identifierName, s.subscription_category_code, si.identifierValue, nm.notificationAddress, nm.notificationMechanismType "
                    + " from subscription s, notification_mechanism nm, subscription_subject_identifier si where nm.subscriptionId = s.id and si.subscriptionId = s.id ";

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

        int subscriptionCountForOwner = this.jdbcTemplate.queryForInt(sqlQuery, new Object[] {
            subscriptionOwner
        });

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

        return subscriptions.get(0);
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
     * Retrieve the single subscription (in a list) that matches the specified subscribing system, the specified owner, and the specified subject
     * @param subscribingSystemId
     * @param subscriptionOwner the federation-wide unique identifier for the person that owns the subscriptions. If there is no matching owner
     * @param subjectIdentifiers the identifiers for the subject of the event
     * @return
     */
    public List<Subscription> queryForSubscription(String topic, String subscribingSystemId, String owner, Map<String, String> subjectIdentifiers) {

        List<Subscription> ret = new ArrayList<Subscription>();

        Object[] criteriaArray = new Object[] {
            subscribingSystemId.trim(), owner, topic
        };
        criteriaArray = ArrayUtils.addAll(criteriaArray, SubscriptionSearchQueryDAO.buildCriteriaArray(subjectIdentifiers));
        String queryString = BASE_QUERY_STRING + " and s.subscribingSystemIdentifier=? and s.subscriptionOwner = ? and s.topic=? and "
                + SubscriptionSearchQueryDAO.buildCriteriaSql(subjectIdentifiers.size());
        ret = this.jdbcTemplate.query(queryString, criteriaArray, resultSetExtractor);

        return ret;

    }
    
    /**
     * Create a subscription (or update an existing one) given the input parameters
     * @param subscriptionSystemId
     * @param topic
     * @param startDateString
     * @param endDateString
     * @param subjectIdentifiers
     * @param emailAddresses
     * @param offenderName
     * @param subscribingSystemId
     * @param subscriptionQualifier
     * @param reasonCategoryCode
     * @param subscriptionOwner
     * @return the ID of the created (or updated) subscription
     */
    public Number subscribe(String subscriptionSystemId, String topic, String startDateString, String endDateString, Map<String, String> subjectIdentifiers, Set<String> emailAddresses, String offenderName,
            String subscribingSystemId, String subscriptionQualifier, String reasonCategoryCode, String subscriptionOwner, String subscriptionOwnerEmailAddress, LocalDate creationDateTime, String agencyCaseNumber) {

        Number ret = null;

        log.debug("Entering subscribe method");

        // Use the current time as the start date
        Date startDate = null;
        Date endDate = null;

        // If start date is not provided in the subscription message, use current date
        if (StringUtils.isNotBlank(startDateString)) {
            // Create SQL date from the end date string
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            try {
                java.util.Date utilStartDate = formatter.parse(startDateString.trim());
                startDate = new Date(utilStartDate.getTime());
            } catch (ParseException e) {
            	throw new RuntimeException(e);
            }
        } else {
            startDate = new Date(System.currentTimeMillis());
        }

        // Many subscription message will not have end dates so we will need to
        // allow nulls
        if (StringUtils.isNotBlank(endDateString)) {
            // Create SQL date from the end date string
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            try {
                java.util.Date utilEndDate = formatter.parse(endDateString.trim());
                endDate = new Date(utilEndDate.getTime());
            } catch (ParseException e) {
            	throw new RuntimeException(e);
            }
        }
        
        java.util.Date creationDate = creationDateTime.toDateTimeAtStartOfDay().toDate();

        log.debug("Start Date String: " + startDateString);
        log.debug("End Date String: " + endDateString);
        log.debug("System Name: " + subscribingSystemId);
        log.debug("Subscription System ID: " + subscriptionSystemId);
        log.debug("Reason Category Code = " + reasonCategoryCode);
        
        if(StringUtils.isEmpty(reasonCategoryCode)){
        	log.warn("Reason Category Code empty, so inserting null into db.");
        	reasonCategoryCode = null;
        }
        
        String fullyQualifiedTopic = NotificationBrokerUtils.getFullyQualifiedTopic(topic);

        List<Subscription> subscriptions = getSubscriptions(subscriptionSystemId, fullyQualifiedTopic, subjectIdentifiers, subscribingSystemId, subscriptionOwner);

        // No Record exist, insert a new one
        if (subscriptions.size() == 0) {

            log.debug("No subscriptions exist, inserting new one");

            log.debug("Inserting row into subscription table");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            this.jdbcTemplate.update(
                    buildPreparedInsertStatementCreator(
                            "insert into subscription (topic, startDate, endDate, subscribingSystemIdentifier, subscriptionOwner, subscriptionOwnerEmailAddress, subjectName, active, subscription_category_code, lastValidationDate, agency_case_number) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[] {
                                fullyQualifiedTopic.trim(), startDate, endDate, subscribingSystemId.trim(), subscriptionOwner, subscriptionOwnerEmailAddress, offenderName.trim(), 1, reasonCategoryCode, creationDate, agencyCaseNumber
                            }), keyHolder);

            ret = keyHolder.getKey();
            log.debug("Inserting row into notification_mechanism table");

            for (String emailAddress : emailAddresses) {
                this.jdbcTemplate.update("insert into notification_mechanism (subscriptionId, notificationMechanismType, notificationAddress) values (?,?,?)", keyHolder.getKey(), NotificationConstants.NOTIFICATION_MECHANISM_EMAIL,
                        emailAddress);
            }

            log.debug("Inserting row(s) into subscription_subject_identifier table");

            for (Map.Entry<String, String> entry : subjectIdentifiers.entrySet()) {
                this.jdbcTemplate.update("insert into subscription_subject_identifier (subscriptionId, identifierName, identifierValue) values (?,?,?)", keyHolder.getKey(), entry.getKey(),
                        entry.getValue());
            }
        }

        // A subscriptions exists, let's update it
        if (subscriptions.size() == 1) {
            log.debug("Ensure that SIDs match before updating subscription");

            log.debug("Updating existing subscription");
            log.debug("Subject Id Map: " + subjectIdentifiers);
            log.debug("Email Addresses: " + emailAddresses.toString());

            log.debug("Updating row in subscription table");

            this.jdbcTemplate.update("update subscription set topic=?, startDate=?, endDate=?, subjectName=?, active=1, subscriptionOwner=?, subscriptionOwnerEmailAddress=?, lastValidationDate=? where id=?", new Object[] {
                fullyQualifiedTopic.trim(), startDate, endDate, offenderName.trim(), subscriptionOwner, subscriptionOwnerEmailAddress, creationDate, subscriptions.get(0).getId()
            });

            log.debug("Updating row in notification_mechanism table");

            // We will delete all email addresses associated with the subscription and re-add them
            this.jdbcTemplate.update("delete from notification_mechanism where subscriptionId = ?", new Object[] {
                subscriptions.get(0).getId()
            });

            for (String emailAddress : emailAddresses) {
                this.jdbcTemplate.update("insert into notification_mechanism (subscriptionId, notificationMechanismType, notificationAddress) values (?,?,?)", subscriptions.get(0).getId(), NotificationConstants.NOTIFICATION_MECHANISM_EMAIL,
                        emailAddress);
            }

            ret = subscriptions.get(0).getId();

        }

        if (ret != null && CIVIL_SUBSCRIPTION_REASON_CODE.equals(reasonCategoryCode)){
        	subscribeIdentificationTransaction(ret, agencyCaseNumber, endDateString);
        }
        
        return ret;

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

    private final String SID_CONSOLIDATE = "UPDATE subscription_subject_identifier SET identifierValue = ? "
    		+ "WHERE identifierName = 'SID' and identifierValue = ?"; 
    /**
     * Replace the currentSid in the subscripiton_subject_identifier with the newSid
     * @param currentSid
     * @param newSid
     */
    public void consolidateSid(String currentSid, String newSid){
    	this.jdbcTemplate.update(SID_CONSOLIDATE, newSid, currentSid);
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

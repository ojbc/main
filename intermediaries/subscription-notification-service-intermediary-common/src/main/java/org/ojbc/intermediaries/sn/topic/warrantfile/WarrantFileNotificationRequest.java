package org.ojbc.intermediaries.sn.topic.warrantfile;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Message;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;
import org.ojbc.intermediaries.sn.util.NotificationBrokerUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class WarrantFileNotificationRequest extends NotificationRequest{
	
	private static final Logger logger = Logger.getLogger(WarrantFileNotificationRequest.class);
	
    public WarrantFileNotificationRequest(Message message) throws Exception{
        this(message.getBody(Document.class));
    }

    public WarrantFileNotificationRequest(Document document) throws Exception {
        super(document);
        buildSubjectIdMap();        
    }
    
    public String getPersonFullName() {
        String personFirstName = StringUtils.strip(getPersonFirstName());
        String personLastName = StringUtils.strip(getPersonLastName());
        return personFirstName + " " + personLastName;
    }
    
    public String getEventDateTimeDisplay() {
        boolean isNotificationEventDateInclusiveOfTime = isNotificationEventDateInclusiveOfTime();
        DateTime eventDateTime = getNotificationEventDate();
        String eventDate = NotificationBrokerUtils.returnFormattedNotificationEventDate(eventDateTime, isNotificationEventDateInclusiveOfTime);
        return StringUtils.strip(eventDate);
    }
    
	@Override
	protected String getNotificationEventDateRootXpath() {
		
		// note doesn't return nc:Date or nc:DateTime but rather returns their parent node,
		// because inherited logic conditionally uses one the the two child values
		return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingCriminalHistoryUpdate/chu:CycleTrackingIdentifierAssignment/nc:ActivityDate";
	}

	@Override
	protected String getNotifyingAgencyXpath() {
		
		String notifyingAgencyXpath = null;		
		
		try {
			String origOrganizerRef = getOrigOrganizationRef();
			
			notifyingAgencyXpath = "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Organization[@s:id='"+origOrganizerRef+"']/nc:OrganizationName";	
		} catch (Exception e) {			
			e.printStackTrace();
		}

		return notifyingAgencyXpath;
	}

	private String getOrigOrganizationRef() throws Exception {
		String origOrganizerRef = XmlUtils.xPathStringSearch(requestDocument, 
				"/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingCriminalHistoryUpdate/chu:CycleTrackingIdentifierAssignment/chu:OriginatorOrganizationReference/@s:ref");
		
		logger.info("Notifying Agency ref: " + origOrganizerRef);
		return origOrganizerRef;
	}

	@Override
	protected String getNotificationEventIdentifierXpath() {

		return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingCriminalHistoryUpdate/chu:CycleTrackingIdentifierAssignment/nc:Case/nc:ActivityIdentification/nc:IdentificationID";
	}
		
	
	@Override
	protected String getNotifyingSystemNameXPath(){
		return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingActivityReportingSystemNameText";
	}
	
	@Override
	public String getDescriptiveSubjectIdentifier() {
		
		Map<String, String> sidMap = getSubjectIdentifiers();
		
		String lastName = sidMap.get(SubscriptionNotificationConstants.LAST_NAME);
		
		String firstName = sidMap.get(SubscriptionNotificationConstants.FIRST_NAME);
		
		String dateOfBirth = sidMap.get(SubscriptionNotificationConstants.DATE_OF_BIRTH);
				
		return lastName + "_" + firstName + "_" + dateOfBirth;
	}
	
	@Override
	protected String getNotificationAgencyPhoneNumberXpath() {
		return "''";	
	}

	@Override
	protected String getOfficerNameReferenceXPath() {		
		return null;
	}

	private void buildSubjectIdMap() throws Exception {
		
		subjectIdentifiers = new HashMap<String, String>();
		
		subjectIdentifiers.put(SubscriptionNotificationConstants.FIRST_NAME, personFirstName);
		subjectIdentifiers.put(SubscriptionNotificationConstants.LAST_NAME, personLastName);
		
		String dateOfBirth = XmlUtils.xPathStringSearch(requestDocument,
				"/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Person[@s:id=../nc:ActivityInvolvedPersonAssociation/nc:PersonReference/@s:ref]/nc:PersonBirthDate/nc:Date");
		
		logger.debug("Notification message DOB: " + dateOfBirth);
		
		subjectIdentifiers.put(SubscriptionNotificationConstants.DATE_OF_BIRTH, dateOfBirth);
	}

	@Override
	protected String getNotifyingAgencyOriXpath() {
		try {
			String origOrganizerRef = getOrigOrganizationRef();
			return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Organization[@s:id='"
					+origOrganizerRef+"']/jxdm41:OrganizationAugmentation/jxdm41:OrganizationORIIdentification/nc:IdentificationID";			
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}

	}	
}

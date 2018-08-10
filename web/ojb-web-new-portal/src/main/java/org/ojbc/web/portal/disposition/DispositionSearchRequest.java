package org.ojbc.web.portal.disposition;

import java.time.LocalDate;

import org.ojbc.web.portal.SearchFieldMetadata;
import org.springframework.format.annotation.DateTimeFormat;

public class DispositionSearchRequest {

	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate dispositionDateRangeStartDate; 
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate dispositionDateRangeEndDate;
	
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate arrestDateRangeStartDate; 
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate arrestDateRangeEndDate;
	
	private String firstName;
    private SearchFieldMetadata firstNameSearchMetadata;
    
    private String lastName;
    private SearchFieldMetadata lastNameSearchMetadata;
    
	@DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate dob;
    
    private String ssn;
    private String otn;
    
    private String arrestIdentification;
    private String ori;
    
	public LocalDate getDispositionDateRangeStartDate() {
		return dispositionDateRangeStartDate;
	}
	public void setDispositionDateRangeStartDate(LocalDate dispositionDateRangeStartDate) {
		this.dispositionDateRangeStartDate = dispositionDateRangeStartDate;
	}
	public LocalDate getDispositionDateRangeEndDate() {
		return dispositionDateRangeEndDate;
	}
	public void setDispositionDateRangeEndDate(LocalDate dispositionDateRangeEndDate) {
		this.dispositionDateRangeEndDate = dispositionDateRangeEndDate;
	}
	public LocalDate getArrestDateRangeStartDate() {
		return arrestDateRangeStartDate;
	}
	public void setArrestDateRangeStartDate(LocalDate arrestDateRangeStartDate) {
		this.arrestDateRangeStartDate = arrestDateRangeStartDate;
	}
	public LocalDate getArrestDateRangeEndDate() {
		return arrestDateRangeEndDate;
	}
	public void setArrestDateRangeEndDate(LocalDate arrestDateRangeEndDate) {
		this.arrestDateRangeEndDate = arrestDateRangeEndDate;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public SearchFieldMetadata getFirstNameSearchMetadata() {
		return firstNameSearchMetadata;
	}
	public void setFirstNameSearchMetadata(SearchFieldMetadata firstNameSearchMetadata) {
		this.firstNameSearchMetadata = firstNameSearchMetadata;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public SearchFieldMetadata getLastNameSearchMetadata() {
		return lastNameSearchMetadata;
	}
	public void setLastNameSearchMetadata(SearchFieldMetadata lastNameSearchMetadata) {
		this.lastNameSearchMetadata = lastNameSearchMetadata;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getOtn() {
		return otn;
	}
	public void setOtn(String otn) {
		this.otn = otn;
	}
	public String getArrestIdentification() {
		return arrestIdentification;
	}
	public void setArrestIdentification(String arrestIdentification) {
		this.arrestIdentification = arrestIdentification;
	}
	public String getOri() {
		return ori;
	}
	public void setOri(String ori) {
		this.ori = ori;
	}
	public LocalDate getDob() {
		return dob;
	}
	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	
	@Override
	public String toString() {
		return "DispositionSearchRequest [dispositionDateRangeStartDate=" + dispositionDateRangeStartDate
				+ ", dispositionDateRangeEndDate=" + dispositionDateRangeEndDate + ", arrestDateRangeStartDate="
				+ arrestDateRangeStartDate + ", arrestDateRangeEndDate=" + arrestDateRangeEndDate + ", firstName="
				+ firstName + ", firstNameSearchMetadata=" + firstNameSearchMetadata + ", lastName=" + lastName
				+ ", lastNameSearchMetadata=" + lastNameSearchMetadata + ", dob=" + dob + ", ssn=" + ssn + ", otn="
				+ otn + ", arrestIdentification=" + arrestIdentification + ", ori=" + ori + "]";
	}
}
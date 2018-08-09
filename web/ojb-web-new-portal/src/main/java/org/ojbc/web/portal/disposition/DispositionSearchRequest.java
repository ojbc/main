package org.ojbc.web.portal.disposition;

import java.time.LocalDate;

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
    private String firstNameSearchMetadata;
    
    private String lastName;
    private String lastNameSearchMetadata;
    
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
	public String getFirstNameSearchMetadata() {
		return firstNameSearchMetadata;
	}
	public void setFirstNameSearchMetadata(String firstNameSearchMetadata) {
		this.firstNameSearchMetadata = firstNameSearchMetadata;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getLastNameSearchMetadata() {
		return lastNameSearchMetadata;
	}
	public void setLastNameSearchMetadata(String lastNameSearchMetadata) {
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
}
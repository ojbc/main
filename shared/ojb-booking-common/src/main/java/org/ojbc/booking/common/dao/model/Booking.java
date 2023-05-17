package org.ojbc.booking.common.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Booking {
	
	private int id;
	
	private Integer personId;
	
	private List<Arrest> arrestList = new ArrayList<Arrest>();

	private Date lastUdatedDate;
	
	private String bookingNumber;

	private Date bookingDatetime;

	private String facility;

	private byte[] bookingPhoto;

	private Date actualReleaseDatetime;

	private Date commitDate;

	private Date scheduledReleaseDate;

	private String block;

	private String bed;

	private String cell;

	private String caseStatus;

	private Boolean inmateWorkReleaseIndicator;

	private Boolean inmateWorkerIndicator;

	private Boolean probationerIndicator;
	
	private Boolean incarceratedIndicator;
	
	private Boolean inProcessIndicator; 
	
	private Boolean mistakenBookingIndicator;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public List<Arrest> getArrestList() {
		return arrestList;
	}

	public void setArrestList(List<Arrest> arrestList) {
		this.arrestList = arrestList;
	}

	public Date getLastUdatedDate() {
		return lastUdatedDate;
	}

	public void setLastUdatedDate(Date lastUdatedDate) {
		this.lastUdatedDate = lastUdatedDate;
	}

	public String getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(String bookingNumber) {
		this.bookingNumber = bookingNumber;
	}

	public String getFacility() {
		return facility;
	}

	public void setFacility(String facility) {
		this.facility = facility;
	}

	public Date getActualReleaseDatetime() {
		return actualReleaseDatetime;
	}

	public void setActualReleaseDatetime(Date actualReleaseDatetime) {
		this.actualReleaseDatetime = actualReleaseDatetime;
	}

	public Date getCommitDate() {
		return commitDate;
	}

	public void setCommitDate(Date commitDate) {
		this.commitDate = commitDate;
	}

	public Date getScheduledReleaseDate() {
		return scheduledReleaseDate;
	}

	public void setScheduledReleaseDate(Date scheduledReleaseDate) {
		this.scheduledReleaseDate = scheduledReleaseDate;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getBed() {
		return bed;
	}

	public void setBed(String bed) {
		this.bed = bed;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public Boolean getInmateWorkReleaseIndicator() {
		return inmateWorkReleaseIndicator;
	}

	public void setInmateWorkReleaseIndicator(Boolean inmateWorkReleaseIndicator) {
		this.inmateWorkReleaseIndicator = inmateWorkReleaseIndicator;
	}

	public Boolean getInmateWorkerIndicator() {
		return inmateWorkerIndicator;
	}

	public void setInmateWorkerIndicator(Boolean inmateWorkerIndicator) {
		this.inmateWorkerIndicator = inmateWorkerIndicator;
	}

	public Boolean getProbationerIndicator() {
		return probationerIndicator;
	}

	public void setProbationerIndicator(Boolean probationerIndicator) {
		this.probationerIndicator = probationerIndicator;
	}

	public Boolean getIncarceratedIndicator() {
		return incarceratedIndicator;
	}

	public void setIncarceratedIndicator(Boolean incarceratedIndicator) {
		this.incarceratedIndicator = incarceratedIndicator;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public byte[] getBookingPhoto() {
		return bookingPhoto;
	}

	public void setBookingPhoto(byte[] bookingPhoto) {
		this.bookingPhoto = bookingPhoto;
	}

	public Boolean getInProcessIndicator() {
		return inProcessIndicator;
	}

	public void setInProcessIndicator(Boolean inProcessIndicator) {
		this.inProcessIndicator = inProcessIndicator;
	}

	public Boolean getMistakenBookingIndicator() {
		return mistakenBookingIndicator;
	}

	public void setMistakenBookingIndicator(Boolean mistakenBookingIndicator) {
		this.mistakenBookingIndicator = mistakenBookingIndicator;
	}

	public Date getBookingDatetime() {
		return bookingDatetime;
	}

	public void setBookingDatetime(Date bookingDatetime) {
		this.bookingDatetime = bookingDatetime;
	}
	
}


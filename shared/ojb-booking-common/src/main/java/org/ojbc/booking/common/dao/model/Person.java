package org.ojbc.booking.common.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Person {
	
	private Integer id;

	private String personUniqueIdentifier;
	
	private List<Booking> bookingList;
	
	private List<PersonAlias> personAliasList = new ArrayList<PersonAlias>();
	
	private List<ScarsMarksTattoos> scarsMarksTatoosList = new ArrayList<ScarsMarksTattoos>();
	
	private List<Conditions> conditionsList = new ArrayList<Conditions>();
	
	private Boolean allowDeposits;
	
	private Boolean sexOffender;
	
	private String education;
	
	private String primaryLanguage;
	
	private Date dob;
	
	private String ethnicity;
	
	private String eyeColor;
	
	private String hairColor;
	
	private Integer height;
	
	private Integer weight;
		
	private String race;
	
	private String sid;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private String sex;
	
	private String occupation;
	
	private String militaryService;


	public Integer getId() {
		return id;
	}
	
	public String getPersonUniqueIdentifier() {
		return personUniqueIdentifier;
	}

	public void setPersonUniqueIdentifier(String personUniqueIdentifier) {
		this.personUniqueIdentifier = personUniqueIdentifier;
	}

	public List<Booking> getBookingList() {
		return bookingList;
	}

	public void setBookingList(List<Booking> bookingList) {
		this.bookingList = bookingList;
	}

	public List<PersonAlias> getPersonAliasList() {
		return personAliasList;
	}

	public void setPersonAliasList(List<PersonAlias> personAliasList) {
		this.personAliasList = personAliasList;
	}

	public List<ScarsMarksTattoos> getScarsMarksTatoosList() {
		return scarsMarksTatoosList;
	}

	public void setScarsMarksTatoosList(List<ScarsMarksTattoos> scarsMarksTatoosList) {
		this.scarsMarksTatoosList = scarsMarksTatoosList;
	}

	public Boolean getAllowDeposits() {
		return allowDeposits;
	}

	public void setAllowDeposits(Boolean allowDeposits) {
		this.allowDeposits = allowDeposits;
	}

	public Boolean getSexOffender() {
		return sexOffender;
	}

	public void setId(Integer id) {
		this.id = id;
	}	
	
	public void setSexOffender(Boolean sexOffender) {
		this.sexOffender = sexOffender;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getPrimaryLanguage() {
		return primaryLanguage;
	}

	public void setPrimaryLanguage(String primaryLanguage) {
		this.primaryLanguage = primaryLanguage;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getEyeColor() {
		return eyeColor;
	}

	public void setEyeColor(String eyeColor) {
		this.eyeColor = eyeColor;
	}

	public String getHairColor() {
		return hairColor;
	}

	public void setHairColor(String hairColor) {
		this.hairColor = hairColor;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getMilitaryService() {
		return militaryService;
	}

	public void setMilitaryService(String militaryService) {
		this.militaryService = militaryService;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", personUniqueIdentifier="
				+ personUniqueIdentifier + ", bookingList=" + bookingList
				+ ", personAliasList=" + personAliasList
				+ ", scarsMarksTatoosList=" + scarsMarksTatoosList
				+ ", allowDeposits=" + allowDeposits + ", sexOffender="
				+ sexOffender + ", education=" + education
				+ ", primaryLanguage=" + primaryLanguage + ", dob=" + dob
				+ ", ethnicity=" + ethnicity + ", eyeColor=" + eyeColor
				+ ", hairColor=" + hairColor + ", height=" + height
				+ ", weight=" + weight + ", race=" + race + ", sid=" + sid
				+ ", firstName=" + firstName + ", middleName=" + middleName
				+ ", lastName=" + lastName + ", sex=" + sex + ", occupation="
				+ occupation + ", militaryService=" + militaryService + "]";
	}

	public List<Conditions> getConditionsList() {
		return conditionsList;
	}

	public void setConditionsList(List<Conditions> conditionsList) {
		this.conditionsList = conditionsList;
	}
	
}

package org.ojbc.web.model.person.search;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import org.joda.time.DateTime;
import org.ojbc.web.SearchFieldMetadata;


public class PersonSearchRequest implements Serializable{

    private static final long serialVersionUID = 4875534327596344809L;
    private PersonName personName = new PersonName();
    private PersonName alias = new PersonName();
    private PersonName parentName = new PersonName(); 
    private Address address = new Address();
    
    private String placement; 
    //Demographics
	private DateTime personDateOfBirth;
	private DateTime personDateOfBirthRangeStart;
	private DateTime personDateOfBirthRangeEnd;
	private String personRaceCode;
	private String personSexCode;
	private String personEyeColor;
	private String personHairColor;

	private Integer personWeight;
	private Integer personWeightRangeStart;
	private Integer personWeightRangeEnd;

	private Integer personHeightTotalInches;
	private Integer personHeightTotalInchesRangeStart;
	private Integer personHeightTotalInchesRangeEnd;
	
	//Identifiers
	private String personSocialSecurityNumber;
	private String personDriversLicenseNumber;
	private String personDriversLicenseIssuer;
	private String personSID;
	private String personFBINumber;
	
	//Logging
	private String purpose;
	private String onBehalfOf;
	
	private List<String> sourceSystems;
	
	public String getPersonHairColor() {
		return personHairColor;
	}
	public void setPersonHairColor(String personHairColor) {
		this.personHairColor = personHairColor;
	}
	public String getPersonEyeColor() {
		return personEyeColor;
	}
	public void setPersonEyeColor(String personEyeColor) {
		this.personEyeColor = personEyeColor;
	}
	public String getPersonGivenName() {
		return personName.getGivenName();
	}
	public void setPersonGivenName(String personGivenName) {
		this.personName.setGivenName(personGivenName) ;
	}
	public String getPersonMiddleName() {
		return personName.getMiddleName();
	}
	public void setPersonMiddleName(String personMiddleName) {
		this.personName.setMiddleName(personMiddleName);
	}
	public String getPersonSurName() {
		return personName.getSurName();
	}
	public void setPersonSurName(String personSurName) {
		this.personName.setSurName(personSurName);
	}
	public String getPersonSexCode() {
		return personSexCode;
	}
	public void setPersonSexCode(String personSexCode) {
		this.personSexCode = personSexCode;
	}
	public String getPersonRaceCode() {
		return personRaceCode;
	}
	public void setPersonRaceCode(String personRaceCode) {
		this.personRaceCode = personRaceCode;
	}
	public String getPersonSocialSecurityNumber() {
		return personSocialSecurityNumber;
	}
	public void setPersonSocialSecurityNumber(String personSocialSecurityNumber) {
		this.personSocialSecurityNumber = personSocialSecurityNumber;
	}
	public String getPersonDriversLicenseNumber() {
		return personDriversLicenseNumber;
	}
	public void setPersonDriversLicenseNumber(String personDriversLicenseNumber) {
		this.personDriversLicenseNumber = personDriversLicenseNumber;
	}
	public String getPersonFBINumber() {
		return personFBINumber;
	}
	public void setPersonFBINumber(String personFBINumber) {
		this.personFBINumber = personFBINumber;
	}
	public String getPersonSID() {
		return personSID;
	}
	public void setPersonSID(String personSID) {
		this.personSID = personSID;
	}
	public SearchFieldMetadata getPersonGivenNameMetaData() {
		return personName.getGivenNameMetaData();
	}
	public void setPersonGivenNameMetaData(SearchFieldMetadata personGivenNameMetaData) {
		this.personName.setGivenNameMetaData( personGivenNameMetaData );
	}
	public SearchFieldMetadata getPersonSurNameMetaData() {
		return personName.getSurNameMetaData();
	}
	public void setPersonSurNameMetaData(SearchFieldMetadata personSurNameMetaData) {
		this.personName.setSurNameMetaData( personSurNameMetaData );
	}
	public String getPersonDriversLicenseIssuer() {
		return personDriversLicenseIssuer;
	}
	public void setPersonDriversLicenseIssuer(String personDriversLicenseIssuer) {
		this.personDriversLicenseIssuer = personDriversLicenseIssuer;
	}
	public DateTime getPersonDateOfBirth() {
		return personDateOfBirth;
	}
	public void setPersonDateOfBirth(DateTime personDateOfBirth) {
		this.personDateOfBirth = personDateOfBirth;
	}
	public DateTime getPersonDateOfBirthRangeStart() {
		return personDateOfBirthRangeStart;
	}
	public void setPersonDateOfBirthRangeStart(DateTime personDateOfBirthRangeStart) {
		this.personDateOfBirthRangeStart = personDateOfBirthRangeStart;
	}
	public DateTime getPersonDateOfBirthRangeEnd() {
		return personDateOfBirthRangeEnd;
	}
	public void setPersonDateOfBirthRangeEnd(DateTime personDateOfBirthRangeEnd) {
		this.personDateOfBirthRangeEnd = personDateOfBirthRangeEnd;
	}
	public List<String> getSourceSystems() {
		return sourceSystems;
	}
	public void setSourceSystems(List<String> sourceSystems) {
		this.sourceSystems = sourceSystems;
	}
	public Integer getPersonWeight() {
		return personWeight;
	}
	public void setPersonWeight(Integer personWeight) {
		this.personWeight = personWeight;
	}
	public Integer getPersonWeightRangeStart() {
		return personWeightRangeStart;
	}
	public void setPersonWeightRangeStart(Integer personWeightRangeStart) {
		this.personWeightRangeStart = personWeightRangeStart;
	}
	public Integer getPersonWeightRangeEnd() {
		return personWeightRangeEnd;
	}
	public void setPersonWeightRangeEnd(Integer personWeightRangeEnd) {
		this.personWeightRangeEnd = personWeightRangeEnd;
	}
	public Integer getPersonHeightTotalInches() {
		return personHeightTotalInches;
	}
	public void setPersonHeightTotalInches(Integer personHeightTotalInches) {
		this.personHeightTotalInches = personHeightTotalInches;
	}
	public Integer getPersonHeightTotalInchesRangeStart() {
		return personHeightTotalInchesRangeStart;
	}
	public void setPersonHeightTotalInchesRangeStart(Integer personHeightTotalInchesRangeStart) {
		this.personHeightTotalInchesRangeStart = personHeightTotalInchesRangeStart;
	}
	public Integer getPersonHeightTotalInchesRangeEnd() {
		return personHeightTotalInchesRangeEnd;
	}
	public void setPersonHeightTotalInchesRangeEnd(Integer personHeightTotalInchesRangeEnd) {
		this.personHeightTotalInchesRangeEnd = personHeightTotalInchesRangeEnd;
	}
	
	public String toString() {
		  StringBuilder result = new StringBuilder();
		  String newLine = System.getProperty("line.separator");

		  result.append( this.getClass().getName() );
		  result.append( " Object {" );
		  result.append(newLine);

		  //determine fields declared in this class only (no fields of superclass)
		  Field[] fields = this.getClass().getDeclaredFields();

		  //print field names paired with their values
		  for ( Field field : fields  ) {
		    result.append("  ");
		    try {
		      result.append( field.getName() );
		      result.append(": ");
		      //requires access to private field:
		      result.append( field.get(this) );
		    } catch ( IllegalAccessException ex ) {
		      System.out.println(ex);
		    }
		    result.append(newLine);
		  }
		  result.append("}");

		  return result.toString();
		}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getOnBehalfOf() {
		return onBehalfOf;
	}
	public void setOnBehalfOf(String onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}
    public PersonName getAlias() {
        return alias;
    }
    public void setAlias(PersonName alias) {
        this.alias = alias;
    }
    public PersonName getParentName() {
        return parentName;
    }
    public void setParentName(PersonName parentName) {
        this.parentName = parentName;
    }
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public String getPlacement() {
        return placement;
    }
    public void setPlacement(String placement) {
        this.placement = placement;
    }
}

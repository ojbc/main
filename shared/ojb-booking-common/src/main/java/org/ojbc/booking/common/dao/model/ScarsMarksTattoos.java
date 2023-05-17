package org.ojbc.booking.common.dao.model;


public class ScarsMarksTattoos {
	
	private Integer id;
	
	private Integer personId;

	private String scarsMarksTattoosDescription;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public String getScarsMarksTattoosDescription() {
		return scarsMarksTattoosDescription;
	}

	public void setScarsMarksTattoosDescription(String scarsMarksTattoosDescription) {
		this.scarsMarksTattoosDescription = scarsMarksTattoosDescription;
	}

	@Override
	public String toString() {
		return "ScarsMarksTattoos [id=" + id + ", personId=" + personId
				+ ", scarsMarksTattoosDescription="
				+ scarsMarksTattoosDescription + "]";
	}
		
}

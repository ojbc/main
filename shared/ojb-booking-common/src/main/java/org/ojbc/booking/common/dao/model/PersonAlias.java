package org.ojbc.booking.common.dao.model;

import java.util.Date;

public class PersonAlias {

	private Integer id;
	
	private Integer personId; 
	
	private String nameType;
	
	private String aliasLastName;
	
	private String aliasFirstName;
	
	private String aliasMiddle;
	
	private String aliasSex;
	
	private Date aliasDob;

	
	public Integer getId() {
		return id;
	}

	public Integer getPersonId() {
		return personId;
	}

	public String getNameType() {
		return nameType;
	}

	public String getAliasLastName() {
		return aliasLastName;
	}

	public String getAliasFirstName() {
		return aliasFirstName;
	}

	public String getAliasMiddle() {
		return aliasMiddle;
	}

	public String getAliasSex() {
		return aliasSex;
	}

	public Date getAliasDob() {
		return aliasDob;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public void setNameType(String nameType) {
		this.nameType = nameType;
	}

	public void setAliasLastName(String aliasLastName) {
		this.aliasLastName = aliasLastName;
	}

	public void setAliasFirstName(String aliasFirstName) {
		this.aliasFirstName = aliasFirstName;
	}

	public void setAliasMiddle(String aliasMiddle) {
		this.aliasMiddle = aliasMiddle;
	}

	public void setAliasSex(String aliasSex) {
		this.aliasSex = aliasSex;
	}

	public void setAliasDob(Date aliasDob) {
		this.aliasDob = aliasDob;
	}

	@Override
	public String toString() {
		return "PersonAlias [id=" + id + ", personId=" + personId
				+ ", nameType=" + nameType + ", aliasLastName=" + aliasLastName
				+ ", aliasFirstName=" + aliasFirstName + ", aliasMiddle="
				+ aliasMiddle + ", aliasSex=" + aliasSex + ", aliasDob="
				+ aliasDob + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((aliasDob == null) ? 0 : aliasDob.hashCode());
		result = prime * result
				+ ((aliasFirstName == null) ? 0 : aliasFirstName.hashCode());
		result = prime * result
				+ ((aliasLastName == null) ? 0 : aliasLastName.hashCode());
		result = prime * result
				+ ((aliasMiddle == null) ? 0 : aliasMiddle.hashCode());
		result = prime * result
				+ ((aliasSex == null) ? 0 : aliasSex.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((nameType == null) ? 0 : nameType.hashCode());
		result = prime * result
				+ ((personId == null) ? 0 : personId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonAlias other = (PersonAlias) obj;
		if (aliasDob == null) {
			if (other.aliasDob != null)
				return false;
		} else if (!aliasDob.equals(other.aliasDob))
			return false;
		if (aliasFirstName == null) {
			if (other.aliasFirstName != null)
				return false;
		} else if (!aliasFirstName.equals(other.aliasFirstName))
			return false;
		if (aliasLastName == null) {
			if (other.aliasLastName != null)
				return false;
		} else if (!aliasLastName.equals(other.aliasLastName))
			return false;
		if (aliasMiddle == null) {
			if (other.aliasMiddle != null)
				return false;
		} else if (!aliasMiddle.equals(other.aliasMiddle))
			return false;
		if (aliasSex == null) {
			if (other.aliasSex != null)
				return false;
		} else if (!aliasSex.equals(other.aliasSex))
			return false;
		if (id != other.id)
			return false;
		if (nameType == null) {
			if (other.nameType != null)
				return false;
		} else if (!nameType.equals(other.nameType))
			return false;
		if (personId == null) {
			if (other.personId != null)
				return false;
		} else if (!personId.equals(other.personId))
			return false;
		return true;
	}
	
}

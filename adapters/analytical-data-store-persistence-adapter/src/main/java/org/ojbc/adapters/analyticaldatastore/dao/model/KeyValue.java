package org.ojbc.adapters.analyticaldatastore.dao.model;
import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class KeyValue implements Serializable
{
    private static final long serialVersionUID = -6279067862222444293L;
    private Long key;
	private String value;

	public KeyValue() {
	}

	public KeyValue(Long key, String value) {
		setKey(key);
		setValue(value);
	}

	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != getClass())
			return false;

		KeyValue kv = (KeyValue) obj;
		return this.getKey().equals(kv.getKey()) && this.getValue().equals(kv.getValue());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);	
	}
}
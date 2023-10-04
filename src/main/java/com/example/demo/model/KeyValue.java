package com.example.demo.model;

import java.nio.ByteBuffer;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@UserDefinedType("keyvalue")
public class KeyValue {
	@Column("key")
	private String key;
	
	@Column("value_type")
	private String valueType;
	
	@Column("value_string")
	private String valueString;
	
	@Column("value_bool")
	private Boolean valueBool;
	
	@Column("value_long")
	private Long valueLong;
	
	@Column("value_double")
	private Double valueDouble;
	
	@Column("value_binary")
	private ByteBuffer valueBinary;

	public KeyValue(String key, String valueType, String valueString, Boolean valueBool, Long valueLong,
			Double valueDouble, ByteBuffer valueBinary) {
		super();
		this.key = key;
		this.valueType = valueType;
		this.valueString = valueString;
		this.valueBool = valueBool;
		this.valueLong = valueLong;
		this.valueDouble = valueDouble;
		this.valueBinary = valueBinary;
	}
	
	public KeyValue() {
		// TODO Auto-generated constructor stub
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getValueString() {
		return valueString;
	}

	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	public Boolean getValueBool() {
		return valueBool;
	}

	public void setValueBool(Boolean valueBool) {
		this.valueBool = valueBool;
	}

	public Long getValueLong() {
		return valueLong;
	}

	public void setValueLong(Long valueLong) {
		this.valueLong = valueLong;
	}

	public Double getValueDouble() {
		return valueDouble;
	}

	public void setValueDouble(Double valueDouble) {
		this.valueDouble = valueDouble;
	}

	public ByteBuffer getValueBinary() {
		return valueBinary;
	}

	public void setValueBinary(ByteBuffer valueBinary) {
		this.valueBinary = valueBinary;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("KeyValue [key=");
		builder.append(key);
		builder.append(", valueType=");
		builder.append(valueType);
		builder.append(", valueString=");
		builder.append(valueString);
		builder.append(", valueBool=");
		builder.append(valueBool);
		builder.append(", valueLong=");
		builder.append(valueLong);
		builder.append(", valueDouble=");
		builder.append(valueDouble);
		builder.append(", valueBinary=");
		builder.append(valueBinary);
		builder.append("]");
		return builder.toString();
	}
}

package com.example.demo.dto;

public class Field {
	private String field_name;
	private String type;
	public Field(String field_name, String type) {
		super();
		this.field_name = field_name;
		this.type = type;
	}
	public Field() {
		// TODO Auto-generated constructor stub
	}
	public String getField_name() {
		return field_name;
	}
	public void setField_name(String field_name) {
		this.field_name = field_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return String.format("Field [field_name=%s, type=%s]", field_name, type);
	}
}

package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Field {
	private String field_name;
	private String type;
	@JsonInclude(Include.NON_NULL)
	private String color;
	public Field(String field_name, String type) {
		super();
		this.field_name = field_name;
		this.type = type;
	}
	public Field(String field_name, String type, String color) {
		super();
		this.field_name = field_name;
		this.type = type;
		this.color = color;
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
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}

package com.example.demo.model;

import java.util.List;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Frozen;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@UserDefinedType("log")
public class Log {
	@Column("ts")
	private Long timestamp;
	
	@Column("fields")
	@Frozen
	private List<KeyValue> fields;

	public Log(Long timestamp, @Frozen List<KeyValue> fields) {
		super();
		this.timestamp = timestamp;
		this.fields = fields;
	}
	
	public Log() {
		// TODO Auto-generated constructor stub
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public List<KeyValue> getFields() {
		return fields;
	}

	public void setFields(List<KeyValue> fields) {
		this.fields = fields;
	}
}

package com.example.demo.model;

import java.util.List;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Frozen;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@UserDefinedType("process")
public class Process {
	@Column("service_name")
	private String serviceName;
	
	@Column("tags")
	@Frozen
	private List<KeyValue> tags;

	public Process(String serviceName, @Frozen List<KeyValue> tags) {
		super();
		this.serviceName = serviceName;
		this.tags = tags;
	}
	
	public Process() {
		// TODO Auto-generated constructor stub
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public List<KeyValue> getTags() {
		return tags;
	}

	public void setTags(List<KeyValue> tags) {
		this.tags = tags;
	}
}

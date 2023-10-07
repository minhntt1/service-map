package com.example.demo.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("service_names")
public class ServiceNames {
	@PrimaryKey(value = "service_name")
	private String serviceName;
	
	public ServiceNames() {
		// TODO Auto-generated constructor stub
	}

	public ServiceNames(String serviceName) {
		super();
		this.serviceName = serviceName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}

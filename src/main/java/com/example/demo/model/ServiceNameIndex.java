package com.example.demo.model;

import java.nio.ByteBuffer;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("service_name_index")
public class ServiceNameIndex {
	@PrimaryKeyClass
	public class ServiceNameIndexKey {
		@PrimaryKeyColumn(name = "service_name", type = PrimaryKeyType.PARTITIONED)
		private String serviceName;
		@PrimaryKeyColumn(name = "bucket", type = PrimaryKeyType.PARTITIONED)
		private Integer bucket;
		@PrimaryKeyColumn(name = "start_time", ordinal = 0, ordering = Ordering.DESCENDING)
		private Long startTime;
	}
	
	@PrimaryKey
	private ServiceNameIndexKey indexKey;
	
	@Column(value = "trace_id")
	private ByteBuffer traceId;

	public ServiceNameIndex(ServiceNameIndexKey indexKey, ByteBuffer traceId) {
		super();
		this.indexKey = indexKey;
		this.traceId = traceId;
	}

	public ServiceNameIndexKey getIndexKey() {
		return indexKey;
	}

	public void setIndexKey(ServiceNameIndexKey indexKey) {
		this.indexKey = indexKey;
	}

	public ByteBuffer getTraceId() {
		return traceId;
	}

	public void setTraceId(ByteBuffer traceId) {
		this.traceId = traceId;
	}
}

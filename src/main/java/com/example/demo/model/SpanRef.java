package com.example.demo.model;

import java.nio.ByteBuffer;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@UserDefinedType("span_ref")
public class SpanRef {
	@Column("ref_type")
	private String refType;
	
	@Column("trace_id")
	private ByteBuffer traceId;
	
	@Column("span_id")
	private Long spanId;
	
	public SpanRef() {
		// TODO Auto-generated constructor stub
	}

	public SpanRef(String refType, ByteBuffer traceId, Long spanId) {
		super();
		this.refType = refType;
		this.traceId = traceId;
		this.spanId = spanId;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public ByteBuffer getTraceId() {
		return traceId;
	}

	public void setTraceId(ByteBuffer traceId) {
		this.traceId = traceId;
	}

	public Long getSpanId() {
		return spanId;
	}

	public void setSpanId(Long spanId) {
		this.spanId = spanId;
	}
}

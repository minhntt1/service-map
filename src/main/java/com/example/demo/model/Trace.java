package com.example.demo.model;

import java.nio.ByteBuffer;
import java.util.List;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Frozen;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("traces")
public class Trace {
	@PrimaryKeyClass
	public static class TracePK{
		@PrimaryKeyColumn(name="trace_id",type = PrimaryKeyType.PARTITIONED)
		private ByteBuffer traceId;
		@PrimaryKeyColumn(name="span_id",ordinal=0)
		private Long spanId;
		@PrimaryKeyColumn(name="span_hash",ordinal=1)
		private Long spanHash;
		public TracePK(ByteBuffer traceId, Long spanId, Long spanHash) {
			super();
			this.traceId = traceId;
			this.spanId = spanId;
			this.spanHash = spanHash;
		}
		public TracePK() {
			super();
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
		public Long getSpanHash() {
			return spanHash;
		}
		public void setSpanHash(Long spanHash) {
			this.spanHash = spanHash;
		}
		@Override
		public String toString() {
			return String.format("TracePK [traceId=%s, spanId=%s, spanHash=%s]", traceId, spanId, spanHash);
		}
	}
	
	@PrimaryKey
	private TracePK pk;
	
	@Column("duration")
	private Long duration;
	
	@Column("flags")
	private Integer flags;
	
	@Column("logs")
	@Frozen
	private List<Log> logs;
	
	@Column("operation_name")
	private String operationName;
	
	@Column("parent_id")
	private Long parentId;
	
	@Column("process")
	@Frozen
	private Process process;
	
	@Column
	@Frozen
	private List<SpanRef> refs;
	
	@Column("start_time")
	private Long startTime;
	
	@Column("tags")
	@Frozen
	private List<KeyValue> tags;

	public Trace(TracePK pk, Long duration, Integer flags, @Frozen List<Log> logs, String operationName, Long parentId,
			@Frozen Process process, @Frozen List<SpanRef> refs, Long startTime, @Frozen List<KeyValue> tags) {
		super();
		this.pk = pk;
		this.duration = duration;
		this.flags = flags;
		this.logs = logs;
		this.operationName = operationName;
		this.parentId = parentId;
		this.process = process;
		this.refs = refs;
		this.startTime = startTime;
		this.tags = tags;
	}
	
	public Trace() {
		// TODO Auto-generated constructor stub
	}

	public TracePK getPk() {
		return pk;
	}

	public void setPk(TracePK pk) {
		this.pk = pk;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Integer getFlags() {
		return flags;
	}

	public void setFlags(Integer flags) {
		this.flags = flags;
	}

	public List<Log> getLogs() {
		return logs;
	}

	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public List<SpanRef> getRefs() {
		return refs;
	}

	public void setRefs(List<SpanRef> refs) {
		this.refs = refs;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public List<KeyValue> getTags() {
		return tags;
	}

	public void setTags(List<KeyValue> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return String.format(
				"Trace [pk=%s, duration=%s, flags=%s, logs=%s, operationName=%s, parentId=%s, process=%s, refs=%s, startTime=%s, tags=%s]",
				pk, duration, flags, logs, operationName, parentId, process, refs, startTime, tags);
	}
}

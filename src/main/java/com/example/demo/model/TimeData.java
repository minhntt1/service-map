package com.example.demo.model;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("v4_time_data")
public class TimeData {
	@PrimaryKeyClass
	public static class Pk{
		@PrimaryKeyColumn(name = "bucket", type = PrimaryKeyType.PARTITIONED)
		Long bucket;
		@PrimaryKeyColumn(name = "ts", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
		Long ts;
		@PrimaryKeyColumn(name = "ts_server", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
		Long tsServer;
		public Pk(Long bucket, Long ts, Long tsServer) {
			super();
			this.bucket = bucket;
			this.ts = ts;
			this.tsServer = tsServer;
		}
		public Long getBucket() {
			return bucket;
		}
		public void setBucket(Long bucket) {
			this.bucket = bucket;
		}
		public Long getTs() {
			return ts;
		}
		public void setTs(Long ts) {
			this.ts = ts;
		}
		public Long getTsServer() {
			return tsServer;
		}
		public void setTsServer(Long tsServer) {
			this.tsServer = tsServer;
		}
	}
	
	@PrimaryKey
	private Pk pk;
	
	@Column(value = "client")
	private String client;
	
	@Column(value = "conn_type")
	private String connType;
	
	@Column(value = "server")
	private String server;
	
	@Column(value = "server_dur")
	private Long serverDur;
	
	@Column(value = "server_err")
	private Boolean serverErr;

	
	public TimeData() {
		// TODO Auto-generated constructor stub
	}
	
	public TimeData(Pk pk, String client, String connType, String server, Long serverDur, Boolean serverErr) {
		super();
		this.pk = pk;
		this.client = client;
		this.connType = connType;
		this.server = server;
		this.serverDur = serverDur;
		this.serverErr = serverErr;
	}

	public Pk getPk() {
		return pk;
	}

	public void setPk(Pk pk) {
		this.pk = pk;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getConnType() {
		return connType;
	}

	public void setConnType(String connType) {
		this.connType = connType;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public Long getServerDur() {
		return serverDur;
	}

	public void setServerDur(Long serverDur) {
		this.serverDur = serverDur;
	}

	public Boolean getServerErr() {
		return serverErr;
	}

	public void setServerErr(Boolean serverErr) {
		this.serverErr = serverErr;
	}
}

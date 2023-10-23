package com.example.demo.repo;

import org.springframework.stereotype.Repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;

@Repository
public class TimeDataRepo {
	private final CqlSession cqlSession;
	private final PreparedStatement preparedStatement;
	
	public TimeDataRepo(CqlSession cqlSession) {
		// TODO Auto-generated constructor stub
		this.cqlSession = cqlSession;
		this.preparedStatement = this.cqlSession.prepare("select client,server,conn_type,server_dur,server_err,show_reverse from v4_time_data where bucket=? and ts>=? and ts<=?");
	}
	
	public ResultSet getResults(Long bucket, Long start, Long end){
		return this.cqlSession.execute(this.preparedStatement.bind(bucket, start, end));
	}
}

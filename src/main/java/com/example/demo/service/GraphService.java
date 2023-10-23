package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.example.demo.component.Calculation;
import com.example.demo.dto.Edge;
import com.example.demo.dto.GraphData;
import com.example.demo.dto.Node;
import com.example.demo.repo.TimeDataRepo;


@Service
public class GraphService {
	private final long bucketSize = (long) 3e11;
	private final TimeDataRepo timeDataRepo;
	
	public GraphService(TimeDataRepo timeDataRepo) {
		// TODO Auto-generated constructor stub
		this.timeDataRepo = timeDataRepo;
	}

	public GraphData graphData(Long start, Long end) {
		long curr = System.currentTimeMillis();
		start = start == null ? curr - 1800000 : start;
		end = end == null ? curr : end;
		start *= 1000000;
		end *= 1000000;
		
		List<Long> buckets = Calculation.calBuckets(this.bucketSize, start, end);
		HashSet<Edge> hashSet = new HashSet<Edge>();
		Map<String, double[]> hashMap2 = new HashMap<String, double[]>();
		
		for(Long bucket:buckets) {
			ResultSet resultSet = this.timeDataRepo.getResults(bucket, start, end);
			for(Row row:resultSet) {
				String client = row.getString("client");
				String server = row.getString("server");
				String connType = row.getString("conn_type");
				Boolean serverErr = row.getBoolean("server_err");
				Long serverDur = row.getLong("server_dur");
				Boolean showReverse = row.getBoolean("show_reverse");
				
				if(showReverse)
					hashSet.add(new Edge(hashSet.size()+1, server, client, connType));
				else
					hashSet.add(new Edge(hashSet.size()+1, client, server, connType));
				
				if(!hashMap2.containsKey(client))
					hashMap2.put(client, new double[] {0,0,0});
				if(!hashMap2.containsKey(server))
					hashMap2.put(server, new double[] {0,0,0});
				
				double[] valServer = hashMap2.get(server);
				++valServer[0];
				if(serverErr)
					++valServer[1];
				valServer[2] += serverDur;
			}
		}
		
		List<Edge> edges = new ArrayList<Edge>(hashSet);
		List<Node> nodes = new ArrayList<Node>();
		
		/*
		 * valServer[0]: total call
		 * valServer[1]: total call err
		 * valServer[2]: total dur (nano)
		 * */
		//mainStat = avg resp (secs)
		//second = req/min
		//subtitle = err rate
		
		double rangeS = (end-start)/1e9;	//to second
		hashMap2.forEach((k,v)->{
			nodes.add(new Node(
					k, 
					k, 
					v[0]!=0 ? Calculation.round(v[2]/v[0]/1e6) : 0, 
					rangeS!=0 ? Calculation.round(v[0]*60/rangeS) : 0, 
					v[0]!=0 ? v[1]/v[0] : 0
			));
		});
		
		return new GraphData(edges, nodes);
	}
}

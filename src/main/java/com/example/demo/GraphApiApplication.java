package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.repo.TimeDataRepo;

@SpringBootApplication
public class GraphApiApplication {
	public static void main(String[] args) {
		System.setProperty("datastax-java-driver.basic.request.timeout", "300 seconds");
		SpringApplication.run(GraphApiApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(TimeDataRepo timeDataRepo) {
		return runner->{
//			Long bucketSize = (long)3e11;
//			Long start = 1698149820000l;
//			Long end = 1698150600000l;
//			start *= 1000000;
//			end *= 1000000;
//			
//			List<Long> buckets = Calculation.calBuckets(bucketSize, start, end);
//			HashSet<Edge> hashSet = new HashSet<Edge>();
//			Map<String, double[]> hashMap2 = new HashMap<String, double[]>();
//			
//			for(Long bucket:buckets) {
//				ResultSet resultSet = timeDataRepo.getResults(bucket, start, end);
//				for(Row row:resultSet) {
//					String client = row.getString("client");
//					String server = row.getString("server");
//					String connType = row.getString("conn_type");
//					Boolean serverErr = row.getBoolean("server_err");
//					Long serverDur = row.getLong("server_dur");
//					Boolean showReverse = row.getBoolean("show_reverse");
//					
//					if(showReverse)
//						hashSet.add(new Edge(hashSet.size()+1, server, client, connType));
//					else
//						hashSet.add(new Edge(hashSet.size()+1, client, server, connType));
//					
//					if(!hashMap2.containsKey(client))
//						hashMap2.put(client, new double[] {0,0,0});
//					if(!hashMap2.containsKey(server))
//						hashMap2.put(server, new double[] {0,0,0});
//					
//					double[] valServer = hashMap2.get(server);
//					++valServer[0];
//					if(serverErr)
//						++valServer[1];
//					valServer[2] += serverDur;
//				}
//			}
//			
//			System.out.println();
		};
	}
}

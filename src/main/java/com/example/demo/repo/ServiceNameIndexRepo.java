package com.example.demo.repo;

import java.util.stream.Stream;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.ServiceNameIndex;
import com.example.demo.model.ServiceNameIndex.ServiceNameIndexKey;

public interface ServiceNameIndexRepo extends CrudRepository<ServiceNameIndex, ServiceNameIndexKey> {
	@Query("select trace_id from service_name_index where service_name = :service and bucket in (0,1,2,3,4,5,6,7,8,9) and start_time>=:start and start_time<=:end")
	Stream<ServiceNameIndex> findByTimeRange(@Param("service") String service, @Param("start") Long start,@Param("end") Long end);
}

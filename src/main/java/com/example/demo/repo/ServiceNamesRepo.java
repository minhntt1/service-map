package com.example.demo.repo;

import java.util.stream.Stream;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.ServiceNames;

public interface ServiceNamesRepo extends CrudRepository<ServiceNames, String> {
	@Query("select * from service_names")
	Stream<ServiceNames> findAllSvc();
}

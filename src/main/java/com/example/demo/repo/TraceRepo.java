package com.example.demo.repo;

import java.nio.ByteBuffer;
import java.util.stream.Stream;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Trace;
import com.example.demo.model.Trace.TracePK;

@Repository
public interface TraceRepo extends CrudRepository<Trace, TracePK> {
    @Query("select span_id,duration,process,refs,start_time,tags from traces where trace_id = :traceId")
    Stream<Trace> findByTraceId(@Param("traceId") ByteBuffer traceId);
}

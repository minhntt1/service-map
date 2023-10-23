package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.GraphData;
import com.example.demo.dto.GraphFields;
import com.example.demo.service.GraphService;

@RestController
@RequestMapping("/api")
public class GraphController {
	private final GraphService service;

	public GraphController(GraphService service) {
		// TODO Auto-generated constructor stub
		this.service = service;
	}

	@GetMapping("/health")
	public ResponseEntity<Boolean> getHealth() {
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	@GetMapping("/graph/fields")
	public ResponseEntity<GraphFields> getFields(GraphFields graphFields) {
		return new ResponseEntity<GraphFields>(graphFields, HttpStatus.OK);
	}

	@GetMapping("/graph/data")
	public ResponseEntity<GraphData> getData(
			@RequestParam(required = false) Long start,
			@RequestParam(required = false) Long end
		) {
		return new ResponseEntity<GraphData>(this.service.graphData(start, end), HttpStatus.OK);
	}
}

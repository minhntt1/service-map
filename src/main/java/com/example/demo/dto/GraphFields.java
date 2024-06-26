package com.example.demo.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class GraphFields {
	private List<Field> edges_fields; 
	private List<Field> nodes_fields;
	public GraphFields() {
		// TODO Auto-generated constructor stub
		this.edges_fields=new ArrayList<Field>(Arrays.asList(
				new Field("id", "number"),
				new Field("source", "number"),
				new Field("target", "number")
		));
		this.nodes_fields=new ArrayList<Field>(Arrays.asList(
				new Field("id", "number"),
				new Field("title", "string"),
				new Field("mainStat", "number"),
				new Field("secondaryStat", "number"),
				new Field("subTitle","number"),
				new Field("arc__success", "number", "green"),
				new Field("arc__error", "number", "red")
		));
	}
	public List<Field> getEdges_fields() {
		return edges_fields;
	}
	public void setEdges_fields(List<Field> edges_fields) {
		this.edges_fields = edges_fields;
	}
	public List<Field> getNodes_fields() {
		return nodes_fields;
	}
	public void setNodes_fields(List<Field> nodes_fields) {
		this.nodes_fields = nodes_fields;
	}
	@Override
	public String toString() {
		return String.format("GraphFields [edges_fields=%s, nodes_fields=%s]", edges_fields, nodes_fields);
	}
}

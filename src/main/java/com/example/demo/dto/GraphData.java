package com.example.demo.dto;

import java.util.List;

public class GraphData {
	List<Edge> edges;
	List<Node> nodes;
	public GraphData(List<Edge> edges, List<Node> nodes) {
		super();
		this.edges = edges;
		this.nodes = nodes;
	}
	public GraphData() {
		// TODO Auto-generated constructor stub
	}
	public List<Edge> getEdges() {
		return edges;
	}
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
	public List<Node> getNodes() {
		return nodes;
	}
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
	@Override
	public String toString() {
		return String.format("GraphData [edges=%s, nodes=%s]", edges, nodes);
	}
}

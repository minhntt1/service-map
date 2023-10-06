package com.example.demo.dto;

public class Edge {
	private Integer id;
	private Integer source;
	private Integer target;
	public Edge(Integer id, Integer source, Integer target) {
		super();
		this.id = id;
		this.source = source;
		this.target = target;
	}
	public Edge() {
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public Integer getTarget() {
		return target;
	}
	public void setTarget(Integer target) {
		this.target = target;
	}
}

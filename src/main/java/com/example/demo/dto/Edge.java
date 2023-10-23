package com.example.demo.dto;

import java.util.Objects;

public class Edge {
	private Integer id;
	private String source;
	private String target;
	private String mainStat;
	public Edge(Integer id, String source, String target, String mainStat) {
		super();
		this.id = id;
		this.source = source;
		this.target = target;
		this.mainStat = mainStat;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getMainStat() {
		return mainStat;
	}
	public void setMainStat(String mainStat) {
		this.mainStat = mainStat;
	}
	@Override
	public int hashCode() {
		return Objects.hash(mainStat, source, target);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		return Objects.equals(mainStat, other.mainStat) && Objects.equals(source, other.source)
				&& Objects.equals(target, other.target);
	}
}

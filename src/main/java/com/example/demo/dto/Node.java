package com.example.demo.dto;

public class Node {
	private Integer id;
	private String title;
	private Double mainStat;
	private Double secondaryStat;
	private Double subTitle;
	public Node(Integer id, String title, Double mainStat, Double secondaryStat, Double subTitle) {
		super();
		this.id = id;
		this.title = title;
		this.mainStat = mainStat;
		this.secondaryStat = secondaryStat;
		this.subTitle = subTitle;
	}
	public Node() {
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Double getMainStat() {
		return mainStat;
	}
	public void setMainStat(Double mainStat) {
		this.mainStat = mainStat;
	}
	public Double getSecondaryStat() {
		return secondaryStat;
	}
	public void setSecondaryStat(Double secondaryStat) {
		this.secondaryStat = secondaryStat;
	}
	public Double getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(Double subTitle) {
		this.subTitle = subTitle;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Node [id=");
		builder.append(id);
		builder.append(", title=");
		builder.append(title);
		builder.append(", mainStat=");
		builder.append(mainStat);
		builder.append(", secondaryStat=");
		builder.append(secondaryStat);
		builder.append(", subTitle=");
		builder.append(subTitle);
		builder.append("]");
		return builder.toString();
	}
}

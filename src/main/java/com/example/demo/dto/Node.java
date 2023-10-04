package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Node {
	private Integer id;
	private String title;
	private Double mainStat;
	private Double secondaryStat;
	private Double subTitle;
	@JsonProperty(value = "arc__success")
	private Double arcSuccess;
	@JsonProperty(value = "arc__error")
	private Double arcError;
	public Node(Integer id, String title, Double mainStat, Double secondaryStat, Double subTitle) {
		super();
		this.id = id;
		this.title = title;
		this.mainStat = mainStat;
		this.secondaryStat = secondaryStat;
		this.subTitle = subTitle;
		this.arcError = subTitle;
		this.arcSuccess = 1 - subTitle;
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
	public Double getArcSuccess() {
		return arcSuccess;
	}
	public void setArcSuccess(Double arcSuccess) {
		this.arcSuccess = arcSuccess;
	}
	public Double getArcError() {
		return arcError;
	}
	public void setArcError(Double arcError) {
		this.arcError = arcError;
	}
}

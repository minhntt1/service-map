package com.example.demo.dto;

import com.example.demo.component.MainStatSerializer;
import com.example.demo.component.SecondaryStatSerializer;
import com.example.demo.component.SubtitleSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Node {
	private String id;
	private String title;
	@JsonSerialize(using = MainStatSerializer.class)
	private Double mainStat;
	@JsonSerialize(using = SecondaryStatSerializer.class)
	private Double secondaryStat;
	@JsonSerialize(using = SubtitleSerializer.class)
	private Double subTitle;
	@JsonProperty(value = "arc__success")
	private Double arcSuccess;
	@JsonProperty(value = "arc__error")
	private Double arcError;
	public Node(String id, String title, Double mainStat, Double secondaryStat, Double subTitle) {
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
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

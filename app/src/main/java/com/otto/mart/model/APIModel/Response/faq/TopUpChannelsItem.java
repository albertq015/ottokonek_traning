package com.otto.mart.model.APIModel.Response.faq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TopUpChannelsItem{
	private List<TopUpInstructionsItem> top_up_instructions;
	private String logo_url;
	private String name;
	private int id;

	public List<TopUpInstructionsItem> getTop_up_instructions() {
		return top_up_instructions;
	}

	public TopUpChannelsItem setTop_up_instructions(List<TopUpInstructionsItem> top_up_instructions) {
		this.top_up_instructions = top_up_instructions;
		return this;
	}

	public String getLogo_url() {
		return logo_url;
	}

	public TopUpChannelsItem setLogo_url(String logo_url) {
		this.logo_url = logo_url;
		return this;
	}

	public String getName() {
		return name;
	}

	public TopUpChannelsItem setName(String name) {
		this.name = name;
		return this;
	}

	public int getId() {
		return id;
	}

	public TopUpChannelsItem setId(int id) {
		this.id = id;
		return this;
	}
}
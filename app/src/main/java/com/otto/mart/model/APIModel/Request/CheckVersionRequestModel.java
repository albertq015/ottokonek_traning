package com.otto.mart.model.APIModel.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckVersionRequestModel{

	@JsonProperty("application_id")
	private String application_id;

	@JsonProperty("version_app")
	private int version_app;

	public CheckVersionRequestModel(String applicationId, int version_app) {
		this.application_id = applicationId;
		this.version_app = version_app;
	}

	public CheckVersionRequestModel() {

	}

	public String getApplication_id() {
		return application_id;
	}

	public void setApplication_id(String application_id) {
		this.application_id = application_id;
	}

	public void setVersionApp(int versionApp){
		this.version_app = versionApp;
	}

	public int getVersionApp(){
		return version_app;
	}
}

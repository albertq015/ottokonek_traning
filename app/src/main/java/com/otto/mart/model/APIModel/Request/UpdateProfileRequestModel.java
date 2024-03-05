package com.otto.mart.model.APIModel.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateProfileRequestModel {

	@JsonProperty("profile_picture")
	private String profile_picture;

	@JsonProperty("email")
	private String email;

	public String getProfile_picture() {
		return profile_picture;
	}

	public void setProfile_picture(String profile_picture) {
		this.profile_picture = profile_picture;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
package com.otto.mart.model.APIModel.Response.olshop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImagesItem{
	private String image;
	private String imageThumb;

	public ImagesItem() {
	}

	public ImagesItem(String image, String imageThumb) {
		this.image = image;
		this.imageThumb = imageThumb;
	}

	public ImagesItem(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImageThumb() {
		return imageThumb;
	}

	public void setImageThumb(String imageThumb) {
		this.imageThumb = imageThumb;
	}
}

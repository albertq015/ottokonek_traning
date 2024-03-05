package com.otto.mart.model.APIModel.Response.olshop.product;

public class Logo{
	private Small small;
	private Big big;
	private Thumb thumb;
	private String url;

	public void setSmall(Small small){
		this.small = small;
	}

	public Small getSmall(){
		return small;
	}

	public void setBig(Big big){
		this.big = big;
	}

	public Big getBig(){
		return big;
	}

	public void setThumb(Thumb thumb){
		this.thumb = thumb;
	}

	public Thumb getThumb(){
		return thumb;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}
}

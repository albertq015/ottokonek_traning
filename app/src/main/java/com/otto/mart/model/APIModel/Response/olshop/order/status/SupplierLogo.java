package com.otto.mart.model.APIModel.Response.olshop.order.status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.olshop.product.Big;
import com.otto.mart.model.APIModel.Response.olshop.product.Small;
import com.otto.mart.model.APIModel.Response.olshop.product.Thumb;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierLogo{
	private Small small;
	private Big big;
	private Thumb thumb;
	private String url;

	public Small getSmall() {
		return small;
	}

	public void setSmall(Small small) {
		this.small = small;
	}

	public Big getBig() {
		return big;
	}

	public void setBig(Big big) {
		this.big = big;
	}

	public Thumb getThumb() {
		return thumb;
	}

	public void setThumb(Thumb thumb) {
		this.thumb = thumb;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}

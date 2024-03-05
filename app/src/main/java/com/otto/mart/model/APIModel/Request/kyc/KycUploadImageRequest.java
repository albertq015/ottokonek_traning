package com.otto.mart.model.APIModel.Request.kyc;

public class KycUploadImageRequest{
	private String passportPhoto;
	private String idCard;

	public KycUploadImageRequest() {
	}

	public KycUploadImageRequest(String idCard, String passportPhoto) {
		this.passportPhoto = passportPhoto;
		this.idCard = idCard;
	}

	public void setPassportPhoto(String passportPhoto){
		this.passportPhoto = passportPhoto;
	}

	public String getPassportPhoto(){
		return passportPhoto;
	}

	public void setIdCard(String idCard){
		this.idCard = idCard;
	}

	public String getIdCard(){
		return idCard;
	}
}

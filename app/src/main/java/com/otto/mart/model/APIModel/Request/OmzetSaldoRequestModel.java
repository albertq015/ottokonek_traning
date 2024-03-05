package com.otto.mart.model.APIModel.Request;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OmzetSaldoRequestModel implements Parcelable{

	@JsonProperty("amount")
	private long amount;

	@JsonProperty("pin")
	private String pin;

	@JsonProperty("user_id")
	private int userId;

	public OmzetSaldoRequestModel() {
	}

	protected OmzetSaldoRequestModel(Parcel in) {
		amount = in.readLong();
		pin = in.readString();
		userId = in.readInt();
	}

	public static final Creator<OmzetSaldoRequestModel> CREATOR = new Creator<OmzetSaldoRequestModel>() {
		@Override
		public OmzetSaldoRequestModel createFromParcel(Parcel in) {
			return new OmzetSaldoRequestModel(in);
		}

		@Override
		public OmzetSaldoRequestModel[] newArray(int size) {
			return new OmzetSaldoRequestModel[size];
		}
	};

	public void setAmount(int amount){
		this.amount = amount;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public void setPin(String pin){
		this.pin = pin;
	}

	public String getPin(){
		return pin;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(amount);
		dest.writeString(pin);
		dest.writeInt(userId);
	}
}
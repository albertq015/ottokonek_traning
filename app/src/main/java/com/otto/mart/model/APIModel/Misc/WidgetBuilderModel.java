package com.otto.mart.model.APIModel.Misc;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WidgetBuilderModel implements Parcelable {

    private String key;
    private String value;

    public WidgetBuilderModel() {

    }

    protected WidgetBuilderModel(Parcel in) {
        key = in.readString();
        value = in.readString();
    }

    public static final Creator<WidgetBuilderModel> CREATOR = new Creator<WidgetBuilderModel>() {
        @Override
        public WidgetBuilderModel createFromParcel(Parcel in) {
            return new WidgetBuilderModel(in);
        }

        @Override
        public WidgetBuilderModel[] newArray(int size) {
            return new WidgetBuilderModel[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
    }
}

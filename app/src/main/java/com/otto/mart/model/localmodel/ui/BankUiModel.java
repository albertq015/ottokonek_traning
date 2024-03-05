package com.otto.mart.model.localmodel.ui;

import android.os.Parcel;
import android.os.Parcelable;

import com.otto.mart.model.APIModel.Misc.bank.BankListingModel;
import com.otto.mart.model.APIModel.Misc.bank.BankRequestModel;

public class BankUiModel implements Parcelable {
    BankListingModel listModel;
    BankRequestModel requestModel;
    int selectedBankPos;
    int selectedPaytypePos;


    public BankUiModel() {
    }

    protected BankUiModel(Parcel in) {
        listModel = (BankListingModel) in.readParcelable(BankListingModel.class.getClassLoader());
        requestModel = (BankRequestModel) in.readParcelable(BankRequestModel.class.getClassLoader());
        selectedBankPos = in.readInt();
        selectedPaytypePos = in.readInt();
    }

    public static final Creator<BankUiModel> CREATOR = new Creator<BankUiModel>() {
        @Override
        public BankUiModel createFromParcel(Parcel in) {
            return new BankUiModel(in);
        }

        @Override
        public BankUiModel[] newArray(int size) {
            return new BankUiModel[size];
        }
    };

    public BankListingModel getListModel() {
        return listModel;
    }

    public void setListModel(BankListingModel listModel) {
        this.listModel = listModel;
    }

    public BankRequestModel getRequestModel() {
        return requestModel;
    }

    public void setRequestModel(BankRequestModel requestModel) {
        this.requestModel = requestModel;
    }

    public int getSelectedBankPos() {
        return selectedBankPos;
    }

    public void setSelectedBankPos(int selectedBankPos) {
        this.selectedBankPos = selectedBankPos;
    }

    public int getSelectedPaytypePos() {
        return selectedPaytypePos;
    }

    public void setSelectedPaytypePos(int selectedPaytypePos) {
        this.selectedPaytypePos = selectedPaytypePos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(listModel, flags);
        dest.writeParcelable(requestModel, flags);
        dest.writeInt(selectedBankPos);
        dest.writeInt(selectedPaytypePos);
    }
}

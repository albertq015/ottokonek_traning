package id.ottodigital.data.entity.idm.response;


import id.ottodigital.data.entity.base.BaseResponse;
import id.ottodigital.data.entity.idm.misc.CheckAccountResponseData;

public class CheckAccountResponseModel extends BaseResponse {

    CheckAccountResponseData data;

    public CheckAccountResponseModel() {
    }

    public CheckAccountResponseData getData() {
        return data;
    }

    public void setData(CheckAccountResponseData data) {
        this.data = data;
    }
}

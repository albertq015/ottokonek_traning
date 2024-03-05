package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.SortModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetSortItemRequestModel extends BaseResponseModel {

    public class SortRequestData {
        List<SortModel> sort;

        public List<SortModel> getSort() {
            return sort;
        }

        public void setSort(List<SortModel> sort) {
            this.sort = sort;
        }
    }

    SortRequestData data;

    public SortRequestData getData() {
        return data;
    }

    public void setData(SortRequestData data) {
        this.data = data;
    }
}

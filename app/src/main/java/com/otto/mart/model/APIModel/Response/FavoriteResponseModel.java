package com.otto.mart.model.APIModel.Response;

import androidx.annotation.Keep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Misc.FavoriteItemModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FavoriteResponseModel extends BaseResponseModel {

    @Keep
    public FavoriteResponseModel() {
    }

    @JsonProperty("data")
    private FavoriteDataModel data;

    public FavoriteDataModel getData() {
        return data;
    }

    public void setData(FavoriteDataModel data) {
        this.data = data;
    }

    public List<FavoriteItemModel> getPpob_favourites() {
        return data.getPpob_favourites();
    }

    public void setPpob_favourites(List<FavoriteItemModel> ppob_favourites) {
        data.setPpob_favourites(ppob_favourites);
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class FavoriteDataModel {

    @Keep
    public FavoriteDataModel() {
    }

    @JsonProperty("ppob_favourites")
    private List<FavoriteItemModel> ppob_favourites;

    public List<FavoriteItemModel> getPpob_favourites() {
        return ppob_favourites;
    }

    public void setPpob_favourites(List<FavoriteItemModel> ppob_favourites) {
        this.ppob_favourites = ppob_favourites;
    }
}



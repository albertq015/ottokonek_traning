package com.otto.mart.model.APIModel.Response.tokoOttopay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.tokoOttopay.ReOrderProduct;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReOrderProductResponse extends BaseResponseModel {

   private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Data{
       private List<ReOrderProduct> products;

       public List<ReOrderProduct> getProducts() {
           return products;
       }

       public void setProducts(List<ReOrderProduct> products) {
           this.products = products;
       }
   }
}
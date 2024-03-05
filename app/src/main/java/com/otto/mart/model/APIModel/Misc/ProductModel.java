package com.otto.mart.model.APIModel.Misc;

import java.util.List;

public class ProductModel {
    int id;
    String title;
    PriceModel price;
    List<ProductImageModel> product_images;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PriceModel getPrice() {
        return price;
    }

    public void setPrice(PriceModel price) {
        this.price = price;
    }

    public int getBase_price() {
        return price.base_price;
    }

    public int getDiscount_price() {
        return price.discount_price;
    }

    public String getUnit() {
        return price.unit;
    }

    public class ProductImageModel {
        String image;
        String image_thumb;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImage_thumb() {
            return image_thumb;
        }

        public void setImage_thumb(String image_thumb) {
            this.image_thumb = image_thumb;
        }


    }

    public List<ProductImageModel> getProduct_images() {
        return product_images;
    }

    public void setProduct_images(List<ProductImageModel> product_images) {
        this.product_images = product_images;
    }
}

class PriceModel {
    int base_price;
    int discount_price;
    String unit;

    public int getBase_price() {
        return base_price;
    }

    public void setBase_price(int base_price) {
        this.base_price = base_price;
    }

    public int getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(int discount_price) {
        this.discount_price = discount_price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}



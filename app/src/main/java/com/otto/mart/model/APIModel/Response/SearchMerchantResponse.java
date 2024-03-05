package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchMerchantResponse extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        private Merchant merchant;

        public Merchant getMerchant() {
            return merchant;
        }

        public void setMerchant(Merchant merchant) {
            this.merchant = merchant;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Merchant {

            private String address;
            private String best_visit;
            private String business_location;
            private String business_type;
            private String category_type;
            private String city;
            private String district;
            private long dob;
            private int id;
            private String id_card;
            private ImageIdCard image_id_card;
            private ImageMerchant image_merchant;
            private ImageMerchantLocation image_merchant_location;
            private ImageMerchantLocationAdditional1 image_merchant_location_additional_1;
            private ImageMerchantLocationAdditional2 image_merchant_location_additional_2;
            private boolean indomarco_status;
            private String latitude;
            private String lokasi_usaha;
            private String longitude;
            private String merchant_id;
            private String name;
            private String note;
            private String operation_hour;
            private String owner_name;
            private String phone_area;
            private String phone_number;
            private PhotoMerchantLocation photo_merchant_location;
            private String province;
            private String village;
            private boolean with_device;
            private String merchant_name;
            private int business_category_id;
            private String business_category_name;
            private String business_type_name;
            private int business_type_id;
            private int province_id;
            private int city_id;
            private int district_id;
            private long village_id;
            private String province_name;
            private String city_name;
            private String district_name;
            private String village_name;
            private String complete_address;
            private double address_longitude;
            private double address_latitude;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getBest_visit() {
                return best_visit;
            }

            public void setBest_visit(String best_visit) {
                this.best_visit = best_visit;
            }

            public String getBusiness_location() {
                return business_location;
            }

            public void setBusiness_location(String business_location) {
                this.business_location = business_location;
            }

            public String getBusiness_type() {
                return business_type;
            }

            public void setBusiness_type(String business_type) {
                this.business_type = business_type;
            }

            public String getCategory_type() {
                return category_type;
            }

            public void setCategory_type(String category_type) {
                this.category_type = category_type;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public long getDob() {
                return dob;
            }

            public void setDob(long dob) {
                this.dob = dob;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getId_card() {
                return id_card;
            }

            public void setId_card(String id_card) {
                this.id_card = id_card;
            }

            public ImageIdCard getImage_id_card() {
                return image_id_card;
            }

            public void setImage_id_card(ImageIdCard image_id_card) {
                this.image_id_card = image_id_card;
            }

            public ImageMerchant getImage_merchant() {
                return image_merchant;
            }

            public void setImage_merchant(ImageMerchant image_merchant) {
                this.image_merchant = image_merchant;
            }

            public ImageMerchantLocation getImage_merchant_location() {
                return image_merchant_location;
            }

            public void setImage_merchant_location(ImageMerchantLocation image_merchant_location) {
                this.image_merchant_location = image_merchant_location;
            }

            public ImageMerchantLocationAdditional1 getImage_merchant_location_additional_1() {
                return image_merchant_location_additional_1;
            }

            public void setImage_merchant_location_additional_1(ImageMerchantLocationAdditional1 image_merchant_location_additional_1) {
                this.image_merchant_location_additional_1 = image_merchant_location_additional_1;
            }

            public ImageMerchantLocationAdditional2 getImage_merchant_location_additional_2() {
                return image_merchant_location_additional_2;
            }

            public void setImage_merchant_location_additional_2(ImageMerchantLocationAdditional2 image_merchant_location_additional_2) {
                this.image_merchant_location_additional_2 = image_merchant_location_additional_2;
            }

            public boolean isIndomarco_status() {
                return indomarco_status;
            }

            public void setIndomarco_status(boolean indomarco_status) {
                this.indomarco_status = indomarco_status;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLokasi_usaha() {
                return lokasi_usaha;
            }

            public void setLokasi_usaha(String lokasi_usaha) {
                this.lokasi_usaha = lokasi_usaha;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getMerchant_id() {
                return merchant_id;
            }

            public void setMerchant_id(String merchant_id) {
                this.merchant_id = merchant_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public String getOperation_hour() {
                return operation_hour;
            }

            public void setOperation_hour(String operation_hour) {
                this.operation_hour = operation_hour;
            }

            public String getOwner_name() {
                return owner_name;
            }

            public void setOwner_name(String owner_name) {
                this.owner_name = owner_name;
            }

            public String getPhone_area() {
                return phone_area;
            }

            public void setPhone_area(String phone_area) {
                this.phone_area = phone_area;
            }

            public String getPhone_number() {
                return phone_number;
            }

            public void setPhone_number(String phone_number) {
                this.phone_number = phone_number;
            }

            public PhotoMerchantLocation getPhoto_merchant_location() {
                return photo_merchant_location;
            }

            public void setPhoto_merchant_location(PhotoMerchantLocation photo_merchant_location) {
                this.photo_merchant_location = photo_merchant_location;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getVillage() {
                return village;
            }

            public void setVillage(String village) {
                this.village = village;
            }

            public boolean isWith_device() {
                return with_device;
            }

            public void setWith_device(boolean with_device) {
                this.with_device = with_device;
            }

            public String getMerchant_name() {
                return merchant_name;
            }

            public void setMerchant_name(String merchant_name) {
                this.merchant_name = merchant_name;
            }

            public int getBusiness_category_id() {
                return business_category_id;
            }

            public void setBusiness_category_id(int business_category_id) {
                this.business_category_id = business_category_id;
            }

            public String getBusiness_category_name() {
                return business_category_name;
            }

            public void setBusiness_category_name(String business_category_name) {
                this.business_category_name = business_category_name;
            }

            public String getBusiness_type_name() {
                return business_type_name;
            }

            public void setBusiness_type_name(String business_type_name) {
                this.business_type_name = business_type_name;
            }

            public int getBusiness_type_id() {
                return business_type_id;
            }

            public void setBusiness_type_id(int business_type_id) {
                this.business_type_id = business_type_id;
            }

            public int getProvince_id() {
                return province_id;
            }

            public void setProvince_id(int province_id) {
                this.province_id = province_id;
            }

            public int getCity_id() {
                return city_id;
            }

            public void setCity_id(int city_id) {
                this.city_id = city_id;
            }

            public int getDistrict_id() {
                return district_id;
            }

            public void setDistrict_id(int district_id) {
                this.district_id = district_id;
            }

            public long getVillage_id() {
                return village_id;
            }

            public void setVillage_id(long village_id) {
                this.village_id = village_id;
            }

            public String getProvince_name() {
                return province_name;
            }

            public void setProvince_name(String province_name) {
                this.province_name = province_name;
            }

            public String getCity_name() {
                return city_name;
            }

            public void setCity_name(String city_name) {
                this.city_name = city_name;
            }

            public String getDistrict_name() {
                return district_name;
            }

            public void setDistrict_name(String district_name) {
                this.district_name = district_name;
            }

            public String getVillage_name() {
                return village_name;
            }

            public void setVillage_name(String village_name) {
                this.village_name = village_name;
            }

            public String getComplete_address() {
                return complete_address;
            }

            public void setComplete_address(String complete_address) {
                this.complete_address = complete_address;
            }

            public double getAddress_longitude() {
                return address_longitude;
            }

            public void setAddress_longitude(double address_longitude) {
                this.address_longitude = address_longitude;
            }

            public double getAddress_latitude() {
                return address_latitude;
            }

            public void setAddress_latitude(double address_latitude) {
                this.address_latitude = address_latitude;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ImageIdCard {

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ImageMerchant {

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ImageMerchantLocation {

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ImageMerchantLocationAdditional1 {

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ImageMerchantLocationAdditional2 {

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class PhotoMerchantLocation {

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }
    }
}
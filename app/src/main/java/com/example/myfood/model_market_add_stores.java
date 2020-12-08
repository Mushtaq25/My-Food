package com.example.myfood;

public class model_market_add_stores {

    private String store_Location,store_name,store_image_link;

    public model_market_add_stores() {

    }

    public model_market_add_stores(String store_Location, String store_name, String store_image_link) {
        this.store_Location = store_Location;
        this.store_name = store_name;
        this.store_image_link = store_image_link;
    }

    public String getStore_Location() {
        return store_Location;
    }

    public void setStore_Location(String store_Location) {
        this.store_Location = store_Location;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_image_link() {
        return store_image_link;
    }

    public void setStore_image_link(String store_image_link) {
        this.store_image_link = store_image_link;
    }
}

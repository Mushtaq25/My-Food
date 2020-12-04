package com.example.myfood;

public class model_add_item_existingseller {

    private String Item_Price,Type,category,imagelinkitem1,imagelinkitem2,
            imagelinkitem3,imagelinkitem4,item_cooktime,item_description,item_name;

    public model_add_item_existingseller(){

    }

    public model_add_item_existingseller(String item_Price, String type, String category, String imagelinkitem1, String imagelinkitem2, String imagelinkitem3, String imagelinkitem4, String item_cooktime, String item_description, String item_name) {
        Item_Price = item_Price;
        Type = type;
        this.category = category;
        this.imagelinkitem1 = imagelinkitem1;
        this.imagelinkitem2 = imagelinkitem2;
        this.imagelinkitem3 = imagelinkitem3;
        this.imagelinkitem4 = imagelinkitem4;
        this.item_cooktime = item_cooktime;
        this.item_description = item_description;
        this.item_name = item_name;
    }

    public String getItem_Price() {
        return Item_Price;
    }

    public void setItem_Price(String item_Price) {
        Item_Price = item_Price;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImagelinkitem1() {
        return imagelinkitem1;
    }

    public void setImagelinkitem1(String imagelinkitem1) {
        this.imagelinkitem1 = imagelinkitem1;
    }

    public String getImagelinkitem2() {
        return imagelinkitem2;
    }

    public void setImagelinkitem2(String imagelinkitem2) {
        this.imagelinkitem2 = imagelinkitem2;
    }

    public String getImagelinkitem3() {
        return imagelinkitem3;
    }

    public void setImagelinkitem3(String imagelinkitem3) {
        this.imagelinkitem3 = imagelinkitem3;
    }

    public String getImagelinkitem4() {
        return imagelinkitem4;
    }

    public void setImagelinkitem4(String imagelinkitem4) {
        this.imagelinkitem4 = imagelinkitem4;
    }

    public String getItem_cooktime() {
        return item_cooktime;
    }

    public void setItem_cooktime(String item_cooktime) {
        this.item_cooktime = item_cooktime;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
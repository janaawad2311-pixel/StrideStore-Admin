package com.example.stridestore_admin;

import java.io.Serializable;

public class StrideStore implements Serializable {
    private String id;     // 	رقم الحذاء
    private String name;     // 	اسم الحذاء
    private String brand;     //  	ماركة الحذاء
    private int size;      //   مقاس الحذاء
    private double price;     //  	سعر الحذاء
    private int quantity;     //  الكمية المتوفرة
    private String imageUrl;    // صفة تحميل الصورة


    public StrideStore() {

    }


    public StrideStore(String id, String name, String brand,
                       int size, double price, int quantity) {

        this.id = id;
        this.name = name;
        this.brand = brand;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
    }


    // Get and Set for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Get and Set for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Get and Set for brand
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Get and Set for size
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    // Get and Set for price
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Get and Set for quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}



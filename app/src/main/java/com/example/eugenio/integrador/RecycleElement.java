package com.example.eugenio.integrador;

import java.util.Date;

public class RecycleElement {
    private String mName, mDate, mLocation;
    private int mNumImages;
    public RecycleElement(String name, String date, String location, int numImages) {
        this.mName = name;
        this.mDate = date;
        this.mLocation = location;
        this.mNumImages = numImages;
    }
    public String getName() {
        return this.mName;
    }
    public String getDate() {
        return this.mDate;
    }
}

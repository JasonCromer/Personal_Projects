package com.dev.cromer.jason.takeastand.objects;


public class MarkerPostRequestParams {

    private String url;
    private float latitude;
    private float longitude;
    private int religionID;

    public MarkerPostRequestParams(String url, float latitude, float longitude, int religionID){
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
        this.religionID = religionID;
    }

    public String getUrl() {
        return url;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public int getReligionID() {
        return religionID;
    }

}

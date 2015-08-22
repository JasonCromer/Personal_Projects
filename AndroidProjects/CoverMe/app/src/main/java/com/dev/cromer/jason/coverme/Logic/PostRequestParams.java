package com.dev.cromer.jason.coverme.Logic;




public class PostRequestParams {

    String url;
    String latitude;
    String longitude;
    String markerTitle;

    public PostRequestParams(String url, String latitude, String longitude, String markerTitle) {
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
        this.markerTitle = markerTitle;
    }

    public String getUrl() {
        return this.url;
    }


    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public String getMarkerTitle() {
        return this.markerTitle;
    }
}

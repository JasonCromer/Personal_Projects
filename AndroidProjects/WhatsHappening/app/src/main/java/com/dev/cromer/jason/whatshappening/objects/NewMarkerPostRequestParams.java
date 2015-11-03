package com.dev.cromer.jason.whatshappening.objects;




public class NewMarkerPostRequestParams {

    String url;
    String latitude;
    String longitude;
    String markerTitle;
    String markerDescription;

    public NewMarkerPostRequestParams(String url, String latitude, String longitude, String markerTitle, String markerDescription) {
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
        this.markerTitle = markerTitle;
        this.markerDescription = markerDescription;
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

    public String getMarkerDescription() {
        return this.markerDescription;
    }

}

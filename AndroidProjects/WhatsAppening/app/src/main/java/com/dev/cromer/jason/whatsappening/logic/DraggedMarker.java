package com.dev.cromer.jason.whatsappening.logic;



public class DraggedMarker {

    String latitude;
    String longitude;

    public DraggedMarker(String latitude, String longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDraggedLatitude(){
        return this.latitude;
    }

    public String getDraggedLongitude(){
        return this.longitude;
    }
}

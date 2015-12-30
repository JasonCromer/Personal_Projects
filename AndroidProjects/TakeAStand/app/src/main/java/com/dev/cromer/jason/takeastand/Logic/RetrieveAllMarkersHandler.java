package com.dev.cromer.jason.takeastand.Logic;


import com.dev.cromer.jason.takeastand.networking.GenericHttpGetRequest;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class RetrieveAllMarkersHandler {

    private String url;
    private GenericHttpGetRequest httpGetRequest;
    protected String result;
    protected String[] markerInfoList;
    protected float markerLatitude;
    protected float markerLongitude;
    protected int religionTypeMappedInteger;
    protected MarkerOptions currentMarker;


    //Religion value constants
    private static final int INT_CHRISTIAN = 1;
    private static final int INT_ISLAM = 2;
    private static final int INT_CATHOLIC = 3;
    private static final int INT_HINDU = 4;
    private static final int INT_BUDDHIST = 5;
    private static final int INT_AGNOSTIC = 6;
    private static final int INT_ATHIEST = 7;



    public RetrieveAllMarkersHandler(String url, GenericHttpGetRequest request){
        this.url = url;
        this.httpGetRequest = request;
    }

    public String fetchMarkers(){

        //Attempt GET request
        try{
            result = httpGetRequest.execute(url).get();
        }
        catch(ExecutionException | InterruptedException e){
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public String[] parseMarkerResult(String input){
        if(input != null){

            //Remove jsonified components from retrieved list
            input = input.replace("[","").replace("]","").replace("\"", "").replace(" ","");

            //Convert the input to an Array
            markerInfoList = input.split(",");
        }
        else{
            return null;
        }
        return markerInfoList;
    }

    public HashMap<MarkerOptions, Integer> mapMarkerItems(String[] stringInfoArray){
        /*
            The list passed in is composed of 3 items i.e. [latitude, longitude, integer,....]
            We will use the lat/lng to create a marker and map it to the integer
         */
        HashMap<MarkerOptions, Integer> markersHashMap = new HashMap<>();

        if(stringInfoArray != null){
            for(int i = 0; i < stringInfoArray.length; i+=3) {

                //Lat/lng values must be floats
                markerLatitude = Float.valueOf(stringInfoArray[i]);
                markerLongitude = Float.valueOf(stringInfoArray[i + 1]);
                religionTypeMappedInteger = Integer.valueOf(stringInfoArray[i + 2]);

                //Create a marker and map the religion type to retrieve an appropriate marker Hue
                currentMarker = new MarkerOptions().position(new LatLng(markerLatitude, markerLongitude))
                        .title(getMarkerTitle(religionTypeMappedInteger))
                        .icon(BitmapDescriptorFactory.defaultMarker(getMarkerHue(religionTypeMappedInteger)));

                //Add marker and religion-type integer to hashmap
                markersHashMap.put(currentMarker, religionTypeMappedInteger);
            }
        }
        return markersHashMap;
    }

    private float getMarkerHue(int religionType){
        //return hue depending on religion type
        switch(religionType){
            case INT_CHRISTIAN:
                return BitmapDescriptorFactory.HUE_AZURE;
            case INT_ISLAM:
                return BitmapDescriptorFactory.HUE_MAGENTA;
            case INT_CATHOLIC:
                return BitmapDescriptorFactory.HUE_BLUE;
            case INT_HINDU:
                return BitmapDescriptorFactory.HUE_ROSE;
            case INT_BUDDHIST:
                return BitmapDescriptorFactory.HUE_RED;
            case INT_AGNOSTIC:
                return BitmapDescriptorFactory.HUE_GREEN;
            case INT_ATHIEST:
                return BitmapDescriptorFactory.HUE_YELLOW;
            default:
                return BitmapDescriptorFactory.HUE_CYAN;
        }
    }

    private String getMarkerTitle(int religionType){
        //return hue depending on religion type
        switch(religionType){
            case INT_CHRISTIAN:
                return "Christian";
            case INT_ISLAM:
                return "Islam";
            case INT_CATHOLIC:
                return "Catholic";
            case INT_HINDU:
                return "Hindu";
            case INT_BUDDHIST:
                return "Buddhist";
            case INT_AGNOSTIC:
                return "Agnostic";
            case INT_ATHIEST:
                return "Athiest";
            default:
                return "I Take a Stand!";
        }
    }
}

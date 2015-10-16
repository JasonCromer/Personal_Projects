package com.dev.cromer.jason.takeastand.Logic;


import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class BounceMarkerHandler {
    private Handler handler;
    private long startTime;
    private long elapsedTime;
    private float interpolationTime;
    private double markerFinalLng;
    private double markerFinalLat;
    private Projection projection;
    private LatLng markerInitialLatLng;
    private Marker marker;
    private Point startPoint;
    private LatLng projectionStartLatLng;
    private Interpolator interpolator;

    private static final int duration = 2000;


    public BounceMarkerHandler(Marker marker){
        this.marker = marker;
        this.handler = new Handler();
        interpolator = new BounceInterpolator();
    }


    public void bounce(){

        handler.post(new Runnable() {
            @Override
            public void run() {

                //assign the elapsed time for use in interpolation calculation
                elapsedTime = SystemClock.uptimeMillis() - startTime;

                //Set the interpolation time using elapsed time and duration
                interpolationTime = interpolator.getInterpolation((float) elapsedTime / duration);

                markerFinalLat = interpolationTime * markerInitialLatLng.latitude + (1 - interpolationTime)
                        * projectionStartLatLng.latitude;

                markerFinalLng = interpolationTime * markerInitialLatLng.longitude + (1 - interpolationTime)
                        * projectionStartLatLng.longitude;

                marker.setPosition(new LatLng(markerFinalLat, markerFinalLng));

                if(interpolationTime < 1.0){
                    //Post again 16ms later
                    handler.postDelayed(this, 16);
                }
            }
        });

    }

    //Setters

    public void setStartTime(){
        this.startTime = SystemClock.uptimeMillis();
    }

    public void setMapProjection(GoogleMap mMap){
        this.projection = mMap.getProjection();
    }

    public void setMarkerInitialLatLng(Marker mMarker){
        this.markerInitialLatLng = mMarker.getPosition();
    }

    public void setStartPoint(Marker mMarker){
        final LatLng markerLatLng = mMarker.getPosition();
        if(markerLatLng != null){
            this.startPoint = projection.toScreenLocation(markerLatLng);
        }
    }

    public void setStartPointOffset(int x, int y){
        startPoint.offset(x, y);
    }

    public void setStartBounceLatLng(){
        projectionStartLatLng = projection.fromScreenLocation(startPoint);
    }

}

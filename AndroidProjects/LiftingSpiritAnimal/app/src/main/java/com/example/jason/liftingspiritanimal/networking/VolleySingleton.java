package com.example.jason.liftingspiritanimal.networking;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/*
    This class serves as a singleton to create and manage a Volley http request queue
    throughout the lifecycle of the application.
    Instances are controlled via the "lazy constructor", so that the creation of an instance
    is encapsulated and dictated solely based on whether or not an instance already exists.
 */

public class VolleySingleton {

    private static VolleySingleton thisInstance;
    private RequestQueue thisRequestQueue;
    private static Context thisContext;

    private VolleySingleton(Context context){
        thisContext = context;
        thisRequestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context){
        if(thisInstance == null){
            thisInstance = new VolleySingleton(context);
        }

        return thisInstance;
    }

    public RequestQueue getRequestQueue(){
        if(thisRequestQueue == null){
            thisRequestQueue = Volley.newRequestQueue(thisContext.getApplicationContext());
        }

        return thisRequestQueue;
    }
}

package com.example.jason.liftingspiritanimal.networking;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton thisInstance;
    private RequestQueue thisRequestQueue;
    private static Context thisContext;
    private static final String GET_REQUEST_TAG = "get_request";

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

    public void destroyRequestQueue(){
        if(thisRequestQueue != null){
            thisRequestQueue.cancelAll(GET_REQUEST_TAG);
        }
    }
}

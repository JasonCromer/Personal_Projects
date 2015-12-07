package com.dev.cromer.jason.whatshappening.networking;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyQueueSingleton {

    private static VolleyQueueSingleton thisInstance;
    private RequestQueue thisRequestQueue;
    private static Context thisContext;
    private static final String GET_REQUEST_TAG = "GetRequest";


    private VolleyQueueSingleton(Context context){
        thisContext = context;
        thisRequestQueue = getRequestQueue();
    }


    public static synchronized VolleyQueueSingleton getInstance(Context context){
        if(thisInstance == null){
            thisInstance = new VolleyQueueSingleton(context);
        }

        return thisInstance;
    }


    public RequestQueue getRequestQueue(){
        if(thisRequestQueue == null){
            //getApplicationContext() to persist Context and prevent memory leaks
            thisRequestQueue = Volley.newRequestQueue(thisContext.getApplicationContext());
        }

        return thisRequestQueue;
    }


    //Generic method to add object to our request queue
    /*
    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
    */

    //This method stops any of the requests in queue (typically called in onStop())
    public void destroyRequestQueue(){
        if(thisRequestQueue != null){
            thisRequestQueue.cancelAll(GET_REQUEST_TAG);
        }
    }

}

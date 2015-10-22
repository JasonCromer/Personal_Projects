package com.dev.cromer.jason.takeastand.Logic;

import com.dev.cromer.jason.takeastand.networking.HttpMarkerPostRequest;
import com.dev.cromer.jason.takeastand.objects.MarkerPostRequestParams;

import java.util.concurrent.ExecutionException;


/**
 * This class handles the posting of the Users marker by making an http post request via
 * the passed in request and parameters. The method makeHttpMarkerPostRequest then returns
 * a result that indicates whether or not the post request has succeeded.
 *
 * param: HttpMarkerPostRequest
 * param: MarkerPostRequestParams
 *
 * return: integer
 *
 */
public class PostUserMarkerHandler {

    private HttpMarkerPostRequest markerPostRequest;
    private MarkerPostRequestParams postRequestParams;
    private int result;

    //constants
    private static final int POST_SUCCESS = 0;
    private static final int POST_FAIL = -1;


    public PostUserMarkerHandler(HttpMarkerPostRequest request, MarkerPostRequestParams params){
        this.markerPostRequest = request;
        this.postRequestParams = params;
    }

    public int makeHttpMarkerPostRequest(){
        try{
            int postStatus = markerPostRequest.execute(postRequestParams).get();
            if(postStatus == POST_SUCCESS){
                //Post successful
                result = POST_SUCCESS;
            }
            else if(postStatus == POST_FAIL){
                //Failed
                result = POST_FAIL;
            }
        }
        catch(ExecutionException | InterruptedException e){
            e.printStackTrace();
            result = POST_FAIL;
        }

        return result;
    }
}

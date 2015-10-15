package com.dev.cromer.jason.takeastand.Logic;


import com.dev.cromer.jason.takeastand.networking.GenericHttpGetRequest;

import java.util.concurrent.ExecutionException;

public class UpdateNumUsersHandler {
    String url;
    String result;
    GenericHttpGetRequest httpGetRequest;

    public UpdateNumUsersHandler(String url, GenericHttpGetRequest request){
        this.url = url;
        httpGetRequest = request;
    }

    public void updateNumberOfUsers(){

        try{
            result = httpGetRequest.execute(url).get();
        }
        catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
    }
}

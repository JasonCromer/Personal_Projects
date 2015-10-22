package com.dev.cromer.jason.takeastand.Logic;

import android.util.Log;

import com.dev.cromer.jason.takeastand.networking.GenericHttpGetRequest;

import java.util.concurrent.ExecutionException;


public class RetrieveStatsHandler {

    String stringResult;
    String url;
    GenericHttpGetRequest httpGetRequest;
    int[] arrayResult;

    public RetrieveStatsHandler(String url, GenericHttpGetRequest getRequest){
        this.url = url;
        this.httpGetRequest = getRequest;
    }

    public int[] getNumOfReligionTypes(){

        //Attempt GET request
        try{
            stringResult = httpGetRequest.execute(url).get();
            arrayResult = convertStringToArray(stringResult);
        }
        catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
            arrayResult = null;
        }

        return arrayResult;
    }

    private int[] convertStringToArray(String input){
        //Trim any possible whitespace
        input = input.trim();

        //Create a String array to hold digits (\\D+ searches for digits)
        String[] digits = input.split("\\D+");

        //Create int array to hold our values and return it
        int[] intArray = new int[digits.length];

        //We start at 1 because position 0 is filled with a null value
        for(int i = 1; i < digits.length; i++){
            intArray[i] = Integer.parseInt(digits[i]);
        }

        return intArray;
    }

}

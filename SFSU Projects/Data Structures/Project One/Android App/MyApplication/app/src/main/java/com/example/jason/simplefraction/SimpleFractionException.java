package com.example.jason.simplefraction;

/**
 * Created by jason on 2/23/16.
 *
 */
public class SimpleFractionException extends RuntimeException {

    public SimpleFractionException(){
        this("");
    }

    public SimpleFractionException(String errorMsg){
        super(errorMsg);
    }
}

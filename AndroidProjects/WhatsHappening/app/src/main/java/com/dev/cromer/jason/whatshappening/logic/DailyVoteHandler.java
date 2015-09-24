package com.dev.cromer.jason.whatshappening.logic;


import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DailyVoteHandler {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public DailyVoteHandler(SharedPreferences preferences){
        this.preferences = preferences;
        editor = this.preferences.edit();

    }

    public void checkIfNewDay(String oldDateString){
        final String currentDateString = getCurrentDateString();
        final Date oldDate;
        final Date currentDate;

        if(!oldDateString.equals("NONE")){
            //Convert date Strings to Date objects
            oldDate = parseStringToDate(oldDateString);
            currentDate = parseStringToDate(currentDateString);

            //Compare dates to check if current date is after the old date
            if(currentDate.after(oldDate)){
                resetVoteAndDate();
            }
        }
    }


    public void resetVoteAndDate(){
        this.editor.putInt("NUM_VOTES", 0);
        this.editor.putString("OLD_DATE", "NONE");
        this.editor.apply();
    }


    public void storeOldDateInPreferences(){
        //Store current date as old date if first time voting on new day
        final String currentDateString = getCurrentDateString();
        this.editor.putString("OLD_DATE", currentDateString);
        this.editor.apply();
    }

    public Date parseStringToDate(String dateToParse){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        dateFormatter.setTimeZone(TimeZone.getDefault());
        Date parsedDate = new Date();
        try{
            parsedDate = dateFormatter.parse(dateToParse);
            return parsedDate;
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return parsedDate;
    }

    public String getCurrentDateString(){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        dateFormatter.setTimeZone(TimeZone.getDefault());

        return dateFormatter.format(new Date());
    }
}

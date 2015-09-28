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

    //constants
    private static final int DEFAULT_LIKES = 0;
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String NUM_VOTES_PREFERENCE = "NUM_VOTES";
    private static final String OLD_DATE_PREFERENCE = "OLD_DATE";
    private static final String NO_OLD_DATE_AVAILABLE = "NONE";

    public DailyVoteHandler(SharedPreferences preferences){
        this.preferences = preferences;
        editor = this.preferences.edit();

    }

    public void checkIfNewDay(String oldDateString){
        final String currentDateString = getCurrentDateString();
        final Date oldDate;
        final Date currentDate;

        if(!oldDateString.equals(NO_OLD_DATE_AVAILABLE)){
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
        this.editor.putInt(NUM_VOTES_PREFERENCE, DEFAULT_LIKES);
        this.editor.putString(OLD_DATE_PREFERENCE, NO_OLD_DATE_AVAILABLE);
        this.editor.apply();
    }


    public void storeOldDateInPreferences(){
        //Store current date as old date if first time voting on new day
        final String currentDateString = getCurrentDateString();
        this.editor.putString(OLD_DATE_PREFERENCE, currentDateString);
        this.editor.apply();
    }

    public Date parseStringToDate(String dateToParse){
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);
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
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        dateFormatter.setTimeZone(TimeZone.getDefault());

        return dateFormatter.format(new Date());
    }
}

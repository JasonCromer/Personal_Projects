package com.dev.cromer.jason.whatshappening.logic;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class NotificationsHandler implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {

    private NotificationInstructionFactory instructionFactory;
    private int dialogCount;
    private Context applicationContext;

    //Constants
    private static final String ALERT_DIALOG_PREFERENCES = "FIRST_USER_DIALOG_PREFS";
    private static final String newMarkerTitle = "Creating a Post";
    private static final String voteTitle = "Voting Posts";
    private static final String searchTitle = "Search the World";
    private static final String newMarkerDescription = "Simply press and hold anywhere on the " +
            "map to create a new post.";
    private static final String searchDescription = "Use the search bar at the top to search " +
            "anywhere around the world.";
    private static final String voteDescription = "Like user's posts and comment on them.";
    private static final String nextButtonText = "Next";
    private static final String doneButtonText = "Got it";


    public NotificationsHandler(Context context){
        this.applicationContext = context;
        this.instructionFactory = new NotificationInstructionFactory(context);
        this.dialogCount = 0;
    }

    public void displaySearchInfoDialog(){
        instructionFactory.setAlertTitle(searchTitle);
        instructionFactory.setAlertMessage(searchDescription);
        instructionFactory.setButtonText(nextButtonText, this);
        instructionFactory.setSearchAlertIcon();

        instructionFactory.showAlertDialog();
    }

    public void displayNewMarkerDialog() {
        instructionFactory.setAlertTitle(newMarkerTitle);
        instructionFactory.setAlertMessage(newMarkerDescription);
        instructionFactory.setButtonText(nextButtonText, this);
        instructionFactory.setAddAlertIcon();

        instructionFactory.showAlertDialog();
    }

    public void displayVoteInfoDialog(){
        instructionFactory.setAlertTitle(voteTitle);
        instructionFactory.setAlertMessage(voteDescription);
        instructionFactory.setButtonText(doneButtonText, this);
        instructionFactory.setVoteAlertIcon();

        instructionFactory.showAlertDialog();
    }

    public void displayChainingDialogs(){
        //Create chaining effect for the multiple dialogs using dialogCount;
        displaySearchInfoDialog();
    }

    private void setFirstTimeUser(){
        SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        final boolean isFirstTimeUser = false;
        SharedPreferences.Editor editor = preferenceManager.edit();
        editor.putBoolean(ALERT_DIALOG_PREFERENCES, isFirstTimeUser);
        editor.apply();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialogCount++;
        dialog.dismiss();
        onDismiss(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        //Chain dialogs after each one is dismissed
        if(dialogCount == 1){
            displayNewMarkerDialog();
        }
        if(dialogCount == 2){
            displayVoteInfoDialog();
        }
        if(dialogCount == 3){
            //Check if user has finished tutorial
            setFirstTimeUser();
        }
    }
}

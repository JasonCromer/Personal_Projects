package com.dev.cromer.jason.whatshappening.logic;


import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class NotificationsHandler implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {

    private NotificationInstructionFactory instructionFactory;
    private int dialogCount;

    //Constants
    private static final String newMarkerTitle = "Creating a Post";
    private static final String voteTitle = "Voting Posts";
    private static final String searchTitle = "Search the World";
    private static final String newMarkerDescription = "Simply press and hold anywhere on the " +
            "map to create a new post.";
    private static final String searchDescription = "Use the search bar at the top to search " +
            "anywhere around the world.";
    private static final String voteDescription = "Upvote/Downvote user's posts.. but beware: " +
            "you only have 5 votes per day. Use them wisely.";
    private static final String nextButtonText = "Next";
    private static final String doneButtonText = "Got it";


    public NotificationsHandler(Context context){
        instructionFactory = new NotificationInstructionFactory(context);
        dialogCount = 0;
    }

    public void displaySearchInfoDialog(){
        instructionFactory.setAlertTitle(searchTitle);
        instructionFactory.setAlertMessage(searchDescription);
        instructionFactory.setButtonText(nextButtonText, this);

        instructionFactory.showAlertDialog();
    }

    public void displayNewMarkerDialog() {
        instructionFactory.setAlertTitle(newMarkerTitle);
        instructionFactory.setAlertMessage(newMarkerDescription);
        instructionFactory.setButtonText(nextButtonText, this);

        instructionFactory.showAlertDialog();
    }

    public void displayVoteInfoDialog(){
        instructionFactory.setAlertTitle(voteTitle);
        instructionFactory.setAlertMessage(voteDescription);
        instructionFactory.setButtonText(doneButtonText, this);

        instructionFactory.showAlertDialog();
    }

    public void displayChainingDialogs(){
        //Create chaining effect for the multiple dialogs using dialogCount;
        displaySearchInfoDialog();
    }



    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialogCount++;
        dialog.dismiss();
        onDismiss(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(dialogCount == 1){
            displayNewMarkerDialog();
        }
        if(dialogCount == 2){
            displayVoteInfoDialog();
        }
    }
}

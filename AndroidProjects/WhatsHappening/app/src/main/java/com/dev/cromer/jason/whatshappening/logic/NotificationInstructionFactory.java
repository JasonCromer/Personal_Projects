package com.dev.cromer.jason.whatshappening.logic;


import android.app.AlertDialog;
import android.content.Context;
import android.view.ContextThemeWrapper;

public class NotificationInstructionFactory {

    private AlertDialog.Builder alertDialogBuilder;
    static ContextThemeWrapper themeWrapper;


    public NotificationInstructionFactory(Context context){
        //Pass in our activity context and set our theme dialog for the alert dialog
        themeWrapper = new ContextThemeWrapper(context, android.R.style.Theme_Dialog);

        //Use our theme wrapper to instantiate a new Alert Dialog builder
        alertDialogBuilder = new AlertDialog.Builder(themeWrapper);

        //Set our default icon
        setAlertIcon();
    }


    private void setAlertIcon(){
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
    }


    public void setAlertTitle(String title){
        alertDialogBuilder.setTitle(title);
    }

    public void setAlertMessage(String message){
        alertDialogBuilder.setMessage(message);
    }

    public void setButtonText(String buttonText, AlertDialog.OnClickListener listener){
        alertDialogBuilder.setPositiveButton(buttonText, listener);
    }

    public void showAlertDialog(){
        alertDialogBuilder.show();
    }

}

package com.dev.cromer.jason.whatshappening.logic;


import android.app.AlertDialog;
import android.content.Context;
import android.view.ContextThemeWrapper;

import com.dev.cromer.jason.whatshappening.R;


public class NotificationInstructionFactory {

    private AlertDialog.Builder alertDialogBuilder;
    static ContextThemeWrapper themeWrapper;


    public NotificationInstructionFactory(Context context){

        //Pass in our activity context and set our theme dialog for the alert dialog
        themeWrapper = new ContextThemeWrapper(context, R.style.CustomTransparentDialogTheme);

        //Use our theme wrapper to instantiate a new Alert Dialog builder
        alertDialogBuilder = new AlertDialog.Builder(themeWrapper);

        //Make Dialog non-cancellable
        alertDialogBuilder.setCancelable(false);
    }


    public void setSearchAlertIcon(){
        alertDialogBuilder.setIcon(android.R.drawable.ic_menu_search);
    }


    public void setAddAlertIcon(){
        alertDialogBuilder.setIcon(android.R.drawable.ic_menu_add);
    }

    public void setVoteAlertIcon(){
        alertDialogBuilder.setIcon(android.R.drawable.ic_input_get);
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

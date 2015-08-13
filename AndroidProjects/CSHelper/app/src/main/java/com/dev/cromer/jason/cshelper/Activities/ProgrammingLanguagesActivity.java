package com.dev.cromer.jason.cshelper.Activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.dev.cromer.jason.cshelper.R;

public class ProgrammingLanguagesActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView thisWebView;
    private String thisUrl = "https://en.wikipedia.org/wiki/Comparison_of_programming_languages";
    private ImageView thisBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programming_languages);

        thisWebView = (WebView) findViewById(R.id.programmingLanguageWebView);
        thisBackButton = (ImageView) findViewById(R.id.languagesBackButton);
        thisBackButton.setOnClickListener(this);

        initializeWebView();
    }


    //This function initializes the webview for the activity and catches URL loading errors
    public void initializeWebView() {
        final Activity thisActivity = this;
        thisWebView.getSettings().setJavaScriptEnabled(true);
        thisWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                thisActivity.setProgress(progress * 1000);
            }
        });


        //Handle errors loading webview
        thisWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingURL) {
                Toast.makeText(thisActivity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });

        thisWebView.loadUrl(thisUrl);

    }


    @Override
    public void onClick(View v) {
        if(v == thisBackButton) {
            finish();
        }

    }
}

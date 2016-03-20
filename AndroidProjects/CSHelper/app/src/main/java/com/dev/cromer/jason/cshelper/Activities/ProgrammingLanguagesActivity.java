package com.dev.cromer.jason.cshelper.Activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.dev.cromer.jason.cshelper.R;

public class ProgrammingLanguagesActivity extends AppCompatActivity {

    private WebView thisWebView;
    private static final String THIS_URL = "https://en.wikipedia.org/wiki/Comparison_of_programming_languages";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programming_languages);

        thisWebView = (WebView) findViewById(R.id.programmingLanguageWebView);

        initializeWebView();
    }


    //This function initializes the webview for the activity and catches URL loading errors
    public void initializeWebView() {
        final Activity thisActivity = this;

        //Set progress meter for webview loading
        thisWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                thisActivity.setProgress(progress * 1000);
            }
        });


        //Handle errors loading webview
        thisWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(ProgrammingLanguagesActivity.this, "Sorry, something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        thisWebView.loadUrl(THIS_URL);
    }
}

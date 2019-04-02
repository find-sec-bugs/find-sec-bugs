package testcode.android;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewJavascriptEnabledActivity extends Activity {


    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        WebView myWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        disableJavaScriptEnabled(webSettings);
    }

    private void disableJavaScriptEnabled(WebSettings webSettings) {
        //Should not rise an alert
        webSettings.setJavaScriptEnabled(false);
    }
}

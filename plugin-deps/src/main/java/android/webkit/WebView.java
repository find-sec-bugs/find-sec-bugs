package android.webkit;

import android.view.View;

/**
 * Shortcut taken : the webview doesn't extends directly View.
 * http://developer.android.com/reference/android/webkit/WebView.html
 */
public class WebView extends View {
    public void setWebViewClient (WebViewClient client) {}
    public void setWebChromeClient (WebChromeClient client) {}

    public WebSettings getSettings() {
        return null;
    }

    public void addJavascriptInterface(Object object, String name) {}

    public void evaluateJavascript (String script, ValueCallback<String> resultCallback) {}
}

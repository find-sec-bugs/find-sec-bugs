package testcode.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class WebViewJavascriptInterfaceActivity extends Activity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        UserSession user = new UserSession();

        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.addJavascriptInterface(user, "userInfo");
        myWebView.addJavascriptInterface(new FileWriteUtil(this), "fileWriteUtil");
    }

    class UserSession {
        public String userName;
        public String firstName;
        public String lastName;
        public String sessionId;

        public String getUserName() {
            return userName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getSessionId() {
            return sessionId;
        }
    }

    class FileWriteUtil {
        Context mContext;

        /** Instantiate the interface and set the context */
        FileWriteUtil(Context c) {
            mContext = c;
        }

        public void writeToFile(String data, String filename, String tag) {
            try {
                File root = Environment.getExternalStorageDirectory();
                File dir = new File (root.getAbsolutePath() + "/foldercustom");
                dir.mkdirs();
                File file = new File(dir, filename);
                FileOutputStream f = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(f);
                pw.println(data);
                pw.flush();
                pw.close();
                f.close();
            }
            catch (IOException e) {
                Log.e(tag, "File write failed: " + e.toString());
            }
        }
    }
}

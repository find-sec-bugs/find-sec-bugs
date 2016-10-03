package testcode.android;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;

public class BroadcastIntentActivity extends Activity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        String user = "";
        String email = "";
        String newSessionId = "";

        Intent i = new Intent();
        i.setAction("com.insecure.action.UserConnected");
        i.putExtra("username", user);
        i.putExtra("email", email);
        i.putExtra("session", newSessionId);

        sendBroadcast(i);

        sendBroadcast(i, null); //More signatures
        sendBroadcastAsUser(i, null);
        sendBroadcastAsUser(i,null,null);
        sendOrderedBroadcast(i,null);
        sendOrderedBroadcast(i,null,null,null,0,null,null);
        sendOrderedBroadcastAsUser(i,null,null,null,null,0,null,null);

        /* These calls are safe.
         *
         * https://developer.android.com/reference/android/support/v4/content/LocalBroadcastManager.html
         *      > "You know that the data you are broadcasting won't leave your app, so don't need to worry about leaking private data."
         */
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);

        // This class extends the Android LocalBroadcastManager and is used to test the InterfaceUtils.isSubtype condition.
        CustomLocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }
}

package testcode.android;

import android.app.Activity;
import android.os.Bundle;

import java.io.FileOutputStream;
import java.io.IOException;

public class WorldWritableActivity extends Activity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        String key = "Y0uOnlyL34k0nc3";

        try {
            FileOutputStream fos = openFileOutput("session_info.txt", MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE);
            fos.write(("userPassKey="+key).getBytes());

            //More signature
            fos = openFileOutput("session_info.txt", MODE_WORLD_READABLE);
            fos = openFileOutput("session_info.txt", MODE_WORLD_WRITEABLE);

            //OK (False positive to avoid)
            fos = openFileOutput("session_info.txt", MODE_PRIVATE);
        }
        catch (IOException e){}

    }
}

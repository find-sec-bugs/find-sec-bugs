package testcode.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ExternalFileAccessActivity extends Activity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        //Various external directory reference that could be use to read/write on SDCard.

        File mainCacheDir    = getExternalCacheDir();
        File[] cacheDirs     = getExternalCacheDirs();
        File mainExternalDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] externalDirs  = getExternalFilesDirs(Environment.DIRECTORY_MUSIC);
        File[] mediaDirs     = getExternalMediaDirs();
        File externalDir = Environment.getExternalStorageDirectory();
        File pictureDir  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File newPicture = new File(mainExternalDir,"test.gif");
        try {
            FileOutputStream out = new FileOutputStream(newPicture);

            //out.write();
        } catch (FileNotFoundException e) {
        }
    }
}

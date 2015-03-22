package android.content;

import java.io.File;
import java.io.FileOutputStream;

/**
 * http://developer.android.com/reference/android/content/Context.html
 */
public abstract class Context {

    abstract File   getExternalCacheDir();
    abstract File[]	getExternalCacheDirs();
    abstract File	getExternalFilesDir(String type);
    abstract File[]	getExternalFilesDirs(String type);
    abstract File[]	getExternalMediaDirs();

    abstract FileOutputStream openFileOutput (String name, int mode);
}

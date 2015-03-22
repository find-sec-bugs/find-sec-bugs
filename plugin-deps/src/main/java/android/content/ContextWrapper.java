package android.content;

import java.io.File;
import java.io.FileOutputStream;

public class ContextWrapper extends Context {

    @Override
    public File getExternalCacheDir() {
        return null;
    }

    @Override
    public File[] getExternalCacheDirs() {
        return new File[0];
    }

    @Override
    public File getExternalFilesDir(String type) {
        return null;
    }

    @Override
    public File[] getExternalFilesDirs(String type) {
        return new File[0];
    }

    @Override
    public File[] getExternalMediaDirs() {
        return new File[0];
    }

    @Override
    public FileOutputStream openFileOutput(String name, int mode) {
        return null;
    }
}

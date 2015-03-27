package android.os;

import java.io.File;

public class Environment {
    public static String	DIRECTORY_ALARMS = "";
    public static String	DIRECTORY_DCIM = "";
    public static String	DIRECTORY_DOCUMENTS	= "";
    public static String	DIRECTORY_DOWNLOADS	= "";
    public static String	DIRECTORY_MOVIES = "";
    public static String	DIRECTORY_MUSIC	= "";
    public static String	DIRECTORY_NOTIFICATIONS	= "";
    public static String	DIRECTORY_PICTURES = "";
    public static String	DIRECTORY_PODCASTS = "";
    public static String	DIRECTORY_RINGTONES = "";

    public static File getExternalStorageDirectory() {return null;}
    public static File getExternalStoragePublicDirectory(String type) {return null;}

}

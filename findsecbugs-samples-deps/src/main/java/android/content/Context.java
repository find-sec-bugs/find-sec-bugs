package android.content;

import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;

import java.io.File;
import java.io.FileOutputStream;

/**
 * http://developer.android.com/reference/android/content/Context.html
 */
public abstract class Context {

    public static final  int	MODE_APPEND = 0x00008000;
    public static final  int	MODE_ENABLE_WRITE_AHEAD_LOGGING = 0x00000008;
    public static final  int	MODE_MULTI_PROCESS = 0x00000004;
    public static final  int	MODE_PRIVATE = 0x00000000;
    public static final  int	MODE_WORLD_READABLE = 0x00000001;
    public static final  int	MODE_WORLD_WRITEABLE = 0x00000002;


    abstract File   getExternalCacheDir();
    abstract File[]	getExternalCacheDirs();
    abstract File	getExternalFilesDir(String type);
    abstract File[]	getExternalFilesDirs(String type);
    abstract File[]	getExternalMediaDirs();

    abstract FileOutputStream openFileOutput (String name, int mode);

    abstract void	sendBroadcast(Intent intent, String receiverPermission);
    abstract void	sendBroadcast(Intent intent);
    abstract void	sendBroadcastAsUser(Intent intent, UserHandle user);
    abstract void	sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission);
    abstract void	sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras);
    abstract void	sendOrderedBroadcast(Intent intent, String receiverPermission);
    abstract void	sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras);

}

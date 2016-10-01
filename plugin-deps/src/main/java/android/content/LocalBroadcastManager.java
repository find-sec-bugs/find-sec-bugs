package android.content;

public class LocalBroadcastManager {
    public static LocalBroadcastManager getInstance(Context context) { return new LocalBroadcastManager(); }

    //public void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {};
    public boolean sendBroadcast(Intent intent) { return true; };
    public void sendBroadcastSync(Intent intent) {};
    public void unregisterReceiver(BroadcastReceiver receiver) {};
}

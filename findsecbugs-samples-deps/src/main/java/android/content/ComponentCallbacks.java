package android.content;

import android.content.res.Configuration;

public abstract interface ComponentCallbacks
{
    public abstract void onConfigurationChanged(Configuration paramConfiguration);

    public abstract void onLowMemory();
}

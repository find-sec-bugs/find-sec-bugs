package android.content.res;

//import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
//import android.os.Parcelable.Creator;
import java.util.Locale;

public final class Configuration
        implements Parcelable, Comparable<Configuration>
{
    public Configuration()
    {
        throw new RuntimeException("Stub!");
    }

    public Configuration(Configuration o)
    {
        throw new RuntimeException("Stub!");
    }

    public boolean isLayoutSizeAtLeast(int size)
    {
        throw new RuntimeException("Stub!");
    }

    public void setTo(Configuration o)
    {
        throw new RuntimeException("Stub!");
    }

    public String toString()
    {
        throw new RuntimeException("Stub!");
    }

    public void setToDefaults()
    {
        throw new RuntimeException("Stub!");
    }

    public int updateFrom(Configuration delta)
    {
        throw new RuntimeException("Stub!");
    }

    public int diff(Configuration delta)
    {
        throw new RuntimeException("Stub!");
    }

    public static boolean needNewResources(int configChanges, int interestingChanges)
    {
        throw new RuntimeException("Stub!");
    }

    public int describeContents()
    {
        throw new RuntimeException("Stub!");
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        throw new RuntimeException("Stub!");
    }

    public void readFromParcel(Parcel source)
    {
        throw new RuntimeException("Stub!");
    }

    public int compareTo(Configuration that)
    {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Configuration that)
    {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object that)
    {
        throw new RuntimeException("Stub!");
    }

    public int hashCode()
    {
        throw new RuntimeException("Stub!");
    }

//    public LocaleList getLocales()
//    {
//        throw new RuntimeException("Stub!");
//    }
//
//    public void setLocales(LocaleList locales)
//    {
//        throw new RuntimeException("Stub!");
//    }

    public void setLocale(Locale loc)
    {
        throw new RuntimeException("Stub!");
    }

    public int getLayoutDirection()
    {
        throw new RuntimeException("Stub!");
    }

    public void setLayoutDirection(Locale loc)
    {
        throw new RuntimeException("Stub!");
    }

    public boolean isScreenRound()
    {
        throw new RuntimeException("Stub!");
    }

    public static final Parcelable.Creator<Configuration> CREATOR = null;
    public static final int DENSITY_DPI_UNDEFINED = 0;
    public static final int HARDKEYBOARDHIDDEN_NO = 1;
    public static final int HARDKEYBOARDHIDDEN_UNDEFINED = 0;
    public static final int HARDKEYBOARDHIDDEN_YES = 2;
    public static final int KEYBOARDHIDDEN_NO = 1;
    public static final int KEYBOARDHIDDEN_UNDEFINED = 0;
    public static final int KEYBOARDHIDDEN_YES = 2;
    public static final int KEYBOARD_12KEY = 3;
    public static final int KEYBOARD_NOKEYS = 1;
    public static final int KEYBOARD_QWERTY = 2;
    public static final int KEYBOARD_UNDEFINED = 0;
    public static final int MNC_ZERO = 65535;
    public static final int NAVIGATIONHIDDEN_NO = 1;
    public static final int NAVIGATIONHIDDEN_UNDEFINED = 0;
    public static final int NAVIGATIONHIDDEN_YES = 2;
    public static final int NAVIGATION_DPAD = 2;
    public static final int NAVIGATION_NONAV = 1;
    public static final int NAVIGATION_TRACKBALL = 3;
    public static final int NAVIGATION_UNDEFINED = 0;
    public static final int NAVIGATION_WHEEL = 4;
    public static final int ORIENTATION_LANDSCAPE = 2;
    public static final int ORIENTATION_PORTRAIT = 1;
    @Deprecated
    public static final int ORIENTATION_SQUARE = 3;
    public static final int ORIENTATION_UNDEFINED = 0;
    public static final int SCREENLAYOUT_LAYOUTDIR_LTR = 64;
    public static final int SCREENLAYOUT_LAYOUTDIR_MASK = 192;
    public static final int SCREENLAYOUT_LAYOUTDIR_RTL = 128;
    public static final int SCREENLAYOUT_LAYOUTDIR_SHIFT = 6;
    public static final int SCREENLAYOUT_LAYOUTDIR_UNDEFINED = 0;
    public static final int SCREENLAYOUT_LONG_MASK = 48;
    public static final int SCREENLAYOUT_LONG_NO = 16;
    public static final int SCREENLAYOUT_LONG_UNDEFINED = 0;
    public static final int SCREENLAYOUT_LONG_YES = 32;
    public static final int SCREENLAYOUT_ROUND_MASK = 768;
    public static final int SCREENLAYOUT_ROUND_NO = 256;
    public static final int SCREENLAYOUT_ROUND_UNDEFINED = 0;
    public static final int SCREENLAYOUT_ROUND_YES = 512;
    public static final int SCREENLAYOUT_SIZE_LARGE = 3;
    public static final int SCREENLAYOUT_SIZE_MASK = 15;
    public static final int SCREENLAYOUT_SIZE_NORMAL = 2;
    public static final int SCREENLAYOUT_SIZE_SMALL = 1;
    public static final int SCREENLAYOUT_SIZE_UNDEFINED = 0;
    public static final int SCREENLAYOUT_SIZE_XLARGE = 4;
    public static final int SCREENLAYOUT_UNDEFINED = 0;
    public static final int SCREEN_HEIGHT_DP_UNDEFINED = 0;
    public static final int SCREEN_WIDTH_DP_UNDEFINED = 0;
    public static final int SMALLEST_SCREEN_WIDTH_DP_UNDEFINED = 0;
    public static final int TOUCHSCREEN_FINGER = 3;
    public static final int TOUCHSCREEN_NOTOUCH = 1;
    @Deprecated
    public static final int TOUCHSCREEN_STYLUS = 2;
    public static final int TOUCHSCREEN_UNDEFINED = 0;
    public static final int UI_MODE_NIGHT_MASK = 48;
    public static final int UI_MODE_NIGHT_NO = 16;
    public static final int UI_MODE_NIGHT_UNDEFINED = 0;
    public static final int UI_MODE_NIGHT_YES = 32;
    public static final int UI_MODE_TYPE_APPLIANCE = 5;
    public static final int UI_MODE_TYPE_CAR = 3;
    public static final int UI_MODE_TYPE_DESK = 2;
    public static final int UI_MODE_TYPE_MASK = 15;
    public static final int UI_MODE_TYPE_NORMAL = 1;
    public static final int UI_MODE_TYPE_TELEVISION = 4;
    public static final int UI_MODE_TYPE_UNDEFINED = 0;
    public static final int UI_MODE_TYPE_WATCH = 6;
    public int densityDpi;
    public float fontScale;
    public int hardKeyboardHidden;
    public int keyboard;
    public int keyboardHidden;
    @Deprecated
    public Locale locale;
    public int mcc;
    public int mnc;
    public int navigation;
    public int navigationHidden;
    public int orientation;
    public int screenHeightDp;
    public int screenLayout;
    public int screenWidthDp;
    public int smallestScreenWidthDp;
    public int touchscreen;
    public int uiMode;
}


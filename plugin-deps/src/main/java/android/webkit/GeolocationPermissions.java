package android.webkit;

public class GeolocationPermissions {
    public interface Callback {
        void invoke(String origin, boolean allow, boolean retain);
    }
}

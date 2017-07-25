package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.File;
import java.util.List;
import java.util.Set;

public abstract class Uri
        implements Parcelable, Comparable<Uri>
{
    public static final class Builder
    {
        public Builder()
        {
            throw new RuntimeException("Stub!");
        }

        public Builder scheme(String scheme)
        {
            throw new RuntimeException("Stub!");
        }

        public Builder opaquePart(String opaquePart)
        {
            throw new RuntimeException("Stub!");
        }

        public Builder encodedOpaquePart(String opaquePart)
        {
            throw new RuntimeException("Stub!");
        }

        public Builder authority(String authority)
        {
            throw new RuntimeException("Stub!");
        }

        public Builder encodedAuthority(String authority)
        {
            throw new RuntimeException("Stub!");
        }

        public Builder path(String path)
        {
            throw new RuntimeException("Stub!");
        }

        public Builder encodedPath(String path)
        {
            throw new RuntimeException("Stub!");
        }

        public Builder appendPath(String newSegment)
        {
            throw new RuntimeException("Stub!");
        }

        public Builder appendEncodedPath(String newSegment)
        {
            throw new RuntimeException("Stub!");
        }

        public Builder query(String query)
        {
            throw new RuntimeException("Stub!");
        }

        public Builder encodedQuery(String query)
        {
            throw new RuntimeException("Stub!");
        }

        public Builder fragment(String fragment)
        {
            throw new RuntimeException("Stub!");
        }

        public Builder encodedFragment(String fragment)
        {
            throw new RuntimeException("Stub!");
        }

        public Builder appendQueryParameter(String key, String value)
        {
            throw new RuntimeException("Stub!");
        }

        public Builder clearQuery()
        {
            throw new RuntimeException("Stub!");
        }

        public Uri build()
        {
            throw new RuntimeException("Stub!");
        }

        public String toString()
        {
            throw new RuntimeException("Stub!");
        }
    }

    Uri()
    {
        throw new RuntimeException("Stub!");
    }

    public abstract boolean isHierarchical();

    public boolean isOpaque()
    {
        throw new RuntimeException("Stub!");
    }

    public abstract boolean isRelative();

    public boolean isAbsolute()
    {
        throw new RuntimeException("Stub!");
    }

    public abstract String getScheme();

    public abstract String getSchemeSpecificPart();

    public abstract String getEncodedSchemeSpecificPart();

    public abstract String getAuthority();

    public abstract String getEncodedAuthority();

    public abstract String getUserInfo();

    public abstract String getEncodedUserInfo();

    public abstract String getHost();

    public abstract int getPort();

    public abstract String getPath();

    public abstract String getEncodedPath();

    public abstract String getQuery();

    public abstract String getEncodedQuery();

    public abstract String getFragment();

    public abstract String getEncodedFragment();

    public abstract List<String> getPathSegments();

    public abstract String getLastPathSegment();

    public boolean equals(Object o)
    {
        throw new RuntimeException("Stub!");
    }

    public int hashCode()
    {
        throw new RuntimeException("Stub!");
    }

    public int compareTo(Uri other)
    {
        throw new RuntimeException("Stub!");
    }

    public abstract String toString();

    public abstract Builder buildUpon();

    public static Uri parse(String uriString)
    {
        throw new RuntimeException("Stub!");
    }

    public static Uri fromFile(File file)
    {
        throw new RuntimeException("Stub!");
    }

    public static Uri fromParts(String scheme, String ssp, String fragment)
    {
        throw new RuntimeException("Stub!");
    }

    public Set<String> getQueryParameterNames()
    {
        throw new RuntimeException("Stub!");
    }

    public List<String> getQueryParameters(String key)
    {
        throw new RuntimeException("Stub!");
    }

    public String getQueryParameter(String key)
    {
        throw new RuntimeException("Stub!");
    }

    public boolean getBooleanQueryParameter(String key, boolean defaultValue)
    {
        throw new RuntimeException("Stub!");
    }

    public Uri normalizeScheme()
    {
        throw new RuntimeException("Stub!");
    }

    public static void writeToParcel(Parcel out, Uri uri)
    {
        throw new RuntimeException("Stub!");
    }

    public static String encode(String s)
    {
        throw new RuntimeException("Stub!");
    }

    public static String encode(String s, String allow)
    {
        throw new RuntimeException("Stub!");
    }

    public static String decode(String s)
    {
        throw new RuntimeException("Stub!");
    }

    public static Uri withAppendedPath(Uri baseUri, String pathSegment)
    {
        throw new RuntimeException("Stub!");
    }

    public static final Uri EMPTY = null;
    public static final Parcelable.Creator<Uri> CREATOR = null;
}

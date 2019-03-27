package android.content;

//import android.content.pm.PathPermission;
//import android.content.pm.ProviderInfo;
//import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public abstract class ContentProvider
        implements ComponentCallbacks2
{
    public ContentProvider()
    {
        throw new RuntimeException("Stub!");
    }

    public final Context getContext()
    {
        throw new RuntimeException("Stub!");
    }

    public final String getCallingPackage()
    {
        throw new RuntimeException("Stub!");
    }

    protected final void setReadPermission(String permission)
    {
        throw new RuntimeException("Stub!");
    }

    public final String getReadPermission()
    {
        throw new RuntimeException("Stub!");
    }

    protected final void setWritePermission(String permission)
    {
        throw new RuntimeException("Stub!");
    }

    public final String getWritePermission()
    {
        throw new RuntimeException("Stub!");
    }

//    protected final void setPathPermissions(PathPermission[] permissions)
//    {
//        throw new RuntimeException("Stub!");
//    }
//
//    public final PathPermission[] getPathPermissions()
//    {
//        throw new RuntimeException("Stub!");
//    }

    public abstract boolean onCreate();

    public void onConfigurationChanged(Configuration newConfig)
    {
        throw new RuntimeException("Stub!");
    }

    public void onLowMemory()
    {
        throw new RuntimeException("Stub!");
    }

    public void onTrimMemory(int level)
    {
        throw new RuntimeException("Stub!");
    }

    public abstract Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2);

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal)
    {
        throw new RuntimeException("Stub!");
    }

    public abstract String getType(Uri paramUri);

    public Uri canonicalize(Uri url)
    {
        throw new RuntimeException("Stub!");
    }

    public Uri uncanonicalize(Uri url)
    {
        throw new RuntimeException("Stub!");
    }

    public abstract Uri insert(Uri paramUri, ContentValues paramContentValues);

    public int bulkInsert(Uri uri, ContentValues[] values)
    {
        throw new RuntimeException("Stub!");
    }

    public abstract int delete(Uri paramUri, String paramString, String[] paramArrayOfString);

    public abstract int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString);

    public ParcelFileDescriptor openFile(Uri uri, String mode)
            throws FileNotFoundException
    {
        throw new RuntimeException("Stub!");
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode, CancellationSignal signal)
            throws FileNotFoundException
    {
        throw new RuntimeException("Stub!");
    }

//    public AssetFileDescriptor openAssetFile(Uri uri, String mode)
//            throws FileNotFoundException
//    {
//        throw new RuntimeException("Stub!");
//    }
//
//    public AssetFileDescriptor openAssetFile(Uri uri, String mode, CancellationSignal signal)
//            throws FileNotFoundException
//    {
//        throw new RuntimeException("Stub!");
//    }

    protected final ParcelFileDescriptor openFileHelper(Uri uri, String mode)
            throws FileNotFoundException
    {
        throw new RuntimeException("Stub!");
    }

    public String[] getStreamTypes(Uri uri, String mimeTypeFilter)
    {
        throw new RuntimeException("Stub!");
    }

//    public AssetFileDescriptor openTypedAssetFile(Uri uri, String mimeTypeFilter, Bundle opts)
//            throws FileNotFoundException
//    {
//        throw new RuntimeException("Stub!");
//    }
//
//    public AssetFileDescriptor openTypedAssetFile(Uri uri, String mimeTypeFilter, Bundle opts, CancellationSignal signal)
//            throws FileNotFoundException
//    {
//        throw new RuntimeException("Stub!");
//    }

    public <T> ParcelFileDescriptor openPipeHelper(Uri uri, String mimeType, Bundle opts, T args, PipeDataWriter<T> func)
            throws FileNotFoundException
    {
        throw new RuntimeException("Stub!");
    }

    protected boolean isTemporary()
    {
        throw new RuntimeException("Stub!");
    }

//    public void attachInfo(Context context, ProviderInfo info)
//    {
//        throw new RuntimeException("Stub!");
//    }

//    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
//            throws OperationApplicationException
//    {
//        throw new RuntimeException("Stub!");
//    }

    public Bundle call(String method, String arg, Bundle extras)
    {
        throw new RuntimeException("Stub!");
    }

    public void shutdown()
    {
        throw new RuntimeException("Stub!");
    }

    public void dump(FileDescriptor fd, PrintWriter writer, String[] args)
    {
        throw new RuntimeException("Stub!");
    }

    public static abstract interface PipeDataWriter<T>
    {
        public abstract void writeDataToPipe(ParcelFileDescriptor paramParcelFileDescriptor, Uri paramUri, String paramString, Bundle paramBundle, T paramT);
    }
}

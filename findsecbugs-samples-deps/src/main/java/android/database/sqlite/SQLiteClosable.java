package android.database.sqlite;

import java.io.Closeable;

public abstract class SQLiteClosable
        implements Closeable
{
    public SQLiteClosable()
    {
        throw new RuntimeException("Stub!");
    }

    protected abstract void onAllReferencesReleased();

    @Deprecated
    protected void onAllReferencesReleasedFromContainer()
    {
        throw new RuntimeException("Stub!");
    }

    public void acquireReference()
    {
        throw new RuntimeException("Stub!");
    }

    public void releaseReference()
    {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void releaseReferenceFromContainer()
    {
        throw new RuntimeException("Stub!");
    }

    public void close()
    {
        throw new RuntimeException("Stub!");
    }
}

package android.database.sqlite;

import android.database.Cursor;

public abstract interface SQLiteCursorDriver
{
    public abstract Cursor query(SQLiteDatabase.CursorFactory paramCursorFactory, String[] paramArrayOfString);

    public abstract void cursorDeactivated();

    public abstract void cursorRequeried(Cursor paramCursor);

    public abstract void cursorClosed();

    public abstract void setBindArguments(String[] paramArrayOfString);
}

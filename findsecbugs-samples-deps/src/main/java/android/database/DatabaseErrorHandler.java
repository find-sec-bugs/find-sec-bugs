package android.database;

import android.database.sqlite.SQLiteDatabase;

public interface DatabaseErrorHandler
{
    void onCorruption(SQLiteDatabase paramSQLiteDatabase);
}

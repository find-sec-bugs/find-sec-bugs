package android.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteProgram;
import android.database.sqlite.SQLiteStatement;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class DatabaseUtils
{
    public static final int STATEMENT_ABORT = 6;
    public static final int STATEMENT_ATTACH = 3;
    public static final int STATEMENT_BEGIN = 4;
    public static final int STATEMENT_COMMIT = 5;
    public static final int STATEMENT_DDL = 8;
    public static final int STATEMENT_OTHER = 99;
    public static final int STATEMENT_PRAGMA = 7;
    public static final int STATEMENT_SELECT = 1;
    public static final int STATEMENT_UNPREPARED = 9;
    public static final int STATEMENT_UPDATE = 2;

    @Deprecated
    public static class InsertHelper
    {
        public InsertHelper(SQLiteDatabase db, String tableName)
        {
            throw new RuntimeException("Stub!");
        }

        public int getColumnIndex(String key)
        {
            throw new RuntimeException("Stub!");
        }

        public void bind(int index, double value)
        {
            throw new RuntimeException("Stub!");
        }

        public void bind(int index, float value)
        {
            throw new RuntimeException("Stub!");
        }

        public void bind(int index, long value)
        {
            throw new RuntimeException("Stub!");
        }

        public void bind(int index, int value)
        {
            throw new RuntimeException("Stub!");
        }

        public void bind(int index, boolean value)
        {
            throw new RuntimeException("Stub!");
        }

        public void bindNull(int index)
        {
            throw new RuntimeException("Stub!");
        }

        public void bind(int index, byte[] value)
        {
            throw new RuntimeException("Stub!");
        }

        public void bind(int index, String value)
        {
            throw new RuntimeException("Stub!");
        }

        public long insert(ContentValues values)
        {
            throw new RuntimeException("Stub!");
        }

        public long execute()
        {
            throw new RuntimeException("Stub!");
        }

        public void prepareForInsert()
        {
            throw new RuntimeException("Stub!");
        }

        public void prepareForReplace()
        {
            throw new RuntimeException("Stub!");
        }

        public long replace(ContentValues values)
        {
            throw new RuntimeException("Stub!");
        }

        public void close()
        {
            throw new RuntimeException("Stub!");
        }
    }

    public DatabaseUtils()
    {
        throw new RuntimeException("Stub!");
    }

    public static final void writeExceptionToParcel(Parcel reply, Exception e)
    {
        throw new RuntimeException("Stub!");
    }

    public static final void readExceptionFromParcel(Parcel reply)
    {
        throw new RuntimeException("Stub!");
    }

    public static void readExceptionWithFileNotFoundExceptionFromParcel(Parcel reply)
            throws FileNotFoundException
    {
        throw new RuntimeException("Stub!");
    }

    public static void readExceptionWithOperationApplicationExceptionFromParcel(Parcel reply)
            throws OperationApplicationException
    {
        throw new RuntimeException("Stub!");
    }

    public static void bindObjectToProgram(SQLiteProgram prog, int index, Object value)
    {
        throw new RuntimeException("Stub!");
    }

    public static void appendEscapedSQLString(StringBuilder sb, String sqlString)
    {
        throw new RuntimeException("Stub!");
    }

    public static String sqlEscapeString(String value)
    {
        throw new RuntimeException("Stub!");
    }

    public static final void appendValueToSql(StringBuilder sql, Object value)
    {
        throw new RuntimeException("Stub!");
    }

    public static String concatenateWhere(String a, String b)
    {
        throw new RuntimeException("Stub!");
    }

    public static String getCollationKey(String name)
    {
        throw new RuntimeException("Stub!");
    }

    public static String getHexCollationKey(String name)
    {
        throw new RuntimeException("Stub!");
    }

    public static void dumpCursor(Cursor cursor)
    {
        throw new RuntimeException("Stub!");
    }

    public static void dumpCursor(Cursor cursor, PrintStream stream)
    {
        throw new RuntimeException("Stub!");
    }

    public static void dumpCursor(Cursor cursor, StringBuilder sb)
    {
        throw new RuntimeException("Stub!");
    }

    public static String dumpCursorToString(Cursor cursor)
    {
        throw new RuntimeException("Stub!");
    }

    public static void dumpCurrentRow(Cursor cursor)
    {
        throw new RuntimeException("Stub!");
    }

    public static void dumpCurrentRow(Cursor cursor, PrintStream stream)
    {
        throw new RuntimeException("Stub!");
    }

    public static void dumpCurrentRow(Cursor cursor, StringBuilder sb)
    {
        throw new RuntimeException("Stub!");
    }

    public static String dumpCurrentRowToString(Cursor cursor)
    {
        throw new RuntimeException("Stub!");
    }

    public static void cursorStringToContentValues(Cursor cursor, String field, ContentValues values)
    {
        throw new RuntimeException("Stub!");
    }

    public static void cursorStringToInsertHelper(Cursor cursor, String field, InsertHelper inserter, int index)
    {
        throw new RuntimeException("Stub!");
    }

    public static void cursorStringToContentValues(Cursor cursor, String field, ContentValues values, String key)
    {
        throw new RuntimeException("Stub!");
    }

    public static void cursorIntToContentValues(Cursor cursor, String field, ContentValues values)
    {
        throw new RuntimeException("Stub!");
    }

    public static void cursorIntToContentValues(Cursor cursor, String field, ContentValues values, String key)
    {
        throw new RuntimeException("Stub!");
    }

    public static void cursorLongToContentValues(Cursor cursor, String field, ContentValues values)
    {
        throw new RuntimeException("Stub!");
    }

    public static void cursorLongToContentValues(Cursor cursor, String field, ContentValues values, String key)
    {
        throw new RuntimeException("Stub!");
    }

    public static void cursorDoubleToCursorValues(Cursor cursor, String field, ContentValues values)
    {
        throw new RuntimeException("Stub!");
    }

    public static void cursorDoubleToContentValues(Cursor cursor, String field, ContentValues values, String key)
    {
        throw new RuntimeException("Stub!");
    }

    public static void cursorRowToContentValues(Cursor cursor, ContentValues values)
    {
        throw new RuntimeException("Stub!");
    }

    public static long queryNumEntries(SQLiteDatabase db, String table)
    {
        throw new RuntimeException("Stub!");
    }

    public static long queryNumEntries(SQLiteDatabase db, String table, String selection)
    {
        throw new RuntimeException("Stub!");
    }

    public static long queryNumEntries(SQLiteDatabase db, String table, String selection, String[] selectionArgs)
    {
        throw new RuntimeException("Stub!");
    }

    public static long longForQuery(SQLiteDatabase db, String query, String[] selectionArgs)
    {
        throw new RuntimeException("Stub!");
    }

    public static long longForQuery(SQLiteStatement prog, String[] selectionArgs)
    {
        throw new RuntimeException("Stub!");
    }

    public static String stringForQuery(SQLiteDatabase db, String query, String[] selectionArgs)
    {
        throw new RuntimeException("Stub!");
    }

    public static String stringForQuery(SQLiteStatement prog, String[] selectionArgs)
    {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor blobFileDescriptorForQuery(SQLiteDatabase db, String query, String[] selectionArgs)
    {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor blobFileDescriptorForQuery(SQLiteStatement prog, String[] selectionArgs)
    {
        throw new RuntimeException("Stub!");
    }

    public static void cursorStringToContentValuesIfPresent(Cursor cursor, ContentValues values, String column)
    {
        throw new RuntimeException("Stub!");
    }

    public static void cursorLongToContentValuesIfPresent(Cursor cursor, ContentValues values, String column)
    {
        throw new RuntimeException("Stub!");
    }

    public static void cursorShortToContentValuesIfPresent(Cursor cursor, ContentValues values, String column)
    {
        throw new RuntimeException("Stub!");
    }

    public static void cursorIntToContentValuesIfPresent(Cursor cursor, ContentValues values, String column)
    {
        throw new RuntimeException("Stub!");
    }

    public static void cursorFloatToContentValuesIfPresent(Cursor cursor, ContentValues values, String column)
    {
        throw new RuntimeException("Stub!");
    }

    public static void cursorDoubleToContentValuesIfPresent(Cursor cursor, ContentValues values, String column)
    {
        throw new RuntimeException("Stub!");
    }

    public static void createDbFromSqlStatements(Context context, String dbName, int dbVersion, String sqlStatements)
    {
        throw new RuntimeException("Stub!");
    }

    public static int getSqlStatementType(String sql)
    {
        throw new RuntimeException("Stub!");
    }

    public static String[] appendSelectionArgs(String[] originalValues, String[] newValues)
    {
        throw new RuntimeException("Stub!");
    }
}

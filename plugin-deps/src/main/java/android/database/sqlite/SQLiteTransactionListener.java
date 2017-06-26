package android.database.sqlite;

public abstract interface SQLiteTransactionListener
{
    public abstract void onBegin();

    public abstract void onCommit();

    public abstract void onRollback();
}

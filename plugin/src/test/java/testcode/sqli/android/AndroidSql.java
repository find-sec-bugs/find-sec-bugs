package testcode.sqli.android;

import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.CancellationSignal;

public class AndroidSql {

    public void sampleSQLiteDatabase(SQLiteDatabase db, String input) {
        db.beginTransaction();
        //
        db.compileStatement(input);
        //query
        db.query(false, input, null, null, null, null, null, null, null);
        db.query(false, input, null, null, null, null, null, null, null, null);
        db.query(input, null, null, null, null, null, null);
        db.query(input, null, null, null, null, null, null, null);
        //queryWithFactory
        db.queryWithFactory(null, false, input, null, null, null, null,null,null, null);
        db.queryWithFactory(null, false, input, null, null, null, null,null,null, null, null);
        //rawQueryWithFactory
        db.rawQueryWithFactory(null, input, null, null);
        db.rawQueryWithFactory(null, input, null, null, null);
        //delete
        db.delete(input, null, new String[] {"1","2"});
        db.delete(null, input, new String[] {"1","2"});
        //update
        db.update(input, null, null, null);
        //updateWithOnConflict
        db.updateWithOnConflict(input, null, null, null, SQLiteDatabase.CONFLICT_ROLLBACK);
        db.updateWithOnConflict(null, null, input, null, SQLiteDatabase.CONFLICT_ABORT);
        //execSQL
        db.execSQL(input);
        db.execSQL(input,null);

        db.endTransaction();
    }

    public void sampleDatabaseUtils(DatabaseUtils databaseUtils, String input) {
        databaseUtils.longForQuery(null, input, null);
        databaseUtils.stringForQuery(null, input, null);
        databaseUtils.blobFileDescriptorForQuery(null, input, null);
        databaseUtils.createDbFromSqlStatements(null, null, 0, input);
    }

    public void sampleSQLiteQueryBuilder(SQLiteQueryBuilder builder, String input) {
        builder.appendWhere(input);
        //buildQueryString
        builder.buildQueryString(false, null, null, input, null, null, null, null);
        builder.buildQueryString(false, null, null, null, input, null, null, null);
        builder.buildQueryString(false, null, null, null, null, input, null, null);
        builder.buildQueryString(false, null, null, null, null, null, input,null);
        builder.buildQueryString(false, null, null, null, null, null,null, input);
        //query no limit
        builder.query(null, null, input, null, null, null,null);
        builder.query(null, null, null, null, input, null,null);
        builder.query(null, null, null, null,null,input,null);
        builder.query(null, null, null, null,null,null,input);
        //query with limit
        builder.query(null, null, input, null, null, null,null, null);
        builder.query(null, null, null, null, input, null,null, null);
        builder.query(null, null, null, null,null,input,null, null);
        builder.query(null, null, null, null,null,null,input, null);
        builder.query(null, null, null, null,null,null,null, input);
        //query with limit with cancellation system
        builder.query(null, null, input, null, null, null,null, null, new CancellationSignal());
        builder.query(null, null, null, null, input, null,null, null, new CancellationSignal());
        builder.query(null, null, null, null,null,input,null, null, new CancellationSignal());
        builder.query(null, null, null, null,null,null,input, null, new CancellationSignal());
        builder.query(null, null, null, null,null,null,null, input, new CancellationSignal());
        //buildQuery
        builder.buildQuery(null, input, null, null, null, null);
        builder.buildQuery(null, null, input,null, null, null);
        builder.buildQuery(null, null, null, input, null, null);
        builder.buildQuery(null, null, null, null, input, null);
        builder.buildQuery(null, null, null, null, null, input);
        //buildQuery with selectionArgs
        builder.buildQuery(null, input, new String[0], null, null, null, null);
        builder.buildQuery(null, null, new String[0], input, null, null, null);
        builder.buildQuery(null, null, new String[0],null, input,null, null);
        builder.buildQuery(null, null, new String[0],null, null, input,null);
        builder.buildQuery(null, null, new String[0],null, null, null, input);
        //buildUnionQuery
        builder.buildUnionQuery(new String[] {input}, null, null);
        builder.buildUnionQuery(null, input, null);
        builder.buildUnionQuery(null, null, input);
        //buildUnionSubQuery
        builder.buildUnionSubQuery(null, null, null,0, null, input, null, null);
        builder.buildUnionSubQuery(null, null, null,0, null, null, input, null);
        builder.buildUnionSubQuery(null, null, null,0, null, null, null, input);
        //buildUnionSubQuery with selectionArgs
        builder.buildUnionSubQuery(null, null, null,0, null, input, new String[0],null, null);
        builder.buildUnionSubQuery(null, null, null,0, null, null, new String[0], input, null);
        builder.buildUnionSubQuery(null, null, null,0, null, null, new String[0], null, input);
    }
}

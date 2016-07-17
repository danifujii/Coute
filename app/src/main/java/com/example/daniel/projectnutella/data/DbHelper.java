package com.example.daniel.projectnutella.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Daniel on 13/7/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Nutella.db";

    //TABLE COLUMNS
    private static final String[] COLUMNS_POCKET = {Tables.COLUMN_ID, Tables.COLUMN_POCKET_NAME, Tables.COLUMN_POCKET_BALANCE};
    private static final String[] COLUMNS_TRANS = {Tables.COLUMN_ID, Tables.COLUMN_TRANS_AMOUNT, Tables.COLUMN_TRANS_INCOME,
            Tables.COLUMN_TRANS_DATE, Tables.COLUMN_TRANS_FK_POCKET, Tables.COLUMN_TRANS_FK_CAT};
    private static final String[] COLUMNS_CAT = {Tables.COLUMN_ID, Tables.COLUMN_CAT_NAME};

    //CREATION SCRIPTS
    private static final String CREATE_POCKET = "CREATE TABLE " + Tables.TABLE_NAME_POCKET + " ( "
            + Tables.COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,"
            + Tables.COLUMN_POCKET_NAME + " varchar(60) NOT NULL," + Tables.COLUMN_POCKET_BALANCE + " numeric(20,2) NOT NULL)";
    private static final String CREATE_TRANS = "CREATE TABLE " + Tables.TABLE_NAME_TRANS + " ( "
            + Tables.COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,"
            + Tables.COLUMN_TRANS_AMOUNT + " numeric(20,2) NOT NULL,"
            + Tables.COLUMN_TRANS_INCOME + " boolean NOT NULL,"
            + Tables.COLUMN_TRANS_DATE + " date NOT NULL,"
            + Tables.COLUMN_TRANS_FK_POCKET + " integer NOT NULL,"
            + Tables.COLUMN_TRANS_FK_CAT + " integer NOT NULL,"
            + "CONSTRAINT FK_Pocket FOREIGN KEY ("+Tables.COLUMN_TRANS_FK_POCKET+") REFERENCES "
            + Tables.TABLE_NAME_POCKET+"("+Tables.COLUMN_ID+"),"
            + "CONSTRAINT FK_Categoria FOREIGN KEY ("+Tables.COLUMN_TRANS_FK_CAT+") REFERENCES "
            + Tables.TABLE_NAME_CAT + "(" + Tables.COLUMN_ID + "))";
    private static final String CREATE_CAT = "CREATE TABLE " + Tables.TABLE_NAME_CAT + " ( "
            + Tables.COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,"
            + Tables.COLUMN_CAT_NAME + " varchar(20) NOT NULL)";

    //HAVING CLAUSES FOR RANGES OF DATE
    public static final String[] RANGES = {"",
        " AND DATE("+Tables.COLUMN_TRANS_DATE+") BETWEEN DATE('now', '-7 days') AND DATE('now')",
        " AND strftime('%m', "+Tables.COLUMN_TRANS_DATE+") = strftime('%m', date('now')) " +
                "AND strftime('%Y', "+Tables.COLUMN_TRANS_DATE+") = strftime('%Y', date('now'))",
        " AND strftime('%Y', "+Tables.COLUMN_TRANS_DATE+") = strftime('%Y', date('now'))"
    };

    public DbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_POCKET);
        db.execSQL(CREATE_CAT);
        db.execSQL(CREATE_TRANS);
    }

    public void insertCategory(String name){
        SQLiteDatabase db = getWritableDatabase();
        if (db.rawQuery("SELECT 1 FROM " + Tables.TABLE_NAME_CAT
                + " WHERE "+ Tables.COLUMN_CAT_NAME + "='" + name + "'", null).getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(Tables.COLUMN_CAT_NAME, name);
            db.insert(Tables.TABLE_NAME_CAT, null, values);
        }
        db.close();
    }

    public void insertPocket(String name){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Tables.COLUMN_POCKET_NAME,name);
        values.put(Tables.COLUMN_POCKET_BALANCE,0.0);
        db.insert(Tables.TABLE_NAME_POCKET,null,values);
        db.close();
    }

    public void insertTransaction(Transaction t){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Tables.COLUMN_TRANS_AMOUNT,t.getAmount());
        values.put(Tables.COLUMN_TRANS_DATE,t.getDate());
        values.put(Tables.COLUMN_TRANS_FK_CAT,t.getCat());
        values.put(Tables.COLUMN_TRANS_FK_POCKET,t.getPocket());
        values.put(Tables.COLUMN_TRANS_INCOME,t.getIsIncome());
        db.insert(Tables.TABLE_NAME_TRANS,null,values);
        db.close();
    }

    public Cursor getTransactions(int pocketId){
        return getWritableDatabase().rawQuery("SELECT * FROM " + Tables.TABLE_NAME_TRANS
                + " WHERE " + Tables.COLUMN_TRANS_FK_POCKET + "='" + String.valueOf(pocketId) + "'"
                + " ORDER BY " + Tables.COLUMN_TRANS_DATE + " DESC", null);
    }

    public Cursor getPockets(){
        return getWritableDatabase().rawQuery("SELECT * FROM " + Tables.TABLE_NAME_POCKET, null);
    }

    //get transactions grouped by category, but divided by expense and income
    public Cursor getTransGroupedCat(int pocketId, String range){
        return getWritableDatabase().rawQuery("SELECT SUM(" + Tables.COLUMN_TRANS_AMOUNT
                + ")," + Tables.COLUMN_TRANS_FK_CAT
                + " FROM " + Tables.TABLE_NAME_TRANS
                + " WHERE " + Tables.COLUMN_TRANS_FK_POCKET + "='" + String.valueOf(pocketId) + "'"
                + range
                + " GROUP BY " + Tables.COLUMN_TRANS_FK_CAT
                + " ORDER BY SUM(" + Tables.COLUMN_TRANS_AMOUNT + ") DESC",
                 null);
    }

    public boolean existsPocket(String name){
        Cursor cursor = getWritableDatabase().rawQuery("SELECT 1 FROM " + Tables.TABLE_NAME_POCKET
                    + " WHERE "+ Tables.COLUMN_POCKET_NAME + "='" + name + "'", null);
        boolean exists = (cursor.getCount()>0);
        cursor.close();
        return (exists);
    }

    public String getPocketName(int id){
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM " + Tables.TABLE_NAME_POCKET
                        + " WHERE " + Tables.COLUMN_ID + "='" + String.valueOf(id) + "'", null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            return cursor.getString(1);
        }
        else return "";
    }

    public String getCategoryName(int id){
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM " + Tables.TABLE_NAME_CAT
                + " WHERE " + Tables.COLUMN_ID + "='" + String.valueOf(id) + "'", null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            return cursor.getString(1);
        }
        else return "";
    }

    public Cursor getTransactions(int pocketId, String date){
        return getWritableDatabase().rawQuery("SELECT * FROM " + Tables.TABLE_NAME_TRANS
                + " WHERE " + Tables.COLUMN_TRANS_FK_POCKET + "='" + String.valueOf(pocketId)
                + "' AND date(" + Tables.COLUMN_TRANS_DATE + ")='" + date + "'", null);
    }

    public Cursor getDates(int pocketId){
        return getWritableDatabase().rawQuery("SELECT DISTINCT date("
                + Tables.COLUMN_TRANS_DATE + ") FROM " + Tables.TABLE_NAME_TRANS
                + " WHERE " + Tables.COLUMN_TRANS_FK_POCKET + "='" + String.valueOf(pocketId) + "'"
                + " ORDER BY " + Tables.COLUMN_TRANS_DATE + " ASC"
                ,null);
    }

    public void deletePocket(int id){
        delete(Tables.TABLE_NAME_TRANS,Tables.COLUMN_TRANS_FK_POCKET,String.valueOf(id));
        delete(Tables.TABLE_NAME_POCKET,Tables.COLUMN_ID,String.valueOf(id));
    }

    public void deleteTransaction(int id){
        delete(Tables.TABLE_NAME_TRANS,Tables.COLUMN_ID,String.valueOf(id));
    }

    private void delete(String tableName, String columnName, String value){
        String[] selectionArgs = { value };
        SQLiteDatabase db = getWritableDatabase();
        db.delete(tableName,
                columnName + "=?",
                selectionArgs);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.TABLE_NAME_TRANS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.TABLE_NAME_POCKET);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.TABLE_NAME_CAT);
        onCreate(db);
    }

    public static class Tables{
        public static String TABLE_NAME_POCKET = "POCKET";
        public static String TABLE_NAME_TRANS = "TRANS";
        public static String TABLE_NAME_CAT = "CATEGORY";

        public static String COLUMN_ID = "_id";
        public static String COLUMN_POCKET_NAME = "name";
        public static String COLUMN_POCKET_BALANCE = "balance";

        public static String COLUMN_TRANS_AMOUNT = "amount";
        public static String COLUMN_TRANS_INCOME = "income";
        public static String COLUMN_TRANS_DATE = "dat";
        public static String COLUMN_TRANS_FK_POCKET = "_id_pocket";
        public static String COLUMN_TRANS_FK_CAT = "_id_cat";

        public static String COLUMN_CAT_NAME = "name";
    }

}

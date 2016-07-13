package com.example.daniel.projectnutella.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daniel on 13/7/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Nutella.db";

    //TABLE COLUMNS
    private static final String[] COLUMNS_POCKET = {Tables.COLUMN_ID, Tables.COLUMN_POCKET_NAME, Tables.COLUMN_POCKET_BALANCE};
    private static final String[] COLUMNS_TRANS = {Tables.COLUMN_ID, Tables.COLUMN_TRANS_AMOUNT, Tables.COLUMN_TRANS_INCOME, Tables.COLUMN_TRANS_DATE,
            Tables.COLUMN_TRANS_FK_POCKET, Tables.COLUMN_TRANS_FK_CAT};
    private static final String[] COLUMNS_CAT = {Tables.COLUMN_ID, Tables.COLUMN_CAT_NAME};

    //CREATION SCRIPTS
    private static final String CREATE_POCKET = "CREATE TABLE " + Tables.TABLE_NAME_POCKET + "(" + Tables.COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,"
            + Tables.COLUMN_POCKET_NAME + " varchar(60) NOT NULL," + Tables.COLUMN_POCKET_BALANCE + " numeric(20,2) NOT NULL);";
    private static final String CREATE_TRANS = "CREATE TABLE " + Tables.TABLE_NAME_TRANS + "(" + Tables.COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,"
            + Tables.COLUMN_TRANS_AMOUNT + " numeric(20,2) NOT NULL," + Tables.COLUMN_TRANS_INCOME + " boolean NOT NULL,"
            + Tables.COLUMN_TRANS_DATE + " date NOT NULL," + Tables.COLUMN_TRANS_FK_POCKET + " integer NOT NULL,"
            + Tables.COLUMN_TRANS_FK_CAT + "integer NOT NULL,"
            + "CONSTRAINT FK_Pocket FOREIGN KEY ("+Tables.COLUMN_TRANS_FK_POCKET+") REFERENCES "
            + Tables.TABLE_NAME_POCKET+"("+Tables.COLUMN_ID+"),"
            + "CONSTRAINT FK_Categoria FOREIGN KEY ("+Tables.COLUMN_TRANS_FK_CAT+") REFERENCES "
            + Tables.TABLE_NAME_CAT + "(" + Tables.COLUMN_ID + ");";
    private static final String CREATE_CAT = "CREATE TABLE " + Tables.TABLE_NAME_CAT + "(" + Tables.COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,"
            + Tables.COLUMN_CAT_NAME + "varchar(20) NOT NULL);";

    public DbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_POCKET);
        db.execSQL(CREATE_CAT);
        db.execSQL(CREATE_TRANS);
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
        public static String TABLE_NAME_TRANS = "TRANSACTION";
        public static String TABLE_NAME_CAT = "CATEGORY";

        public static String COLUMN_ID = "_id";
        public static String COLUMN_POCKET_NAME = "name";
        public static String COLUMN_POCKET_BALANCE = "balance";

        public static String COLUMN_TRANS_AMOUNT = "amount";
        public static String COLUMN_TRANS_INCOME = "income";
        public static String COLUMN_TRANS_DATE = "date";
        public static String COLUMN_TRANS_FK_POCKET = "_id_pocket";
        public static String COLUMN_TRANS_FK_CAT = "_id_cat";

        public static String COLUMN_CAT_NAME = "name";
    }

}

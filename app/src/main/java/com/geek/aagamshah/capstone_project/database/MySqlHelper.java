package com.geek.aagamshah.capstone_project.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aagam Shah on 11/23/2016.
 */

public class MySqlHelper extends SQLiteOpenHelper {
    private static MySqlHelper mySqlHelper;

    public static synchronized MySqlHelper getInstance(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        if (mySqlHelper == null)
            mySqlHelper = new MySqlHelper(context, name, cursorFactory, version);
        return mySqlHelper;
    }

    private MySqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "Create table " + MyContentProvider.Contracts.fileInfo.TABLE_NAME + " ( " +
                        MyContentProvider.Contracts.fileInfo.FILE_NAME + " TEXT NOT NULL, " +
                        MyContentProvider.Contracts.fileInfo.FILE_PATH + " TEXT NOT NULL, " +
                        MyContentProvider.Contracts.fileInfo._ID + " INT PRIMARY KEY );"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop Table *;");
        onCreate(sqLiteDatabase);
    }


}

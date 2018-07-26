package com.example.root.kamusdata.helper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.root.kamusdata.helper.Config;

import static com.example.root.kamusdata.helper.Config.TABLE_ENGLISH_INDONESIA;
import static com.example.root.kamusdata.helper.Config.TABLE_INDONESIA_ENDGLIS;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_ENGLISH_INDONESIA);
        sqLiteDatabase.execSQL(TABLE_INDONESIA_ENDGLIS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ Config.DATABASE_TABLE_ENGLISH);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ Config.DATABASE_TABLE_INDONESIA);
        onCreate(sqLiteDatabase);
    }
}

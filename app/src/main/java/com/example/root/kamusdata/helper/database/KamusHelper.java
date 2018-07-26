package com.example.root.kamusdata.helper.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.root.kamusdata.helper.Config;
import com.example.root.kamusdata.model.KamusDataModel;

import java.util.ArrayList;

public class KamusHelper {
    private static String English = Config.DATABASE_TABLE_ENGLISH;
    private static String Indonesia = Config.DATABASE_TABLE_INDONESIA;

    private Context context;
    private DbHelper dbHelper;
    private SQLiteDatabase database;


    public KamusHelper(Context context) {
        this.context = context;
    }

    public KamusHelper open() throws SQLException{
        dbHelper = new DbHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {dbHelper.close();}

    public Cursor searchQueryByName(String query, boolean english){

        String DATABASE_TABLE = english ? English : Indonesia;
        return database.rawQuery("SELECT * FROM " + DATABASE_TABLE +
                " WHERE " + Config.DATABASE_FIELD_WORD + " LIKE '%" + query.trim() + "%'", null);
    }

    public ArrayList<KamusDataModel> getDataByName(String search, boolean english) {
        KamusDataModel kamusDataModel;

        ArrayList<KamusDataModel> arrayList = new ArrayList<>();
        Cursor cursor = searchQueryByName(search, english);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                kamusDataModel = new KamusDataModel();
                kamusDataModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Config.DATABASE_FIELD_ID)));
                kamusDataModel.setWord(cursor.getString(cursor.getColumnIndexOrThrow(Config.DATABASE_FIELD_WORD)));
                kamusDataModel.setTranslate(cursor.getString(cursor.getColumnIndexOrThrow(Config.DATABASE_FIELD_TRANSLATION)));
                arrayList.add(kamusDataModel);

                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public Cursor queryAllData(boolean english) {
        String DATABASE_TABLE = english ? English : Indonesia;
        return database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " ORDER BY " + Config.DATABASE_FIELD_ID + " ASC", null);
    }

    public ArrayList<KamusDataModel> getDataALl(boolean english) {
        KamusDataModel kamusDataModel;

        ArrayList<KamusDataModel> arrayList = new ArrayList<>();
        Cursor cursor = queryAllData(english);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                kamusDataModel = new KamusDataModel();
                kamusDataModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Config.DATABASE_FIELD_ID)));
                kamusDataModel.setWord(cursor.getString(cursor.getColumnIndexOrThrow(Config.DATABASE_FIELD_WORD)));
                kamusDataModel.setTranslate(cursor.getString(cursor.getColumnIndexOrThrow(Config.DATABASE_FIELD_TRANSLATION)));
                arrayList.add(kamusDataModel);

                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }


    public long insert(KamusDataModel kamusDataModel, boolean english) {
        String DATABASE_TABLE = english ? English : Indonesia;
        ContentValues initialValues = new ContentValues();
        initialValues.put(Config.DATABASE_FIELD_WORD, kamusDataModel.getWord());
        initialValues.put(Config.DATABASE_FIELD_TRANSLATION, kamusDataModel.getTranslate());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public void insertTransaction(ArrayList<KamusDataModel> kamusDataModels, boolean english) {
        String DATABASE_TABLE = english ? English : Indonesia;
        String sql = "INSERT INTO " + DATABASE_TABLE + " (" +
                Config.DATABASE_FIELD_WORD + ", " +
                Config.DATABASE_FIELD_TRANSLATION + ") VALUES (?, ?)";

        database.beginTransaction();

        SQLiteStatement stmt = database.compileStatement(sql);
        for (int i = 0; i < kamusDataModels.size(); i++) {
            stmt.bindString(1, kamusDataModels.get(i).getWord());
            stmt.bindString(2, kamusDataModels.get(i).getTranslate());
            stmt.execute();
            stmt.clearBindings();
        }

        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public void update(KamusDataModel kamusDataModel, boolean english) {
        String DATABASE_TABLE = english ? English : Indonesia;
        ContentValues args = new ContentValues();
        args.put(Config.DATABASE_FIELD_WORD, kamusDataModel.getWord());
        args.put(Config.DATABASE_FIELD_TRANSLATION, kamusDataModel.getTranslate());
        database.update(DATABASE_TABLE, args, Config.DATABASE_FIELD_ID+ "= '" + kamusDataModel.getId() + "'", null);
    }

    public void delete(int id, boolean english) {
        String DATABASE_TABLE = english ? English : Indonesia;
        database.delete(DATABASE_TABLE, Config.DATABASE_FIELD_ID + " = '" + id + "'", null);
    }

}

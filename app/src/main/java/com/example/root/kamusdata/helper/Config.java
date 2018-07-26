package com.example.root.kamusdata.helper;

public final class Config {

    public static final String DATABASE_NAME = "kamus_data.db";
    public static final int DATABASE_VERSION = 1;

    public static String DATABASE_TABLE_ENGLISH = "KAMUS_DATA_ENGLISH";
    public static String DATABASE_TABLE_INDONESIA = "KAMUS_DATA_INDONESIA";
    public static String DATABASE_FIELD_ID = "ID";
    public static String DATABASE_FIELD_WORD = "WORD";
    public static String DATABASE_FIELD_TRANSLATION = "TRANSLATION";

    public static final String TABLE_ENGLISH_INDONESIA = "CREATE TABLE " + Config.DATABASE_TABLE_ENGLISH + "("
            + Config.DATABASE_FIELD_ID + " integer primary key autoincrement, "
            + Config.DATABASE_FIELD_WORD + " string, "
            + Config.DATABASE_FIELD_TRANSLATION + " text not null);";

    public static final String TABLE_INDONESIA_ENDGLIS = "CREATE TABLE " + Config.DATABASE_TABLE_INDONESIA + "("
            + Config.DATABASE_FIELD_ID + " integer primary key autoincrement, "
            + Config.DATABASE_FIELD_WORD + " string, "
            + Config.DATABASE_FIELD_TRANSLATION + " text not null);";

    public static final String BUNDLE_WORD = "word";
    public static final String BUNDLE_TRANSLATE = "translate";







}

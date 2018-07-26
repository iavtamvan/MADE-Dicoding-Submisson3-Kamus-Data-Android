package com.example.root.kamusdata.helper.database;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class MyAplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}

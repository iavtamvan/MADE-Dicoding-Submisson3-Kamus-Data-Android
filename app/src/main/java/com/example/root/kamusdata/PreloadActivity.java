package com.example.root.kamusdata;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.root.kamusdata.helper.AppPreference;
import com.example.root.kamusdata.helper.database.KamusHelper;
import com.example.root.kamusdata.model.KamusDataModel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PreloadActivity extends AppCompatActivity {

    private TextView tvLoad;
    private ProgressBar pgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);
        getSupportActionBar().hide();
        initView();

        new LoadData().execute();
    }

    private void initView() {
        tvLoad = findViewById(R.id.tv_load);
        pgBar = findViewById(R.id.pg_bar);
    }


    private class LoadData extends AsyncTask<Void, Integer, Void> {
        KamusHelper kamusHelper;
        AppPreference appPreference;
        double progress;
        double maxprogress = 100;

        @Override
        protected void onPreExecute() {
            kamusHelper = new KamusHelper(PreloadActivity.this);
            appPreference = new AppPreference(PreloadActivity.this);
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            Boolean firstRun = appPreference.getFirstRun();
            if (firstRun) {
                ArrayList<KamusDataModel> kamusEnglish = preLoadData(R.raw.english_indonesia);
                ArrayList<KamusDataModel> kamusIndonesia = preLoadData(R.raw.indonesia_english);

                publishProgress((int) progress);

//                try {
                    kamusHelper.open();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }

                Double progressMaxInsert = 100.0;
                Double progressDiff = (progressMaxInsert - progress) / (kamusEnglish.size() + kamusIndonesia.size());

                kamusHelper.insertTransaction(kamusEnglish, true);
                progress += progressDiff;
                publishProgress((int) progress);

                kamusHelper.insertTransaction(kamusIndonesia, false);
                progress += progressDiff;
                publishProgress((int) progress);

                kamusHelper.close();
                appPreference.setFirstRun(false);

                publishProgress((int) maxprogress);
            } else {
                tvLoad.setVisibility(View.INVISIBLE);
                pgBar.setVisibility(View.GONE);
                try {
                    synchronized (this){
                        this.wait(1000);
                        publishProgress((int) maxprogress);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            pgBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent i = new Intent(PreloadActivity.this, HomeActivity.class);
            startActivity(i);

            finish();
        }
    }

    public ArrayList<KamusDataModel> preLoadData(int data) {
        ArrayList<KamusDataModel> kamusDataModels = new ArrayList<>();
        BufferedReader reader;
        try {
            Resources res = getResources();
            InputStream raw_dict = res.openRawResource(data);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            String line = null;
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");
                KamusDataModel kamusDataModel;
                kamusDataModel = new KamusDataModel(splitstr[0], splitstr[1]);
                kamusDataModels.add(kamusDataModel);
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kamusDataModels;
    }
}

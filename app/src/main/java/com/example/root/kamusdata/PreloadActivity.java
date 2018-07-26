package com.example.root.kamusdata;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.root.kamusdata.helper.SharedPreference;
import com.example.root.kamusdata.helper.database.KamusDataHelper;
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

        new LoadDataKamus().execute();
    }

    private void initView() {
        tvLoad = findViewById(R.id.tv_load);
        pgBar = findViewById(R.id.pg_bar);
    }


    private class LoadDataKamus extends AsyncTask<Void, Integer, Void> {
        KamusDataHelper kamusDataHelper;
        SharedPreference sharedPreference;
        double progress;
        double maxprogress = 100;

        @Override
        protected void onPreExecute() {
            kamusDataHelper = new KamusDataHelper(PreloadActivity.this);
            sharedPreference = new SharedPreference(PreloadActivity.this);
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            Boolean firstRun = sharedPreference.getFirstRun();
            if (firstRun) {
                ArrayList<KamusDataModel> kamusDataEnglish = preLoadData(R.raw.english_indonesia);
                ArrayList<KamusDataModel> kamusDataIndonesia = preLoadData(R.raw.indonesia_english);

                publishProgress((int) progress);

                kamusDataHelper.open();

                Double progressMaxInsert = 100.0;
                Double progressDiff = (progressMaxInsert - progress) / (kamusDataEnglish.size() + kamusDataIndonesia.size());

                kamusDataHelper.insertTransaction(kamusDataEnglish, true);
                progress += progressDiff;
                publishProgress((int) progress);

                kamusDataHelper.insertTransaction(kamusDataIndonesia, false);
                progress += progressDiff;
                publishProgress((int) progress);

                kamusDataHelper.close();
                sharedPreference.setFirstRun(false);

                publishProgress((int) maxprogress);
            } else {
                tvLoad.setVisibility(View.INVISIBLE);
                pgBar.setVisibility(View.GONE);
                try {
                    synchronized (this) {
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
            Resources resources = getResources();
            InputStream raw_dict = resources.openRawResource(data);

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

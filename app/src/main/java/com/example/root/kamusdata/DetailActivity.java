package com.example.root.kamusdata;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.root.kamusdata.helper.Config;

public class DetailActivity extends AppCompatActivity {

    private TextView tvWord;
    private TextView tvTranslate;
    String word, translate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();

        Intent intent = getIntent();
        word = intent.getStringExtra(Config.BUNDLE_WORD);
        translate = intent.getStringExtra(Config.BUNDLE_TRANSLATE);
         tvWord.setText(word);
         tvTranslate.setText(translate);

    }

    private void initView() {
        tvWord = findViewById(R.id.tv_word);
        tvTranslate = findViewById(R.id.tv_translate);
    }
}

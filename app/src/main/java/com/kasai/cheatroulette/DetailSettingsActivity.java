package com.kasai.cheatroulette;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailSettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_settings);

        // Toolbarの設定
        Toolbar toolbar = findViewById(R.id.detail_settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // トップ画面のFragmentを表示
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_settings_container, new DetailSettingsFragment())
                .commit();
    }

    //ツールバーの戻るボタンを押した時の処理
    @Override
    public boolean onSupportNavigateUp() {
        finish();

        return super.onSupportNavigateUp();
    }
}
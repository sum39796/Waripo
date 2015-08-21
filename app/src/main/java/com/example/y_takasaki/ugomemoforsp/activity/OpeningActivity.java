package com.example.y_takasaki.ugomemoforsp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.example.y_takasaki.ugomemoforsp.PreferencesKeys;
import com.example.y_takasaki.ugomemoforsp.R;

import java.io.File;

/**
 * Created by Y-Takasaki on 15/05/16.
 */
public class OpeningActivity extends Activity {
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        prefs = getSharedPreferences(PreferencesKeys.PREF_KEY_PROJECT, MODE_PRIVATE);

        File directory = new File(Environment.getExternalStorageDirectory(), "UMProjects");

        if (directory.exists() == false) {
            directory.mkdir();
        }
    }

    public void make(View v) {

        int UMPJNumber = prefs.getInt(PreferencesKeys.PREF_KEY_NUMBER, 1);
        File directory = new File(Environment.getExternalStorageDirectory(), "UMProjects/UMP-" + UMPJNumber);

        if (directory.exists() == false) {
            directory.mkdir();
            Intent intent = new Intent(this, CanvasActivity.class);
            startActivity(intent);
        }

        prefs.edit()
                // UMPJNumber を更新する
                .putInt(PreferencesKeys.PREF_KEY_NUMBER, UMPJNumber + 1)
                // 保存する
                .commit();
    }

    public void see(View v) {
        Intent intent = new Intent(this, WatchListActivity.class);
        startActivity(intent);

    }

}

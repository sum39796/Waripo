package com.example.y_takasaki.ugomemoforsp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;

/**
 * Created by Y-Takasaki on 15/05/16.
 */
public class OpeningActivity extends Activity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        prefs=getSharedPreferences("ProjectPreferences",MODE_PRIVATE);
    }

    public void make(View v){

        int UMPJNumber=prefs.getInt("UMPJNumber", 1);
        File makeDir = new File(Environment.getExternalStorageDirectory(), "UMproject"+UMPJNumber);
        if (makeDir.exists() == false) {
            makeDir.mkdir();
            Intent intent = new Intent(this, CanvasActivity.class);
            startActivity(intent);
        }
        editor = prefs.edit();
        editor.putInt("UMPJNumber",UMPJNumber+1);
        editor.commit();
    }

}

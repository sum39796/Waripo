package com.example.y_takasaki.ugomemoforsp;

/**
 * Created by Y-Takasaki on 15/02/21.
 */

import android.content.Context;
import android.os.Environment;

import java.text.DecimalFormat;

public class AppConfig {

    public static final String APPNAME = "/UMproject";

    public static String getDirPath(Context context, int result) {
        int UMPJNumber = context.getSharedPreferences(PreferencesKeys.PREF_KEY_PROJECT, Context.MODE_PRIVATE).getInt(PreferencesKeys.PREF_KEY_NUMBER, 1);
        UMPJNumber--;
        return Environment.getExternalStorageDirectory() + "/UMProjects/UMP-" + UMPJNumber;

    }

    public static String getFilePath(Context context, int imageNumber) {
        int result = context.getSharedPreferences("ProjectPreferences", Context.MODE_PRIVATE).getInt("UMPJNumber", 1);
        DecimalFormat form = new DecimalFormat("0000");
        return getDirPath(context, result) + "/" + "img" + form.format(imageNumber) + ".png";
    }

}

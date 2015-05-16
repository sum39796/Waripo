package com.example.y_takasaki.ugomemoforsp;

/**
 * Created by Y-Takasaki on 15/02/21.
 */
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.text.DecimalFormat;

public class AppConfig {


    public static final String APPNAME = "/UMproject";

    public static String getDirPath(int result) {
        //return Environment.getExternalStorageDirectory()+"/"+APPNAME+"/";
        return Environment.getExternalStorageDirectory()+"/"+APPNAME+(result-1)+"//";
    }

    public static String getDirPath(SharedPreferences argPref) {
        int result = argPref.getInt("UMPJNumber", 1);
        Log.d("UMPJNumber", "result : " + result);
        return Environment.getExternalStorageDirectory()+"/"+APPNAME+(result-1)+"//";

    }

    public static String getFilePath(int imageNumber,SharedPreferences argPref) {
        int result = argPref.getInt("UMPJNumber", 1);
        DecimalFormat form=new DecimalFormat("0000");
        return getDirPath(result)+"img"+form.format(imageNumber)+".png";
    }

}

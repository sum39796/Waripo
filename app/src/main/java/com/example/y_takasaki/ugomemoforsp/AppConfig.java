package com.example.y_takasaki.ugomemoforsp;

/**
 * Created by Y-Takasaki on 15/02/21.
 */
import android.os.Environment;

import java.text.DecimalFormat;

public class AppConfig {

    public static final String APPNAME = "/Painted/";

    public static String getDirPath() {
        return Environment.getExternalStorageDirectory()+"/"+APPNAME+"/";
    }

    public static String getFilePath(int imageNumber) {
        DecimalFormat form=new DecimalFormat("0000");
        return getDirPath()+"img"+form.format(imageNumber)+".png";
    }


}

package com.example.y_takasaki.ugomemoforsp;

import android.graphics.Bitmap;

/**
 * Created by Y-Takasaki on 15/06/27.
 */
public class CustomData {
    private Bitmap imageData_;
    private String textData_;

    public void setImagaData(Bitmap image) {
        imageData_ = image;
    }

    public Bitmap getImageData() {
        return imageData_;
    }

    public void setTextData(String text) {
        textData_ = text;
    }

    public String getTextData() {
        return textData_;
    }
}

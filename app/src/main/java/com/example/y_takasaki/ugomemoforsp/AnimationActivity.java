package com.example.y_takasaki.ugomemoforsp;

/**
 * Created by Y-Takasaki on 15/02/21.
 * 跳んだ！！
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.util.ArrayList;

public class AnimationActivity extends Activity {

    private AnimationImageView mAnimImageView;
    private ProgressDialog waitDialog;
    MyAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        mAnimImageView = (AnimationImageView) findViewById(R.id.animationImageView);
        waitDialog = new ProgressDialog(this);
        waitDialog.setMessage("アニメーションを読み込み中…");
        waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitDialog.show();

        task = new MyAsyncTask(this, new MyAsyncTask.GetImagedListener() {
            @Override
            public void onGetImaged(ArrayList<Bitmap> frames) {
                waitDialog.dismiss();
                waitDialog = null;
                mAnimImageView.setFrame(frames);
            }
        });
        task.execute();
    }
    private String getPath(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        String[] columns = { MediaStore.Images.Media.DATA };
        Cursor cursor = contentResolver.query(uri, columns, null, null, null);
        cursor.moveToFirst();
        String path = cursor.getString(0);
        cursor.close();
        return path;
    }

}

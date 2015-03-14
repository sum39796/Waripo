package com.example.y_takasaki.ugomemoforsp;

/**
 * Created by Y-Takasaki on 15/02/21.
 */
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import java.util.ArrayList;

public class AnimationActivity extends Activity {

    private AnimationImageView mAnimImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        mAnimImageView = (AnimationImageView) findViewById(R.id.animationImageView);
        getGalleryImageUris(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    private void getGalleryImageUris(Uri uriType) {
        ArrayList<Bitmap> frames = new ArrayList<Bitmap>();
        CursorLoader cursorLoader = new CursorLoader(
                this,
                uriType,
                null,
                null,
                null, // Selection args (none).
                null);

        Cursor cursor = cursorLoader.loadInBackground();
        //�ｽ�ｽ�ｽR�ｽ[�ｽh�ｽﾌ取得
        //Cursor cursor = managedQuery(uriType, null, null, null, null);
        cursor.moveToFirst();
        int fieldIndex;
        Long id;
        while (cursor.moveToNext()) {
            //�ｽJ�ｽ�ｽ�ｽ�ｽID�ｽﾌ取得
            fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            id = cursor.getLong(fieldIndex);
            if( getPath(ContentUris.withAppendedId(uriType, id)).contains(AppConfig.APPNAME) ) {
                id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                if(bitmap!=null) {
                    frames.add(bitmap);
                }
            }
        }
        mAnimImageView.setFrame(frames);
    }

    /**
     * Uri縺九ｉPath縺ｸ縺ｮ螟画鋤蜃ｦ逅�
     * @param uri
     * @return String
     */
    private String getPath(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        String[] columns = { MediaStore.Images.Media.DATA };
        Cursor cursor = contentResolver.query(uri, columns, null, null, null);
        cursor.moveToFirst();
        String path = cursor.getString(0);
        cursor.close();

        Log.d("GalleryAcitivity getPath()", path);
        return path;
    }

}

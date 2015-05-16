package com.example.y_takasaki.ugomemoforsp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Y-Takasaki on 15/03/14.
 */
public class MyAsyncTask extends AsyncTask<Void, Void, ArrayList<Bitmap>> {

    private GetImagedListener mGetImageListener;
    private Activity activity;
    private Handler mHandler;
    private CursorLoader cursorLoader;

    public MyAsyncTask(Activity activity, GetImagedListener listener) {
        super();
        this.activity = activity;//現在のActivityを引数として代入
        this.mGetImageListener = listener;
        mHandler = new Handler();
        cursorLoader = new CursorLoader(activity);
        cursorLoader.setUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    //@Override
    protected ArrayList<Bitmap> doInBackground(Void... params) {
        ArrayList<Bitmap> frames = new ArrayList<Bitmap>();
        return getGalleryImageUris(frames, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    //@Override
    protected void onPostExecute(ArrayList<Bitmap> frames) { //非同期処理が終了した際に実行
        mGetImageListener.onGetImaged(frames);
    }

    private ArrayList<Bitmap> getGalleryImageUris(ArrayList<Bitmap> frames, Uri uriType) {

        Cursor cursor = cursorLoader.loadInBackground();
        //�ｽ�ｽ�ｽR�ｽ[�ｽh�ｽﾌ取得
        //Cursor cursor = managedQuery(uriType, null, null, null, null);
        cursor.moveToFirst();
        int fieldIndex;
        Long id;
        for (; cursor.moveToNext(); ) {
            //�ｽJ�ｽ�ｽ�ｽ�ｽID�ｽﾌ取得
            fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            id = cursor.getLong(fieldIndex);
            if (getPath(ContentUris.withAppendedId(uriType, id)).contains(AppConfig.APPNAME)) {
                id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(activity.getContentResolver(), id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                if (bitmap != null) {
                    frames.add(bitmap);
                }
            }
        }
        return frames;
    }

    private String getPath(Uri uri) {
        ContentResolver contentResolver = activity.getContentResolver();
        String[] columns = {MediaStore.Images.Media.DATA};
        Cursor cursor = contentResolver.query(uri, columns, null, null, null);
        cursor.moveToFirst();
        String path = cursor.getString(0);
        cursor.close();

        Log.d("GalleryAcitivity ", "getPath()" + path);
        return path;
    }

    public interface GetImagedListener {
        public void onGetImaged(ArrayList<Bitmap> frames);
    }


}

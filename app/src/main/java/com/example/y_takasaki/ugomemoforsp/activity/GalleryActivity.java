package com.example.y_takasaki.ugomemoforsp.activity;

/**
 * Created by Y-Takasaki on 15/02/21.
 */

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.y_takasaki.ugomemoforsp.GalleryImage;
import com.example.y_takasaki.ugomemoforsp.R;
import com.example.y_takasaki.ugomemoforsp.SelAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends Activity {

    private GridView mGridView;
    private SelAdapter mAdapter;

    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mGridView = (GridView) findViewById(R.id.gallery_gridView);
        mAdapter = new SelAdapter(this, new ArrayList<GalleryImage>() );
        mGridView.setAdapter(mAdapter);

        Intent intent = getIntent();
        mPath = intent.getStringExtra("path");

        getGalleryImageUris(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GalleryImage item = mAdapter.getItem(position);
                Intent intent = new Intent(GalleryActivity.this, CanvasActivity.class);
                //intent.setData(Uri.fromFile(item.file));
                intent.putExtra("load_file", Uri.fromFile(item.file).toString());
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery, menu);
        return true;
    }

    private void getGalleryImageUris(Uri uriType) {
        List<GalleryImage> gallaryImages = new ArrayList<GalleryImage>();
        //�ｽ�ｽ�ｽR�ｽ[�ｽh�ｽﾌ取得
        if(TextUtils.isEmpty(mPath)) return;
        File[] filelist = new File(mPath).listFiles();
        if (filelist != null) {
            Log.d(getClass().getSimpleName(), "File List Length:" + filelist.length);
        }
        List<Bitmap> imageList = new ArrayList<>();

        for(File file : filelist) {
            if(file.getName().contains(".png")) {
                GalleryImage galleryImage = new GalleryImage();
                // 画像の場所
                //galleryImage.uri = ContentUris.withAppendedId(uriType, id);
                galleryImage.uri = Uri.fromFile(file);
                galleryImage.file = file;
                // id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                // 画像のBitmapデータ
//                galleryImage.thumnailBitmap = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                gallaryImages.add(galleryImage);
            }
        }
        mAdapter.addAll(gallaryImages);

        /*Cursor cursor = managedQuery(uriType, null, null, null, null);
        cursor.moveToFirst();
        int fieldIndex;
        Long id;
        while (cursor.moveToNext()) {
            //�ｽJ�ｽ�ｽ�ｽ�ｽID�ｽﾌ取得
            fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            id = cursor.getLong(fieldIndex);

            if( getPath(ContentUris.withAppendedId(uriType, id)).contains(AppConfig.APPNAME) ) {
                //ID�ｽ�ｽ�ｽ�ｽURI�ｽ�ｽ�ｽ謫ｾ
                GalleryImage galleryImage = new GalleryImage();
                // 画像の場所
                galleryImage.uri = ContentUris.withAppendedId(uriType, id);
                id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                // 画像のBitmapデータ
                galleryImage.thumnailBitmap = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                gallaryImages.add(galleryImage);
            }
        }
        mAdapter.addContents(gallaryImages);*/
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
        return path;
    }

}

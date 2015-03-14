package com.example.y_takasaki.ugomemoforsp;

/**
 * Created by Y-Takasaki on 15/02/21.
 */
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends Activity {

    private GridView mGridView;
    private SelAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mGridView = (GridView) findViewById(R.id.gallery_gridView);
        mAdapter = new SelAdapter(this, new ArrayList<GalleryImage>() );
        mGridView.setAdapter(mAdapter);

        //getGalleryImageUris(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        getGalleryImageUris(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery, menu);
        return true;
    }

    private void getGalleryImageUris(Uri uriType) {
        List<GalleryImage> gallaryImages = new ArrayList<GalleryImage>();
        //�ｽ�ｽ�ｽR�ｽ[�ｽh�ｽﾌ取得
        Cursor cursor = managedQuery(uriType, null, null, null, null);
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
                galleryImage.uri = ContentUris.withAppendedId(uriType, id);
                id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                galleryImage.thumnailBitmap = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                gallaryImages.add(galleryImage);
            }
        }
        mAdapter.addContents(gallaryImages);
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
        Log.d("GalleryAcitivity getPath()", uri.toString());
        Log.d("GalleryAcitivity getPath()", path);
        return path;
    }

}

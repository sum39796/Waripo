package com.example.y_takasaki.ugomemoforsp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class CanvasActivity extends Activity {
    Paint mPaint;
    Canvas mCanvas;
    Path mPath;
    Bitmap mBitmap;
    ImageView mImageView;
    ImageView TouchView;
    AlertDialog.Builder mAlertBuilder;
    MyAsyncTask task;
    float x1,y1;
    int width,height;
    int fude;
    private DrawerLayout mDrawer;
    private static ProgressDialog waitDialog;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mImageView=(ImageView) findViewById(R.id.imageView);
        TouchView=(ImageView) findViewById(R.id.imageView1);
        Display disp=((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        width=disp.getWidth();
        height=disp.getHeight();
        mBitmap=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mPaint=new Paint();
        mPath=new Path();
        mCanvas=new Canvas(mBitmap);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mCanvas.drawColor(Color.WHITE);
        mImageView.setImageBitmap(mBitmap);
        mImageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x=event.getX();
                float y=event.getY();
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mPath.reset();
                        mPath.moveTo(x, y);
                        x1=x;
                        y1=y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mPath.quadTo(x1, y1, x, y);
                        x1=x;
                        y1=y;
                        mCanvas.drawPath(mPath,mPaint);
                        mPath.reset();
                        mPath.moveTo(x, y);
                        break;
                    case MotionEvent.ACTION_UP:
                        mPath.quadTo(x1, y1, x, y);
                        mCanvas.drawPath(mPath,mPaint);
                        mPath.reset();
                        break;
                }
                TouchView.setImageBitmap(mBitmap);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.canvas, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    //メニューが押されたときの処理
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_save:
                save();
                Toast.makeText(this, "保存しました", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_open:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 0);
                break;
            case R.id.menu_color_change:
                final String[] items=getResources().getStringArray(R.array.ColorName);
                final int[] colors=getResources().getIntArray(R.array.Color);
                mAlertBuilder=new AlertDialog.Builder(this);
                mAlertBuilder.setTitle(R.string.menu_color_change);
                mAlertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        mPaint.setColor(colors[item]);
                    }
                });
                mAlertBuilder.show();
                break;
            case R.id.menu_new:
                mAlertBuilder=new AlertDialog.Builder(this);
                mAlertBuilder.setTitle(R.string.menu_new);
                mAlertBuilder.setMessage("保存していない作業内容ば破棄されます。よろしいですか?");
                mAlertBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mCanvas.drawColor(Color.WHITE);
                                mImageView.setImageBitmap(mBitmap);
                            }
                        });
                mAlertBuilder.setNegativeButton("キャンセル",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                mAlertBuilder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gallery(View v) {
        Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
        startActivity(intent);
    }

    public void animation(View v) {
        task = new MyAsyncTask(this,);
        task.execute();
    }

    public void open(View v) {
        mDrawer.openDrawer(Gravity.LEFT);
    }

    public void enpitu(View v) {
        mPaint.setStrokeWidth(5);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        mPaint.setARGB(255, 0, 0, 0);
        mDrawer.closeDrawers();
    }

    public void keshigomu(View v){
        mPaint.setStrokeWidth(10);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        mPaint.setARGB(255, 255, 255, 255);
        mDrawer.closeDrawers();
    }

    public void nuritubu(View v) {
        mDrawer.closeDrawers();
        Toast.makeText(this, "「塗りつぶし」は未実装です", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        try {
            mBitmap=loadImage(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCanvas = new Canvas(mBitmap);
        mImageView.setImageBitmap(mBitmap);
    }
    Bitmap loadImage(Uri uri) throws IOException{
        boolean landscape=false;
        Bitmap bm;
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        InputStream is = getContentResolver().openInputStream(uri);
        BitmapFactory.decodeStream(is,null,options);
        is.close();
        int oh=options.outHeight;
        int ow=options.outWidth;
        if(ow>oh){
            landscape=true;
            oh=options.outWidth;
            ow=options.outHeight;
        }
        options.inJustDecodeBounds=false;
        options.inSampleSize=Math.max(ow/width, oh/height);
        InputStream is2 = getContentResolver().openInputStream(uri);
        bm = BitmapFactory.decodeStream(is2,null,options);
        is2.close();
        if(landscape){
            Matrix matrix = new Matrix();
            matrix.setRotate(90.0f);
            bm=Bitmap.createBitmap(bm, 0, 0,
                    bm.getWidth(), bm.getHeight(), matrix, false);
        } bm=Bitmap.createScaledBitmap(bm,(int)(width), (int)(width*(((double)oh)/((double)ow))), false);
        Bitmap offBitmap=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas offCanvas=new Canvas(offBitmap);
        offCanvas.drawBitmap(bm, 0, (height-bm.getHeight())/2, null);
        bm=offBitmap;
        return bm;
    }
    void save(){ //SharedPreferencesオブジェクトを得る
        SharedPreferences prefs=getSharedPreferences("FingarPaintPreferences",MODE_PRIVATE);
        int imageNumber=prefs.getInt("imageNumber", 1);
        File file=null;
        DecimalFormat form=new DecimalFormat("0000");
        //SDカードへ書き込む
        String dirPath = AppConfig.getDirPath();
        File outDir=new File(dirPath);
        if(!outDir.exists())outDir.mkdir();
        do{
            file=new File(AppConfig.getFilePath(imageNumber));
            imageNumber++;
        }while(file.exists());
        if(writeImage(file)){
            scanMedia(file.getPath());
            SharedPreferences.Editor editor=prefs.edit();
            editor.putInt("imageNumber", imageNumber+1);
            editor.commit();
        }
    }
    boolean writeImage(File file){
        try {
            FileOutputStream fo=new FileOutputStream(file);
            mBitmap.compress(CompressFormat.PNG, 100, fo);
            fo.flush();
            fo.close();
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    MediaScannerConnection mc;
    void scanMedia(final String fp) {
        mc = new MediaScannerConnection(this, new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onScanCompleted(String path, Uri uri) {
                mc.disconnect();
            }
            @Override
            public void onMediaScannerConnected() {
                mc.scanFile(fp,"image/*");
            }
        });
        mc.connect();
    }
}
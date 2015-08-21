package com.example.y_takasaki.ugomemoforsp.activity;

import android.app.AlertDialog;
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
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.y_takasaki.ugomemoforsp.AppConfig;
import com.example.y_takasaki.ugomemoforsp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class CanvasActivity extends ActionBarActivity {
    Paint mPaint;
    Path mPath;
    Canvas[] mCanvas;
    Bitmap[] mBitmap;
    ImageView[] mImageView;
    ImageView Thumbnail;
    AlertDialog.Builder mAlertBuilder;
    TextView mTextView;
    FrameLayout mFrameLayout;
    private Toolbar mToolBar;

    float x1, y1;
    int width, height;
    private DrawerLayout mDrawer;

    private int selectLayer = 0;
    private int strokesize = 5;

    OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPath.reset();
                    mPath.moveTo(x, y);
                    x1 = x;
                    y1 = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    mPath.quadTo(x1, y1, x, y);
                    x1 = x;
                    y1 = y;
                    mCanvas[selectLayer].drawPath(mPath, mPaint);
                    mPath.reset();
                    mPath.moveTo(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    mPath.quadTo(x1, y1, x, y);
                    mCanvas[selectLayer].drawPath(mPath, mPaint);
                    mPath.reset();
                    break;
            }
            mImageView[selectLayer].setImageBitmap(mBitmap[selectLayer]);
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        setTitle("うごくメモ帳 for SmartPhone");
        mToolBar = (Toolbar) findViewById(R.id.toolbar_view);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationIcon(R.mipmap.icon_db);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(Gravity.LEFT);
            }
        });
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mCanvas = new Canvas[2];
        mBitmap = new Bitmap[2];
        mImageView = new ImageView[2];
        mImageView[0] = (ImageView) findViewById(R.id.imageView);
        mImageView[1] = (ImageView) findViewById(R.id.imageView1);
        mFrameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        mTextView = (TextView) findViewById(R.id.textView6);
        Thumbnail = (ImageView) findViewById(R.id.imageView2);

        mFrameLayout.setDrawingCacheEnabled(true);
        mFrameLayout.destroyDrawingCache();

        Display disp = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        width = disp.getWidth();
        height = disp.getHeight();
        mPaint = new Paint();
        mPath = new Path();

        mPaint.setStrokeWidth(strokesize);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);


        mBitmap[0] = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas[0] = new Canvas(mBitmap[0]);
        mCanvas[0].drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        mBitmap[1] = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas[1] = new Canvas(mBitmap[1]);
        mCanvas[1].drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        for (int i = 0; i < 2; i++) {
            mImageView[i].setImageBitmap(mBitmap[i]);
        }
        setLayer(0);

        mTextView.setText("レイヤー1");

        Intent intent = getIntent();
        // ロードするファイルが会った場合に画像をロードする
        String loadFile = intent.getStringExtra("load_file");
        if (!TextUtils.isEmpty(loadFile)) {
            Log.d("load_file", loadFile);
            Uri uri = Uri.parse(loadFile);            // 存在チェックのためのFile。
            try {
                Log.d("onActivityResult", uri.toString());
                mBitmap[selectLayer] = loadImage(uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCanvas[selectLayer] = new Canvas(mBitmap[selectLayer]);
            mImageView[selectLayer].setImageBitmap(mBitmap[selectLayer]);
        }
        /*if (intent.getData() != null) {
            try {
                loadImage(intent.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.canvas, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    //メニューが押されたときの処理
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
                final String[] items = getResources().getStringArray(R.array.ColorName);
                final int[] colors = getResources().getIntArray(R.array.Color);
                mAlertBuilder = new AlertDialog.Builder(this);
                mAlertBuilder.setTitle(R.string.menu_color_change);
                mAlertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        mPaint.setColor(colors[item]);
                    }
                });
                mAlertBuilder.show();
                break;
            case R.id.menu_new:
                mAlertBuilder = new AlertDialog.Builder(this);
                mAlertBuilder.setTitle(R.string.menu_new);
                mAlertBuilder.setMessage("保存していない作業内容、レイヤー情報は破棄されます。よろしいですか?");
                mAlertBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                mCanvas[0].drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                                mImageView[0].setImageBitmap(mBitmap[0]);

                                mCanvas[1].drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                                mImageView[1].setImageBitmap(mBitmap[1]);
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
        Toast.makeText(this, "読み込み中…", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
        startActivity(intent);
    }

    public void animation(View v) {
        Intent intent = new Intent(this, AnimationActivity.class);
        startActivity(intent);
    }

    public void open(View v) {
        mDrawer.openDrawer(Gravity.LEFT);
    }

    public void enpitu(View v) {
        mPaint.setStrokeWidth(strokesize);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        mPaint.setARGB(255, 0, 0, 0);
        mDrawer.closeDrawers();
    }

    public void keshigomu(View v) {
        mPaint.setStrokeWidth(strokesize);
        mPaint.setARGB(0, 255, 255, 255);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mDrawer.closeDrawers();
    }

    public void nuritubu(View v) {
        mDrawer.closeDrawers();
        Toast.makeText(this, "「塗りつぶし」はまだ未実装です。サーセン", Toast.LENGTH_LONG).show();
    }

    public void kuro(View v) {
        mPaint.setARGB(255, 0, 0, 0);
        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.ADD);
        mPaint.setXfermode(mode);
        mDrawer.closeDrawers();
    }

    public void aka(View v) {
        mPaint.setARGB(255, 255, 0, 0);
        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.ADD);
        mPaint.setXfermode(mode);
        mDrawer.closeDrawers();
    }

    public void midori(View v) {
        mPaint.setARGB(255, 0, 255, 0);
        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.ADD);
        mPaint.setXfermode(mode);
        mDrawer.closeDrawers();
    }

    public void ao(View v) {
        mPaint.setARGB(255, 0, 0, 255);
        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.ADD);
        mPaint.setXfermode(mode);
        mDrawer.closeDrawers();
    }

    public void mizuiro(View v) {
        mPaint.setARGB(255, 175, 223, 228);
        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.ADD);
        mPaint.setXfermode(mode);
        mDrawer.closeDrawers();
    }

    public void pinku(View v) {
        mPaint.setARGB(255, 255, 192, 203);
        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.ADD);
        mPaint.setXfermode(mode);
        mDrawer.closeDrawers();
    }

    public void kiiro(View v) {
        mPaint.setARGB(255, 255, 212, 0);
        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.ADD);
        mPaint.setXfermode(mode);
        mDrawer.closeDrawers();
    }

    public void shiro(View v) {
        mPaint.setARGB(255, 255, 255, 255);
        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.ADD);
        mPaint.setXfermode(mode);
        mDrawer.closeDrawers();
    }

    public void layer1(View v) {
        mTextView.setText("レイヤー1");
        mDrawer.closeDrawers();
        setLayer(0);
    }

    public void layer2(View v) {
        mTextView.setText("レイヤー2");
        mDrawer.closeDrawers();
        setLayer(1);
    }

    public void hosome(View v) {
        strokesize = 1;
        mPaint.setStrokeWidth(strokesize);
        mDrawer.closeDrawers();
    }

    public void tyuukann1(View v) {
        strokesize = 5;
        mPaint.setStrokeWidth(strokesize);
        mDrawer.closeDrawers();
    }

    public void tyuukann2(View v) {
        strokesize = 10;
        mPaint.setStrokeWidth(strokesize);
        mDrawer.closeDrawers();
    }

    public void futome(View v) {
        strokesize = 15;
        mPaint.setStrokeWidth(strokesize);
        mDrawer.closeDrawers();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        try {
            Log.d("onActivityResult", uri.toString());
            mBitmap[selectLayer] = loadImage(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCanvas[selectLayer] = new Canvas(mBitmap[selectLayer]);
        mImageView[selectLayer].setImageBitmap(mBitmap[selectLayer]);
    }

    Bitmap loadImage(Uri uri) throws IOException {
        boolean landscape = false;
        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream is = getContentResolver().openInputStream(uri);
        BitmapFactory.decodeStream(is, null, options);
        is.close();
        int oh = options.outHeight;
        int ow = options.outWidth;
        if (ow > oh) {
            landscape = true;
            oh = options.outWidth;
            ow = options.outHeight;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = Math.max(ow / width, oh / height);
        InputStream is2 = getContentResolver().openInputStream(uri);
        bm = BitmapFactory.decodeStream(is2, null, options);
        is2.close();
        if (landscape) {
            Matrix matrix = new Matrix();
            matrix.setRotate(90.0f);
            bm = Bitmap.createBitmap(bm, 0, 0,
                    bm.getWidth(), bm.getHeight(), matrix, false);
        }
        bm = Bitmap.createScaledBitmap(bm, (width), (int) (width * (((double) oh) / ((double) ow))), false);
        Bitmap offBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas offCanvas = new Canvas(offBitmap);
        offCanvas.drawBitmap(bm, 0, (height - bm.getHeight()) / 2, null);
        bm = offBitmap;
        return bm;
    }

    void save() { //SharedPreferencesオブジェクトを得る
        SharedPreferences prefs = getSharedPreferences("FingarPaintPreferences", MODE_PRIVATE);
        int imageNumber = prefs.getInt("imageNumber", 1);
        File file = null;
        DecimalFormat form = new DecimalFormat("0000");

        do {
            file = new File(AppConfig.getFilePath(this, imageNumber));
            //imageNumber++;
        } while (file.exists());
        if (writeImage(file)) {
            scanMedia(file.getPath());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("imageNumber", imageNumber + 1);
            Log.d("putInt imageNumber", "imageNumber+1 :" + imageNumber);
            editor.commit();
        }
    }

    boolean writeImage(File file) {
        try {
            FileOutputStream fo = new FileOutputStream(file);

            mFrameLayout.setDrawingCacheEnabled(false);
            mFrameLayout.setDrawingCacheEnabled(true);

            Bitmap bitmap = Bitmap.createBitmap(mFrameLayout.getDrawingCache());

            bitmap.compress(CompressFormat.PNG, 100, fo);
            Log.d("Write Image", bitmap.toString());
            fo.flush();
            fo.close();
        } catch (Exception e) {
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
                mc.scanFile(fp, "image/*");
            }
        });
        mc.connect();
    }

    public void setLayer(int layer) {
        //TODO 例外条件書いといて！
        selectLayer = layer;
        for (int i = 0; i < mImageView.length; i++) {
            if (i == selectLayer) {
                mImageView[i].setOnTouchListener(touchListener);
            } else {
                mImageView[i].setOnTouchListener(null);
            }
        }
        Thumbnail.setImageBitmap(mBitmap[selectLayer]);
    }
}
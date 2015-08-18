package com.example.y_takasaki.ugomemoforsp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Y-Takasaki on 15/06/27.
 */
public class WatchListActivity extends Activity {

    private ListView watch;
    private FileAdapter fileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);
        watch = (ListView) findViewById(R.id.listView);
        fileAdapter = new FileAdapter(this, new ArrayList<File>());
        watch.setAdapter(fileAdapter);
        watch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File item = fileAdapter.getItem(position);
                Toast.makeText(getApplicationContext(), item.getName() + "くりっくど", Toast.LENGTH_LONG).show();
            }
        });

        File[] filelist = new File(Environment.getExternalStorageDirectory() + "/UMProjects/").listFiles();
        if (filelist != null) {
            Log.d(getClass().getSimpleName(), "File List Length:" + filelist.length);
            fileAdapter.addAll(filelist);
        }
    }

}

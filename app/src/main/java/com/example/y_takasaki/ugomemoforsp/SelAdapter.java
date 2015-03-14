package com.example.y_takasaki.ugomemoforsp;

/**
 * Created by Y-Takasaki on 15/02/21.
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelAdapter extends BaseAdapter {

    private List<GalleryImage> mImagesUri = new ArrayList<GalleryImage>();
    private LayoutInflater inflater;
    private Context mContext;

    public SelAdapter(Context context, List<GalleryImage> objects) {
        mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImagesUri = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_sel, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.sel_imageView);
            holder.textView = (TextView) convertView.findViewById(R.id.sel_textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageBitmap(getItem(position).thumnailBitmap);
        holder.textView.setText(Integer.toString(position));

        return convertView;
    }

    @Override
    public int getCount() {
        return mImagesUri.size();
    }

    @Override
    public GalleryImage getItem(int position) {
        return mImagesUri.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addContents(List<GalleryImage> objects) {
        this.mImagesUri.addAll(objects);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

}

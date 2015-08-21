package com.example.y_takasaki.ugomemoforsp;

/**
 * Created by Y-Takasaki on 15/02/21.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SelAdapter extends ArrayAdapter<GalleryImage> {

    public SelAdapter(Context context, List<GalleryImage> objects) {
        super(context, R.layout.item_sel, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_sel, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.sel_imageView);
            holder.textView = (TextView) convertView.findViewById(R.id.sel_textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GalleryImage item = getItem(position);
        if (item.thumnailBitmap != null) {
            holder.imageView.setImageBitmap(item.thumnailBitmap);
        } else {
            Picasso.with(getContext()).load(item.file).into(holder.imageView);
        }

        holder.textView.setText(Integer.toString(position));

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

}

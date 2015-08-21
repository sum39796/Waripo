package com.example.y_takasaki.ugomemoforsp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by Y-Takasaki on 15/06/27.
 */


public class ProjectAdapter extends ArrayAdapter<File> {

    public ProjectAdapter(Context context, List<File> objects) {
        super(context, R.layout.item_project, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 特定の行(position)のデータを得る
        File item = getItem(position);

        ViewHolder viewHolder;
        // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
        if (null == convertView) {
            // CustomDataのデータをViewの各Widgetにセットする
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_project, null);
            viewHolder.iamgeView = (ImageView)convertView.findViewById(R.id.image);
            viewHolder.nameView = (TextView)convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.iamgeView.setImageResource(R.drawable.icon23);
        if(item.isDirectory()) {
            viewHolder.nameView.setText(item.getName() + "/");
        } else {
            viewHolder.nameView.setText(item.getName());
        }
        return convertView;
    }

    public class ViewHolder {
        ImageView iamgeView;
        TextView nameView;
    }
}





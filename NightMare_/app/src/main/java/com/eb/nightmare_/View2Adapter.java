package com.eb.nightmare_;


import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class View2Adapter extends ArrayAdapter<ListViewItem> {
    private int resourceId;

    public View2Adapter(Context context, int textViewResourceId,
                        List<ListViewItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewItem fruit = getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView day = (TextView) view.findViewById(R.id.view_day);
        TextView week = (TextView) view.findViewById(R.id.view_week);
        TextView text = (TextView) view.findViewById(R.id.view_text);
        if (!TextUtils.isEmpty(fruit.getText())) {
            day.setText(fruit.getDay() + "");
            text.setText(fruit.getText());
            week.setText(fruit.week_to_string());
            if (fruit.getWeek() == 1) {
                week.setTextColor(Color.rgb(255, 0, 0));
            }
        }

        return view;
    }
}



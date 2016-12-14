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

public class AdapterMonth extends ArrayAdapter<MonthSelect> {
        private int resourceId;
        public AdapterMonth(Context context, int textViewResourceId,
                            List<MonthSelect> objects) {
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MonthSelect fruit = getItem(position); // 获取当前项的Fruit实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            ImageView off = (ImageView) view.findViewById(R.id.off);
            ImageView on = (ImageView) view.findViewById(R.id.on);
            off.setImageResource(fruit.getOff());
            on.setImageResource(fruit.getOn());

            return view;
        }




}

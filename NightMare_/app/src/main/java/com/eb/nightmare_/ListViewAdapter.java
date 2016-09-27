package com.eb.nightmare_;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends BaseAdapter{

    //声明区***************************************************
    private List<ListViewItem> mList;
    private Context context;
    private LayoutInflater inflater;
    private final int TYPE_COUNT=2;
    private final int FIRST_TYPE=0;
    private final int OTHERS_TYPE=1;
    private int currentType;

    //两个view
    class Holder1 {
        ImageView circle;

        Holder1(View view) {
            circle = (ImageView) view.findViewById(R.id.circle_img_id);
        }

    }
    class Holder2 {
        TextView week;
        TextView day;
        TextView text;

        Holder2(View view) {
            week = (TextView) view.findViewById(R.id.after_week);
            day = (TextView) view.findViewById(R.id.after_day);
            text = (TextView) view.findViewById(R.id.after_text);
        }
    }

    //构造函数
    public ListViewAdapter(Context context, List<ListViewItem> data) {
        this.mList=data;
        this.context=context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        System.out.println("mList.size()" + mList.size());
        return mList.size();
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mList.get(arg0);
    }
    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
     /*   if(mList.get(position).getText()!=null)
        {
            mList.get(position).type=OTHERS_TYPE;
        }*/
        return mList.get(position).type;
    }
    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 2;
    }
    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);

        Holder1 holder1 = null;
        Holder2 holder2 = null;
        System.out.println("getView " + position + " " + convertView
                + " type = " + type);
        if (convertView == null) {
            //选择某一个样式。。
            switch (type) {
                case FIRST_TYPE:
                    convertView = inflater.inflate(R.layout.before_click_listview, null);
                    holder1 = new Holder1(convertView);
                    if(mList.get(position).getWeek()==1) {
                        holder1.circle.setImageResource(R.drawable.add_red_dot_btn);
                    }else{
                        holder1.circle.setImageResource(R.drawable.add_dot_btn);
                    }

                    convertView.setTag(holder1);
                    break;
                case OTHERS_TYPE:
                    convertView = inflater.inflate(R.layout.after_click_listview,null);

                    holder2 = new Holder2(convertView);

                    holder2.week.setText(mList.get(position).week_to_string().substring(0,3));
                    if(mList.get(position).getWeek()==1) {
                        holder2.week.setTextColor(Color.rgb(255, 0, 0));
                    }

                    holder2.day.setText(mList.get(position).getDay()+"");

                    holder2.text.setText(mList.get(position).getText());
//                    holder2.text.setText("hello");

                    convertView.setTag(holder2);

                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case FIRST_TYPE:
                    holder1 = (Holder1) convertView.getTag();
                    if(mList.get(position).getWeek()==1) {
                        holder1.circle.setImageResource(R.drawable.add_red_dot_btn);
                    }else{
                        holder1.circle.setImageResource(R.drawable.add_dot_btn);
                    }
                    break;
                case OTHERS_TYPE:
                    holder2 = (Holder2) convertView.getTag();
                    holder2.week.setText(mList.get(position).week_to_string().substring(0,3));
                    if(mList.get(position).getWeek()==1) {
                        holder2.week.setTextColor(Color.rgb(255, 0, 0));
                    }
                    holder2.day.setText(mList.get(position).getDay()+"");
                    holder2.text.setText(mList.get(position).getText());
                    break;

                default:
                    break;
            }

        }

        return convertView;
    }
}




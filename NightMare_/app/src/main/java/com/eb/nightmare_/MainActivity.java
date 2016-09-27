package com.eb.nightmare_;


import android.content.Intent;


import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.os.Handler;
import android.text.format.DateFormat;


public class MainActivity extends AppCompatActivity {
    ListViewAdapter adapter;
    View2Adapter view_adapter;
    private ImageView nextButton  ;
    List<MonthSelect> month_list = new ArrayList<>();
    ListView listView;
    private final int requestCode = 1500;
    final List<ListViewItem> listItem = new ArrayList<ListViewItem>();
    final  List<ListViewItem> viewItem =new ArrayList<>();
    TextView mTime;
    final Calendar calendar = Calendar.getInstance();
    private static final int msgKey1 = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //声明区***************************************************
        setContentView(R.layout.activity_main);
        final ListView listView=(ListView)findViewById(R.id.list_view);
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);                                         //修改为set
        int month = calendar.get(Calendar.MONTH);                                       //修改为set
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int week = calendar.get(Calendar.DAY_OF_WEEK);

        //listview
        //向list中存储元素
            int aim;
            for(int i =0;i<day;++i){
                if(((i+1)%7-day%7+week)>0) {
                    aim = ((i+1)%7-day%7)+week;
                }
                else{
                    aim = ((i+1)%7-day%7)+week+7;
                }
                    listItem.add(new ListViewItem(year,month,aim,i+1));
                    String inputText = load(listItem.get(i).getId()+"");
                if (!TextUtils.isEmpty(inputText)) {
                    listItem.get(i).setText(inputText);
                    listItem.get(i).setType();
                    Toast.makeText(this, "Restoring succeeded",
                            Toast.LENGTH_SHORT).show();
                }

            }
            //构造适配器
        for(int i=0;i<listItem.size();++i){
            if(listItem.get(i).getText()!=null){
                viewItem.add(listItem.get(i));
            }
        }
        view_adapter = new View2Adapter(this,R.layout.view_other,viewItem);

        adapter = new ListViewAdapter(MainActivity.this, listItem);
            //listView加载适配器
        listView.setAdapter(adapter);
    //    listView.setAdapter(view_adapter);
            //设置点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EditActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("before",(ListViewItem)listItem.get(position));
                intent.putExtras(mBundle);
                startActivityForResult(intent,requestCode);

            }
        });
        //header
        //显示时间Today is week
        TextView header_week = (TextView)findViewById(R.id.head_ShowTheWeeks);
        header_week.setText("Today is "+listItem.get(day-1).week_to_string());
        //        September 27 15:06:17
        TextView header_month = (TextView)findViewById(R.id.month);
        mTime = (TextView) findViewById(R.id.mytime);

        new TimeThread().start();

        header_month.setText(listItem.get(day-1).month_to_string()+" "+day+"  |  ");
        //点击加号
        ImageView plus = (ImageView)findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EditActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("before",(ListViewItem)listItem.get(day-1));
                intent.putExtras(mBundle);
                startActivityForResult(intent,requestCode);
            }
        });
//切换月份
        TextView month_btn = (TextView)findViewById(R.id.month_button);
        String month_select = listItem.get(day-1).month_to_string();
        month_btn.setText("| "+month_select+" | ");
        final LinearLayout main = (LinearLayout)findViewById(R.id.main);
        final LinearLayout view = (LinearLayout)findViewById(R.id.view);
//        final RelativeLayout main = (RelativeLayout) findViewById(R.id.main);
//        final RelativeLayout view = (RelativeLayout)findViewById(R.id.view);
        final ListView monthList = (ListView)findViewById(R.id.month_list);

        initMonth();
        AdapterMonth monthAdapter = new AdapterMonth(this,R.layout.month,month_list);
        monthList.setAdapter(monthAdapter);
        month_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
                monthList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        month_list.get(position).onIsSelect();
                      //  ImageView on = (ImageView)findViewById(R.id.off);
                      //  ImageView off = (ImageView)findViewById(R.id.on);
                      //  on.setVisibility(View.GONE);
                      //  off.setVisibility(View.VISIBLE);
                        main.setVisibility(View.VISIBLE);
                        view.setVisibility(View.GONE);
                     /*   switch (  month_list.get(position).getMonth()){
                            case "January": calendar.set(year,0);break;
                            case "February":calendar.set(year,1);break;
                            case "March":calendar.set(year,2);break;
                            case "April":calendar.set(year,3);break;
                            case "May":calendar.set(year,4);break;
                            case "Jnue":calendar.set(year,5);break;
                            case "July":calendar.set(year,6);break;
                            case "August":calendar.set(year,7);break;
                            case "September":calendar.set(year,8);break;
                            case "October":calendar.set(year,9);break;
                            case "November":calendar.set(year,10);break;
                            default:calendar.set(year,11);
                        }
*/
                    }
                });
            }
        });
        //切换年
        TextView year_btn = (TextView)findViewById(R.id.year_button);
        year_btn.setText(listItem.get(day-1).getYear()+" | ");
        //切换样式

        nextButton = (ImageView)findViewById(R.id.another_view);
        nextButton.setOnClickListener(new View.OnClickListener() {
            int count = 0;
            @Override
            public void onClick(View v) {
                count +=1;
                if(count==2){
                    count =0;
                }
                if(count==0){
                    listView.setAdapter(view_adapter);
                }else if(count==1){
                    listView.setAdapter(adapter);
                }

            }
        });
    }

    private void initMonth() {
        month_list.add(new MonthSelect(R.drawable.month_1_on,R.drawable.month_1_off,"January"));
        month_list.add(new MonthSelect(R.drawable.month_2_on,R.drawable.month_2_off,"February"));
        month_list.add(new MonthSelect(R.drawable.month_3_on,R.drawable.month_3_off,"March"));
        month_list.add(new MonthSelect(R.drawable.month_4_on,R.drawable.month_4_off,"April"));
        month_list.add(new MonthSelect(R.drawable.month_5_on,R.drawable.month_5_off,"May"));
        month_list.add(new MonthSelect(R.drawable.month_6_on,R.drawable.month_6_off,"June"));
        month_list.add(new MonthSelect(R.drawable.month_7_on,R.drawable.month_7_off,"July"));
        month_list.add(new MonthSelect(R.drawable.month_8_on,R.drawable.month_8_off,"August"));
        month_list.add(new MonthSelect(R.drawable.month_9_on,R.drawable.month_9_off,"September"));
        month_list.add(new MonthSelect(R.drawable.month_10_on,R.drawable.month_10_off,"October"));
        month_list.add(new MonthSelect(R.drawable.month_11_on,R.drawable.month_11_off,"November"));
        month_list.add(new MonthSelect(R.drawable.month_12_on,R.drawable.month_12_off,"December"));
    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

   //     setContentView(R.layout.activity_main);
        switch(resultCode){
            case RESULT_OK:{//接收并显示Activity传过来的值
                Bundle bundle = data.getExtras();
                ListViewItem mAfter = (ListViewItem) bundle.getSerializable("AfterClick");
                listItem.get(mAfter.getDay()-1).setText(mAfter.getText());
                listItem.get(mAfter.getDay()-1).setType();
                ListView listView = (ListView)findViewById(R.id.list_view);
                ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, listItem);
                listView.setAdapter(adapter);
                break;
            }
            default:
                break;
        }
    }

    public String load(String name) {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput(name);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
    public class TimeThread extends Thread {
        @Override
        public void run () {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                }
                                 catch (InterruptedException e) {
                                     e.printStackTrace();
                                 }
            } while(true);
        }
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    long sysTime = System.currentTimeMillis();
                    CharSequence sysTimeStr = DateFormat.format("HH:mm:ss", sysTime);

                    mTime.setText(sysTimeStr);
                    break;

                default:
                    break;
            }
        }
    };
}


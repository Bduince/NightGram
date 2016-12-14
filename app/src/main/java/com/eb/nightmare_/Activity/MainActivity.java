package com.eb.nightmare_.Activity;


import android.content.Intent;


import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Handler;

import com.eb.nightmare_.AdapterMonth;
import com.eb.nightmare_.DB.DbOprate;
import com.eb.nightmare_.ListViewAdapter;
import com.eb.nightmare_.ListViewItem;
import com.eb.nightmare_.MonthSelect;
import com.eb.nightmare_.R;
import com.eb.nightmare_.View2Adapter;


public class MainActivity extends AppCompatActivity {
    private DbOprate dbOprate;
    private List<ListViewItem> allItem = new ArrayList<ListViewItem>();
    private ListViewAdapter adapter;
    private String month_select;
    private View2Adapter view_adapter;
    private TextView month_btn;
    private TextView year_btn;
    private ImageView nextButton;
    private List<MonthSelect> month_list = new ArrayList<>();
    private ListView listView;
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
        final int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        dbOprate = new DbOprate(this);
        allItem = dbOprate.getAllDate();
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
                if(aim>7) aim-=7;
                   ListViewItem item = new ListViewItem(year,month,aim,i+1);
                    listItem.add(item);
                    if(dbOprate.search(item.getId())!=null){
                        item.setText(dbOprate.search(item.getId()).getText());
                    }
                    else{
                        dbOprate.insertDate(item);
                    }
                    String inputText = load(listItem.get(i).getId()+"");
                if (!TextUtils.isEmpty(inputText)) {
                    listItem.get(i).setText(inputText);
                    listItem.get(i).setType();
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
        month_btn = (TextView)findViewById(R.id.month_button);
        month_select = listItem.get(day-1).month_to_string();
        month_btn.setText("| "+month_select+" | ");
        final LinearLayout main = (LinearLayout)findViewById(R.id.main);
        final LinearLayout view = (LinearLayout)findViewById(R.id.view);
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
                        switch (  month_list.get(position).getMonth()){
                            case "January":
                                calendar.set(Calendar.MONTH,0);
                                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                month_btn.setText("| "+"January "+" | ");
                                break;
                            case "February":
                                calendar.set(Calendar.MONTH,1);
                                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                month_btn.setText("| "+"February"+" | ");
                                break;
                            case "March":
                                calendar.set(Calendar.MONTH,2);
                                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                month_btn.setText("| "+"March"   +" | ");
                                break;
                            case "April":
                                calendar.set(Calendar.MONTH,3);
                                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                month_btn.setText("| "+"April   "+" | ");
                                break;
                            case "May":
                                calendar.set(Calendar.MONTH,4);
                                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                month_btn.setText("| "+"May     "+" | ");break;
                            case "Jnue":
                                calendar.set(Calendar.MONTH,5);
                                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                month_btn.setText("| "+"Jnue    "+" | ");break;
                            case "July":
                                calendar.set(Calendar.MONTH,6);
                                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                month_btn.setText("| "+"July    "+" | ");break;
                            case "August":
                                calendar.set(Calendar.MONTH,7);
                                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                month_btn.setText("| "+"August   "+" | ");break;
                            case "September":
                                calendar.set(Calendar.MONTH,8);
                                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                month_btn.setText("| "+"September"+" | ");break;
                            case "October":
                                calendar.set(Calendar.MONTH,9);
                                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                month_btn.setText("| "+"October  "+" | ");break;
                            case "November":
                                calendar.set(Calendar.MONTH,10);
                                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                month_btn.setText("| "+"November "+" | ");break;
                            default:
                                calendar.set(Calendar.MONTH,11);
                                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                month_btn.setText("| "+"December "+" | ");break;
                        }
                        main.setVisibility(View.VISIBLE);
                        view.setVisibility(View.GONE);
                        List<ListViewItem> change_month_list = new ArrayList<ListViewItem>();
                        initList(calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.DAY_OF_WEEK),
                                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),change_month_list);

                        listItem.clear();
                        listItem.addAll(change_month_list);
                        change_month_list.clear();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        //切换年
        year_btn = (TextView)findViewById(R.id.year_button);
        year_btn.setText(listItem.get(day-1).getYear()+" | ");
        final LinearLayout view_year = (LinearLayout)findViewById(R.id.view_year);
        year_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.setVisibility(View.GONE);
                view_year.setVisibility(View.VISIBLE);
                TextView year13 = (TextView)findViewById(R.id.year13);
                TextView year14 = (TextView)findViewById(R.id.year14);
                TextView year15 = (TextView)findViewById(R.id.year15);
                TextView year16 = (TextView)findViewById(R.id.year16);
                year13.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendar.set(Calendar.YEAR,2013);
                        calendar.set(Calendar.DAY_OF_MONTH,calendar.getMaximum(Calendar.DAY_OF_MONTH));
                        year_btn.setText("2013"+" | ");
                        main.setVisibility(View.VISIBLE);
                        view_year.setVisibility(View.GONE);
                        List<ListViewItem> change_month_list = new ArrayList<ListViewItem>();
                        initList(calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.DAY_OF_WEEK),
                                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),change_month_list);

                        listItem.clear();
                        listItem.addAll(change_month_list);
                        change_month_list.clear();
                        adapter.notifyDataSetChanged();
                    }
                });
                year14.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendar.set(Calendar.YEAR,2014);
                        calendar.set(Calendar.DAY_OF_MONTH,calendar.getMaximum(Calendar.DAY_OF_MONTH));
                        year_btn.setText("2014"+" | ");
                        main.setVisibility(View.VISIBLE);
                        view_year.setVisibility(View.GONE);
                        List<ListViewItem> change_month_list = new ArrayList<ListViewItem>();
                        initList(calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.DAY_OF_WEEK),
                                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),change_month_list);

                        listItem.clear();
                        listItem.addAll(change_month_list);
                        change_month_list.clear();
                        adapter.notifyDataSetChanged();
                    }
                });
                year15.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendar.set(Calendar.YEAR,2015);
                        calendar.set(Calendar.DAY_OF_MONTH,calendar.getMaximum(Calendar.DAY_OF_MONTH));
                        year_btn.setText("2015"+" | ");
                        main.setVisibility(View.VISIBLE);
                        view_year.setVisibility(View.GONE);
                        List<ListViewItem> change_month_list = new ArrayList<ListViewItem>();
                        initList(calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.DAY_OF_WEEK),
                                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),change_month_list);

                        listItem.clear();
                        listItem.addAll(change_month_list);
                        change_month_list.clear();
                        adapter.notifyDataSetChanged();
                    }
                });
                year16.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendar.set(Calendar.YEAR,2016);
                        calendar.set(Calendar.DAY_OF_MONTH,calendar.getMaximum(Calendar.DAY_OF_MONTH));
                        year_btn.setText("2016"+" | ");
                        main.setVisibility(View.VISIBLE);
                        view_year.setVisibility(View.GONE);
                        List<ListViewItem> change_month_list = new ArrayList<ListViewItem>();
                        initList(calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.DAY_OF_WEEK),
                                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),change_month_list);

                        listItem.clear();
                        listItem.addAll(change_month_list);
                        change_month_list.clear();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
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
    public void initList(int day,int week,int year,int month,List<ListViewItem> listItem){
        int aim;
        for(int i =0;i<day;++i){
            if(((i+1)%7-day%7+week)>0) {
                aim = ((i+1)%7-day%7)+week;
            }
            else{
                aim = ((i+1)%7-day%7)+week+7;
            }
            if(aim>7) aim-=7;
            listItem.add(new ListViewItem(year,month,aim,i+1));
            String inputText = load(listItem.get(i).getId()+"");
            if (!TextUtils.isEmpty(inputText)) {
                listItem.get(i).setText(inputText);
                listItem.get(i).setType();
                Toast.makeText(this, "Restoring succeeded",
                        Toast.LENGTH_SHORT).show();
            }

        }
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
    private void refresh() {
        finish();
        onCreate(null);
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


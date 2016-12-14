package com.eb.nightmare_.Activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eb.nightmare_.DB.DbOprate;
import com.eb.nightmare_.ListViewItem;
import com.eb.nightmare_.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class EditActivity extends AppCompatActivity {
    private DbOprate dbOperate;
    private EditText edit;
    Calendar calendar = Calendar.getInstance();
    private View root;
    ListViewItem mPerson;
  //  final ListViewItem sod = new AfterClickListView();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        dbOperate = new DbOprate(this);
        //取得信息

        Intent i =getIntent();
        final ListViewItem mlistItem =(ListViewItem) i.getSerializableExtra("before");
        mPerson = mlistItem;
        int day = mPerson.getDay();
        int week = mPerson.getWeek();
        int month=mPerson.getMonth();
        int year= mPerson.getYear();
        calendar.set(year,month,day);
        edit = (EditText) findViewById(R.id.edit);

        TextView textView = (TextView) findViewById(R.id.show_today_date);
       //标题时间
        String str = (new SimpleDateFormat("/ dd / yyyy")).format(calendar.getTime());
        String mon = new String();

        String date = new String();

        textView.setText(mPerson.week_to_string()+" / "+mPerson.month_to_string()+str);

        String inputText = mPerson.getText();
        if (!TextUtils.isEmpty(inputText)) {
            edit.setText(inputText);

            mPerson.setText(inputText);

            edit.setSelection(inputText.length());
            Toast.makeText(this, "Restoring succeeded",
                    Toast.LENGTH_SHORT).show();
        }

        final ImageButton ibtn_done = (ImageButton) findViewById(R.id.done);
        final ImageView insert_time = (ImageView)findViewById(R.id.insert_time);
        insert_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = (new SimpleDateFormat(" HH:mm ")).format(calendar.getTime());
                getEditTextCursorIndex(edit);
                insertText(edit,str);
            }
        });
        ibtn_done.setVisibility(View.GONE);
        insert_time.setVisibility(View.GONE);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtn_done.setVisibility(View.VISIBLE);
                insert_time.setVisibility(View.VISIBLE);
            }
        });
        ibtn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = edit.getText().toString();
                mPerson.setText(inputText);
                dbOperate.updateOrder(mPerson);
       //         save(inputText,mPerson.getId()+"");
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("AfterClick",mPerson);
                intent.putExtras(mBundle);
                EditActivity.this.setResult(RESULT_OK, intent);
                //关闭当前activity
                EditActivity.this.finish();
              //  startActivity(intent);
            }
        });
    }

    private int getEditTextCursorIndex(EditText edit) {
        return edit.getSelectionStart();
    }
    private void insertText(EditText mEditText, String mText){
        mEditText.getText().insert(getEditTextCursorIndex(mEditText), mText);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        String inputText = edit.getText().toString();
        mPerson.setText(inputText);
        dbOperate.updateOrder(mPerson);
      //  save(inputText,mPerson.getId()+"");

    }

    public void weekTo(String date,int week){
        switch (week){
            case 1: date = "SUNDAY";break;
            case 2: date = "MONDAY";break;
            case 3:date = "TUESDAY";break;
            case 4:date=  "WEDNESDAY";break;
            case 5:date = "THURSDAY";break;
            case 6:date = "FRIDAY";break;
            default: date = "SATURDAY";break;

        }
    }

    public void monthTo(String date,int month){
        switch (month){
            case 0: date = "January";break;
            case 1: date = "February";break;
            case 2:date = "March";break;
            case 3:date=  "April";break;
            case 4:date = "May";break;
            case 5:date = "June";break;
            case 6:date = "July";break;
            case 7: date = "August";break;
            case 8: date = "September";break;
            case 9: date = "October";break;
            case 10: date = "November";break;
            default: date = "December";break;

        }
    }

    public void save(String inputText,String id) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput(id, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
            mPerson.setText(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String load(String id) {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput(id);
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
}



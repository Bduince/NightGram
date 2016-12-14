package com.eb.nightmare_.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.eb.nightmare_.ListViewItem;

import java.util.ArrayList;
import java.util.List;


public class DbOprate {
    private final String[] COLUMNS = new String[] {"Id", "Day","Month","Year","Week","Text"};
    private Context context;
    private Helper dbHelper;

    public DbOprate(Context context){
        this.context = context;
        dbHelper = new Helper(context);
    }
    /**
     * 判断表中是否有数据
     */
    public boolean isDataExist(){
        int count = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            db = dbHelper.getReadableDatabase();
            cursor = db.query(Helper.TABLE_NAME, new String[]{"COUNT(Id)"}, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0){
                return true;
            }
        }  catch (Exception e) {
            Log.e("DataBase", "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return  false;
    }

    /**
     * 查询数据库中所有数据
     */
    public List<ListViewItem> getAllDate(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            // select * from Orders
            cursor = db.query(Helper.TABLE_NAME, COLUMNS, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                List<ListViewItem> orderList = new ArrayList<ListViewItem>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseListViewItem(cursor));
                }
                return orderList;
            }
        }
        catch (Exception e) {
            Log.e("Database", "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }
    /**
     * 将查找到的数据转换成Item类
     */
    private ListViewItem parseListViewItem(Cursor cursor){
        ListViewItem item = new ListViewItem();
        item.setDay((cursor.getInt(cursor.getColumnIndex("Day"))));
        item.setMonth((cursor.getInt(cursor.getColumnIndex("Month"))));
        item.setYear((cursor.getInt(cursor.getColumnIndex("Year"))));
        item.setWeek((cursor.getInt(cursor.getColumnIndex("Week"))));
        item.setText((cursor.getString(cursor.getColumnIndex("Text"))));
        item.setType();
        item.setId();
        return item;
    }
    /**
     * 新增一条数据
     */
    public boolean insertDate(ListViewItem item){
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("Id", item.getId());
            contentValues.put("Day", item.getDay());
            contentValues.put("Month", item.getMonth());
            contentValues.put("Year", item.getYear());
            contentValues.put("Week", item.getWeek());
            contentValues.put("Text", item.getText());
            db.insertOrThrow(Helper.TABLE_NAME, null, contentValues);

            db.setTransactionSuccessful();
            return true;
        }catch (SQLiteConstraintException e){
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e("Database", "", e);
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }
    public boolean insertDate(int id){
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("Id", id);
            contentValues.put("Day", "");
            contentValues.put("Month", "");
            contentValues.put("Year", "");
            contentValues.put("Week", "");
            contentValues.put("Text", "");
            db.insertOrThrow(Helper.TABLE_NAME, null, contentValues);

            db.setTransactionSuccessful();
            return true;
        }catch (SQLiteConstraintException e){
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e("Database", "", e);
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }
    /**
     * 删除一条数据  \
     */
    public boolean deleteOrder(int id) {
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            db.delete(Helper.TABLE_NAME, "Id = ?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e("Database", "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }
    /**
     * 修改一条数据
     */
    public boolean updateOrder(ListViewItem item){
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            db.beginTransaction();

            ContentValues cv = new ContentValues();
            cv.put("Text", item.getText());
            db.update(Helper.TABLE_NAME,
                    cv,
                    "Id = ?",
                    new String[]{String.valueOf(item.getId())});
            db.setTransactionSuccessful();
            return true;
        }
        catch (Exception e) {
            Log.e("Database", "", e);
        }
        finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return false;
    }
    /**
     * 数据查询
     */
    public ListViewItem search(int id){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query(Helper.TABLE_NAME,
                    COLUMNS,
                    "Id = ?",
                    new String[] {""+id},
                    null, null, null);

            if (cursor.getCount() > 0) {
                return parseListViewItem(cursor);
            }
        }
        catch (Exception e) {
            Log.e("Database", "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }
}

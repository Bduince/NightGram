package com.eb.nightmare_;

import java.io.Serializable;


public class ListViewItem implements Serializable {

    private static final long serialVersionUID = 1L;

    public int type;

    private int id;
    private int week;
    private int month;
    private int day;
    private  int year;
    private String text;
    public ListViewItem() {}
    public ListViewItem(int year, int month, int week, int day) {
        setYear(year);
        setMonth(month);
        setWeek(week);
        setDay(day);
        setId();
    }

    public void setType(){
        if(text==null){
            this.type=0;
        }else{
            type=1;
        }
    }


    public void setYear(int year){this.year=year;}
    public void setMonth(int month) {this.month = month;}
    public void setText(String text) {
        this.text= text;
    }
    public void setId(){
        this.id = day+month*100+year*10000;
    }
    public void setDay(int day){
        this.day = day;
    }
    public void setWeek(int week){this.week=week;}



    public int getDay() {return day;}
    public int getWeek() {return week;}
    public int getId() {return id;}
    public int getMonth(){return month;}
    public int getYear(){return year;}
    public String getText(){return text;}

    public  String week_to_string(){
        switch (this.week){
            case 1: return "Sunday";
            case 2: return "Monday";
            case 3: return "Tuesday";
            case 4: return "Wednesday";
            case 5: return "Thursday";
            case 6: return "Friday";
            default: return "Saturday";
        }
    }

    public String month_to_string(){
        switch (this.month) {
            case 0: return "January";
            case 1: return "February";
            case 2: return "March";
            case 3: return "April";
            case 4: return "May";
            case 5: return "June";
            case 6: return "July";
            case 7: return "August";
            case 8: return "September";
            case 9: return "October";
            case 10: return "November";
            default: return "December";
        }
    }
}

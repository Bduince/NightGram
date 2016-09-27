package com.eb.nightmare_;


public class MonthSelect {
    private String month;
    private int imgIDon;
    private int imgIDoff;
    private boolean isSelect;
    public MonthSelect(int imgIDon,int imgIDoff, String month){
        this.isSelect=false;
        this.imgIDoff=imgIDoff;
        this.imgIDon=imgIDon;
        this.month=month;
    }
    public int getOn(){return this.imgIDon;}
    public int getOff(){return this.imgIDoff;}
    public String getMonth(){return this.month;}
    public void onIsSelect(){
        this.isSelect=true;
    }
    public void offIsSelect(){
        this.isSelect=false;
    }
}

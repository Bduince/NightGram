package com.eb.nightmare_;


public class MonthSelect {
    private String month;
    private int imgID;
    private boolean isSelect;
    public MonthSelect(int imgID, String month){
        this.isSelect=false;
        this.imgID=imgID;
        this.month=month;
    }
    public void onIsSelect(){
        this.isSelect=true;
    }
    public void offIsSelect(){
        this.isSelect=false;
    }
}

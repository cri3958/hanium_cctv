package com.hanium.cctv.model;

public class cctv {

    private String number, pw, name, place, special;
    private int id;

    public cctv() {}

    public cctv(String number, String pw, String name, String place, String special){
        this.number = number;
        this.pw = pw;
        this.name = name;
        this.place = place;
        this.special = special;
    }

    public int getId(){return id;}

    public void setId(int id) {this.id = id;}

    public String getNumber() {return number;}

    public void setNumber(String number) {this.number = number;}

    public String getPw() {return pw;}

    public void setPw(String pw) {this.pw = pw;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getPlace() {return place;}

    public void setPlace(String place) {this.place = place;}

    public String getSpecial() {return special;}

    public void setSpecial(String special) {this.special = special;}
}

package com.hanium.cctv.model;

public class cctv {

    private String id, pw, name, place, special;

    public cctv() {}

    public cctv(String id, String pw, String name, String place, String special){
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.place = place;
        this.special = special;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getPw() {return pw;}

    public void setPw(String pw) {this.pw = pw;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getPlace() {return place;}

    public void setPlace(String place) {this.place = place;}

    public String getSpecial() {return special;}

    public void setSpecial(String special) {this.special = special;}
}

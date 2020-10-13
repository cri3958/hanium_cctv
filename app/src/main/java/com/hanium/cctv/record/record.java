package com.hanium.cctv.record;

public class record {

    private String date, object_num, mem_name, reason;
    private int id;

    public record() { }

    public record(String date, String object_num, String mem_name, String reason) {
        this.date = date;
        this.object_num = object_num;
        this.mem_name = mem_name;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getObject_num() {
        return object_num;
    }

    public void setObject_num(String object_num) {
        this.object_num = object_num;
    }

    public String getMem_name() {
        return mem_name;
    }

    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

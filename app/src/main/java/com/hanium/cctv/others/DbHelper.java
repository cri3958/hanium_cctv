package com.hanium.cctv.others;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hanium.cctv.cctv.cctv;
import com.hanium.cctv.record.record;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "cctvlist.db";
    private static final String CCTVLIST = "cctvlist";
    private static final String RECORDLIST = "recordlist";

    private static final String CCTV_ID = "id";
    private static final String CCTV_NUMBER = "number";         //cctvid
    private static final String CCTV_PW = "pw";                 //cctvpw
    private static final String CCTV_NAME = "name";             //cctv의 대상의 이름
    private static final String CCTV_PLACE = "place";           //cctv의 장소
    private static final String CCTV_SPECIAL = "special";       //cctv의 대상의 특이사항

    private static final String RECORD_ID = "id";
    private static final String RECORD_date = "date";                       //응급상황 날짜
    private static final String RECORD_object_num = "object_num";           //cctv의 번호
    private static final String RECORD_mem_name = "mem_name";               //cctv의 대상의 이름
    private static final String RECORD_reason = "reason";                   //응급상황 발생 이유

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_CCTVLIST_TABLE = "CREATE TABLE " + CCTVLIST + "("
                + CCTV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CCTV_NUMBER + " TEXT,"
                + CCTV_PW + " TEXT,"
                + CCTV_NAME + " TEXT,"
                + CCTV_PLACE + " TEXT,"
                + CCTV_SPECIAL + " TEXT" + ")";

        String CREATE_RECORDLIST_TABLE = "CREATE TABLE " + RECORDLIST + "("
                + RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RECORD_date + " TEXT,"
                + RECORD_object_num + " TEXT,"
                + RECORD_mem_name + " TEXT,"
                + RECORD_reason + " INTEGER" + ")";
        db.execSQL(CREATE_CCTVLIST_TABLE);
        db.execSQL(CREATE_RECORDLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("DROP TABLE IF EXISTS " + CCTVLIST);
            case 2:
                db.execSQL("DROP TABLE IF EXISTS " + RECORDLIST);
                break;
        }
        onCreate(db);
    }

    public void insertCCTVLIST(cctv cctv){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CCTV_NUMBER, cctv.getNumber());
        contentValues.put(CCTV_PW, cctv.getPw());
        contentValues.put(CCTV_NAME, cctv.getName());
        contentValues.put(CCTV_PLACE, cctv.getPlace());
        contentValues.put(CCTV_SPECIAL, cctv.getSpecial());
        db.insert(CCTVLIST, null, contentValues);
        db.close();
    }

    public void deleteCCTVLISTById(cctv cctv) { //sql지우기
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CCTVLIST, CCTV_ID + " = ? ", new String[]{String.valueOf(cctv.getId())});
        db.close();
    }

    public void updateCCTVLIST(cctv cctv) { //지운후에 업데이트하기
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CCTV_NUMBER, cctv.getNumber());
        contentValues.put(CCTV_PW, cctv.getPw());
        contentValues.put(CCTV_NAME, cctv.getName());
        contentValues.put(CCTV_PLACE, cctv.getPlace());
        contentValues.put(CCTV_SPECIAL, cctv.getSpecial());

        db.update(CCTVLIST, contentValues, CCTV_ID + " = " + cctv.getId(), null);
        db.close();
    }
    public ArrayList<cctv> getCCTVLIST(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<cctv> cctvlist = new ArrayList<>();
        cctv cctv;
        Cursor cursor = db.rawQuery("SELECT * FROM " + CCTVLIST + " ORDER BY " + CCTV_ID, null);
        while (cursor.moveToNext()){
            cctv = new cctv();
            cctv.setId(cursor.getInt(cursor.getColumnIndex(CCTV_ID)));
            cctv.setNumber(cursor.getString(cursor.getColumnIndex(CCTV_NUMBER)));
            cctv.setPw(cursor.getString(cursor.getColumnIndex(CCTV_PW)));
            cctv.setName(cursor.getString(cursor.getColumnIndex(CCTV_NAME)));
            cctv.setPlace(cursor.getString(cursor.getColumnIndex(CCTV_PLACE)));
            cctv.setSpecial(cursor.getString(cursor.getColumnIndex(CCTV_SPECIAL)));
            cctvlist.add(cctv);
        }
        cursor.close();
        db.close();
        return cctvlist;
    }

    public String[] getCCTV_info(String object_num) {
        String[] info_cctv = new String[5];
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CCTVLIST + " WHERE number='" + object_num + "'", null);
        while (cursor.moveToNext()) {
            info_cctv[0] = cursor.getString(cursor.getColumnIndex(CCTV_NUMBER));
            info_cctv[1] = cursor.getString(cursor.getColumnIndex(CCTV_PW));
            info_cctv[2] = cursor.getString(cursor.getColumnIndex(CCTV_NAME));
            info_cctv[3] = cursor.getString(cursor.getColumnIndex(CCTV_PLACE));
            info_cctv[4] = cursor.getString(cursor.getColumnIndex(CCTV_SPECIAL));
        }
        cursor.close();
        db.close();
        return info_cctv;

    }

    public Boolean checkCCTVLIST(String num) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CCTVLIST + " WHERE number='" + num + "'", null);
        if (cursor.getCount() == 0)
            return false;
        cursor.close();
        db.close();
        return true;
    }

    public void insertRECORDLIST(String date, String object_num, String mem_name, String reason) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RECORD_date, date);
        contentValues.put(RECORD_object_num, object_num);
        contentValues.put(RECORD_mem_name, mem_name);
        contentValues.put(RECORD_reason, reason);
        db.insert(RECORDLIST, null, contentValues);
        db.close();
    }

    public void deleteRECORDLISTById(record record) { //sql지우기
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(RECORDLIST, RECORD_ID + " = ? ", new String[]{String.valueOf(record.getId())});
        db.close();
    }

    public void updateRECORDLIST(record record) { //지운후에 업데이트하기
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(RECORD_date, record.getDate());
        contentValues.put(RECORD_object_num, record.getObject_num());
        contentValues.put(RECORD_mem_name, record.getMem_name());
        contentValues.put(RECORD_reason, record.getReason());

        db.update(RECORDLIST, contentValues, RECORD_ID + " = " + record.getId(), null);
        db.close();
    }

    public ArrayList<record> getRECORDLIST() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<record> recordlist = new ArrayList<>();
        record record;
        Cursor cursor = db.rawQuery("SELECT * FROM " + RECORDLIST + " ORDER BY " + RECORD_ID, null);
        while (cursor.moveToNext()) {
            record = new record();
            record.setId(cursor.getInt(cursor.getColumnIndex(RECORD_ID)));
            record.setDate(cursor.getString(cursor.getColumnIndex(RECORD_date)));
            record.setObject_num(cursor.getString(cursor.getColumnIndex(RECORD_object_num)));
            record.setMem_name(cursor.getString(cursor.getColumnIndex(RECORD_mem_name)));
            record.setReason(cursor.getString(cursor.getColumnIndex(RECORD_reason)));
            recordlist.add(record);
        }
        cursor.close();
        db.close();
        return recordlist;
    }
}

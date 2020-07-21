package com.hanium.cctv.cctv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "cctvlist.db";
    private static final String CCTVLIST = "cctvlist";

    private static final String CCTV_ID = "id";
    private static final String CCTV_NUMBER = "number";
    private static final String CCTV_PW = "pw";
    private static final String CCTV_NAME = "name";
    private static final String CCTV_PLACE = "place";
    private static final String CCTV_SPECIAL = "special";

    public DbHelper(Context context){
        super(context , DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_CCTVLIST_TABLE = "CREATE TABLE " + CCTVLIST + "("
                + CCTV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CCTV_NUMBER + " TEXT,"
                + CCTV_PW + " TEXT,"
                + CCTV_NAME + " TEXT,"
                + CCTV_PLACE + " INTEGER,"
                + CCTV_SPECIAL + " INTEGER" + ")";

        db.execSQL(CREATE_CCTVLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("DROP TABLE IF EXISTS " + CCTVLIST);
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
        return  cctvlist;
    }
}

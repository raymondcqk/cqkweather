package com.raymondqck.www.rayweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/5/21 0021.
 */
public class MyDBOpenHelper extends SQLiteOpenHelper {

    /**
     * Province表建表语句
     */
    public static final String CREATE_PROVINCE_TABLE = "create table Province ("
            + "id integer primary key autoincrement,"
            + "province_name text,"
            + "province_code text"
            + ")";

    /**
     * City表建表语句
     */
    public static final String CREATE_CITY_TABLE = "create table City ("
            + "id integer primary key autoincrement,"
            + "city_name text,"
            + "city_code text,"
            + "province_id integer"
            + ")";
    /**
     * County表建表语句
     */
    public static final String CREATE_COUNTY_TABLE = "create table County ("
            + "id integer primary key autoincrement,"
            + "county_name text,"
            + "county_code text,"
            + "city_id integer"
            + ")";


    public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表
        db.execSQL(CREATE_PROVINCE_TABLE);
        db.execSQL(CREATE_CITY_TABLE);
        db.execSQL(CREATE_COUNTY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

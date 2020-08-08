package com.apps.avichay.myeasyapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    private static final String DB_NAME = "MyDBHelper1.db";
    public static final String TABLE_NAME = "MyDBHelper";
    public static final String LOCATION = "location";
    public static final String ADDRESS = "address";
    public static final String URI = "uri";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String LENGTH = "length";


    MyDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table " + TABLE_NAME + " ( _id integer primary key,search text not null,"+LOCATION+" text not null,"+ADDRESS+" text not null,"+URI+" text not null,"+LAT+" real not null,"+LON+" real not null, "+LENGTH+" text not null)";
        Log.d(TAG, "onCreate: sql=" + sql);
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public void addMyLocations(String search, String address, String location, String uri, double lat, double lon,String length) {
        ContentValues values = new ContentValues();
        values.put("search", search);
        values.put("address", address);
        values.put("uri", uri);
        values.put("location",location);
        values.put("lat",lat);
        values.put("lon",lon);
        values.put("length",length);

        SQLiteDatabase db = this.getWritableDatabase();
        long l=db.insert(TABLE_NAME, "false", values);
        Log.e("addMyLocations","l="+l);
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        Log.e("MyDBHelper","cursor="+res.getCount());
        return res;
    }

    public Cursor getDataSpecific(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res2 = db.rawQuery("SELECT * FROM MyDBHelper "+"WHERE _id=?",new String[]{Long.toString(id)});
        return res2;
    }

    public long removeAllResults() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("MyDBHelper", null, null);
    }

}
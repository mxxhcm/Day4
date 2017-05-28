package com.example.mxx.tools;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;


import com.example.mxx.database.MyDatabaseHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mxx on 2017/3/18.
 */
public class CRUD {
    private static MyDatabaseHelper dbHelper;
    private static final int SUCCESS = 1;
    private static final int FAILURE = 0;

    public CRUD(Context context)
    {
        dbHelper = new MyDatabaseHelper(context, "DAY", null, 1);
    }


    //增加新的事件
    public static int insert(Info info)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        synchronized (CRUD.class) {
            int id = 0;
            if (info.getId() == 0) {
                //这里通过
                Cursor cursor = db.rawQuery("select count(*) from " + Info.TABLE, null);
                cursor.moveToFirst();
                id = cursor.getInt(0);
                id++;
                info.setId(id);
            }
        }
        ContentValues values = new ContentValues();
        values.put(Info.TABLE_ID,info.getId());
        values.put(Info.TABLE_THEME,info.getTheme());
        values.put(Info.TABLE_DATE,info.getDate());
        values.put(Info.TABLE_CONTEXT,info.getContext());
        values.put(Info.TABLE_PICTURE,info.getPicture());

        synchronized (CRUD.class) {
            db.insert(Info.TABLE, null, values);
        }
        db.close();

        return SUCCESS;
    }


    //根据用户事件的id来删除事件
    public static void delete(int info_id)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Info.TABLE,Info.TABLE_ID+"=?",new String[]{String.valueOf(info_id)});
        db.close();
    }

    //更新用户的时间
    public static void update(Info info)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Info.TABLE_THEME,info.getTheme());
        values.put(Info.TABLE_DATE,info.getDate());
        values.put(Info.TABLE_CONTEXT,info.getContext());
        values.put(Info.TABLE_PICTURE,info.getPicture());
        db.update(Info.TABLE,values,Info.TABLE_ID+"=?",new String[]{String.valueOf(info.getId())});
        db.close();
    }

    //根据id获得相应的记录
    public static Info getInfoByID(int id)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Info info= new Info();
        String selectString = "select * "
                + "from "
                + Info.TABLE +
                " where id = " + id +";";
        Cursor cursor = db.rawQuery(selectString,null);
        if(cursor.moveToFirst())
        {
            do
            {
                info.setId(id);
                info.setDate(cursor.getString(cursor.getColumnIndex(Info.TABLE_ID)));
                info.setPicture(cursor.getBlob(cursor.getColumnIndex(Info.TABLE_PICTURE)));
                info.setTheme(cursor.getString(cursor.getColumnIndex(Info.TABLE_THEME)));
                info.setContext(cursor.getString(cursor.getColumnIndex(Info.TABLE_CONTEXT)));

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return info;
    }

    //返回要展示的当前用户的所有记录
    public static ArrayList<Info> getInfoList()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<Info> infoList = new ArrayList<Info>();
        String selectstring = "select * "
                + "from "
                + Info.TABLE + ";";

        Cursor cursor = db.rawQuery(selectstring,null);
        if(cursor.moveToFirst())
        {
            do {
                Info info = new Info();
                info.setId(cursor.getInt(cursor.getColumnIndex(Info.TABLE_ID)));
                info.setTheme(cursor.getString(cursor.getColumnIndex(Info.TABLE_THEME)));
                info.setDate(cursor.getString(cursor.getColumnIndex(Info.TABLE_DATE)));
                info.setContext(cursor.getString(cursor.getColumnIndex(Info.TABLE_CONTEXT)));
                info.setPicture(cursor.getBlob(cursor.getColumnIndex(Info.TABLE_PICTURE)));
                infoList.add(info);

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return infoList;
    }



    public static int dropTable(String tableName)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = new String(" DROP TABLE " + tableName +";");
        db.execSQL(sql);
        db.close();
        return 1;
    }
    public static int dropDatabase()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = new String(" DROP DATABASE DAY;");
        db.execSQL(sql);
        db.close();
        return 1;
    }

    public static int clearTableData(String tableName)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = new String("Delete from " + tableName + ";");
        db.execSQL(sql);
        db.close();
        return 1;
    }
    public static int createTable(String tableName)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //创建表的SQL语句
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+ Info.TABLE +" ("
                + Info.TABLE_ID +" INTEGER PRIMARY KEY ,"
                + Info.TABLE_THEME + " TEXT,"
                + Info.TABLE_DATE + " TEXT,"
                + Info.TABLE_CONTEXT + " TEXT,"
                + Info.TABLE_PICTURE + " BLOB"
                + ");";

        //创建表
        db.execSQL(CREATE_TABLE);

        db.close();

        return 1;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);

        return baos.toByteArray();

    }

    //将一个文件读到byte数组之中
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // 获取文件大小
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // 文件太大，无法读取
            throw new IOException("File is to large "+file.getName());
        }

        // 创建一个数据来保存文件数据
        byte[] bytes = new byte[(int)length];

        // 读取数据到byte数组中
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // 确保所有数据均被读取
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
}

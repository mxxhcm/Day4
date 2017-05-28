package com.example.mxx.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.mxx.tools.Info;

/**
 * Created by mxx on 2017/3/17.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper{

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory
                            factory , int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    //这个用来第一次进行创建表
    @Override
    public void onCreate(SQLiteDatabase db)
    {

        //创建成功的话会有提示
        Toast.makeText(mContext,"Create succeed!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {

    }
}

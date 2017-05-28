package com.example.mxx.tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mxx on 2017/3/18.
 */

public class Info {

    //创建的表名,按理来说这个应该是可以改变的。
    public static String TABLE="aa2233";

    //创建表的字段名,全为常量
    public static final String TABLE_ID = "id";
    public static final String TABLE_THEME = "theme";
    public static final String TABLE_CONTEXT = "context";
    public static final String TABLE_DATE = "date";
    public static final String TABLE_PICTURE = "picture";

    //这个用来存每个字段的值
    private int id = 0;
    private String theme;
    private String context;


    private String date;
    private byte[] picture;

    public Info(String table_name)
    {
        TABLE = table_name;
    }

    public Info()
    {
        id = 0;
    }

    public Info(int _id,String _theme,String _date,String _context,byte[] _picture)
    {
        id = _id;
        theme = _theme;
        date = _date;
        context = _context;
        picture = _picture;
    }

    public static void setTableName(String tablename)
    {
        TABLE = tablename;
    }

    public void setId(int _id)
    {
        id = _id;
    }

    public int getId()
    {
        return id;
    }

    public void setTheme(String _theme)
    {
        theme = _theme;
    }

    public String getTheme()
    {
        return theme;
    }

    public void setContext(String _context)
    {
        context = _context;
    }

    public String getContext()
    {
        return context;
    }

    public void setDate(String _date)
    {
        date = _date;
    }
    public String getDate()
    {
        return date;
    }

    public void setPicture(byte[] _picture)
    {
        picture = _picture;
    }

    public byte[] getPicture()
    {
        return picture;
    }

    public String toString()
    {
        return "[\"" + id + "\",\"" + theme + "\",\"" + date + "\",\"" + context + "\"," + null + "]";
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("topic",theme);
        json.put("date",date);
        json.put("description",context);
        json.put("event_id",id);
        return json;
    }

}

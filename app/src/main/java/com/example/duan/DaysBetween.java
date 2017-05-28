package com.example.duan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Jinjin on 2017/3/26.
 */

public class DaysBetween {
    String sdate;
    String edate;
    public void setSdate(String sdate)
    {
        this.sdate=sdate;
    }
    public void setEdate(String edate)
    {
        this.edate=edate;
    }
    public int daysBetween() throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(this.sdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(this.edate));
        long time2 = cal.getTimeInMillis();
        System.out.println(time2);
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }
}

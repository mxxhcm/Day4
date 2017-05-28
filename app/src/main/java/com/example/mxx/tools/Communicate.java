package com.example.mxx.tools;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by mxx on 2017/3/23.
 */
public class Communicate {

    private static final int TIME_OUT = 50*1000;   //超时时间
    private static final String CHARSET = "utf-8"; //设置编码
    private static final String URLPATH = "http://123.206.28.46:8888";
    private static final String FILEURLPATH = "http://123.206.28.46:8888/files";
    private static final int SUCCESS = 1;
    private static final int FAILURE = 0;
    private static final int BYTELENGTH = 100000;


    private static int counter = 0;
    /*
        函数说明：将注册信息发送到服务器
        函数参数:
            Activity context，一个activity的上下文
            String username,用户名
            String password,md5处理后的密码
     */
    public static int Register(Activity context,String username,String password)
    {

        //传送的JSON格式的数据
        JSONObject data = new JSONObject();
        //存储字符串类型的JSON数据
        String content = new String();

        //将JSON字符串的type设置为registration
        try {
            data.put("type","registration");
            data.put("id",username);
            data.put("password",password);
            content = String.valueOf(data);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            //创建http连接
            URL url = new URL(URLPATH);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(TIME_OUT);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("ser_Agent","Fiddler");
            con.setRequestProperty("Content-Type","application/json");

            //这里是向服务器传输信息
            OutputStream os = con.getOutputStream();
            os.write(content.getBytes());
            os.close();

            //这个是服务器传送的状态码
            int code = con.getResponseCode();
            if(code == 200)
            {
                InputStream is = con.getInputStream();
                String jsonstr = NetUtils.readString(is);
                JSONObject json = new JSONObject(jsonstr);
                String detail = json.getString("detail");
                if(detail.equals("Membership '" + username + "' registered successfully!"))
                {
                    Info.setTableName(username);
                    CRUD crud = new CRUD(context);
                    CRUD.createTable(Info.TABLE);
                    Log.v("注册成功！","用户名为" + username);
                    return SUCCESS;
                }
                else
                {
                    Log.v("注测失败！","Sorry!");
                    return FAILURE;
                }
            }
            else
            {
                Log.v("注测失败！","Sorry!");
                return FAILURE;
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.v("注测失败！","Sorry!");
        return FAILURE;
    }

    /*
    函数说明：以某个用户名登录，
    函数参数:
        Activity context，一个activity的上下文
        String username,用户名
        String password,md5处理后的密码
    */
    public static int Login(Activity context,String username,String password) {

        JSONObject data = new JSONObject();
        String content = new String();

        //将JSON字符串的type设置为check_id
        try {
            data.put("type","check_id");
            data.put("id",username);
            data.put("password",password);
            content = String.valueOf(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            //创建http连接
            URL url = new URL(URLPATH);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(TIME_OUT);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("ser_Agent","Fiddler");
            con.setRequestProperty("Content-Type","application/json");

            //这里是向服务器传输信息
            OutputStream os = con.getOutputStream();
            os.write(content.getBytes());
            os.close();

            int code = con.getResponseCode();
            if(code == 200)
            {
                InputStream is = con.getInputStream();
                String jsonstr = NetUtils.readString(is);
                JSONObject json = new JSONObject(jsonstr);
                String detail = json.getString("detail");
                if(detail.equals("1"))
                {

                    //将表名设置为当前用户的名字
                    Info.setTableName(username);
                    //删除原来的表
                    new CRUD(context);
                    CRUD.createTable(Info.TABLE);
                    CRUD.clearTableData(Info.TABLE);
                    if(new Communicate().Download(context,username)==1)
                    {
                        Log.v("登录成功！",username);
                        return SUCCESS;
                    }
                    else {
                        Log.v("登录失败！",username);
                        return FAILURE;
                    }
                }
                else
                {
                    Log.v("登录失败！",username);
                    return FAILURE;
                }
            }
            else
            {
                Log.v("登录失败！",username);
                return FAILURE;
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.v("登录失败！",username);
        return FAILURE;
    }

    /*
    函数说明：下拉刷新的逻辑实现，将原来的数据表删除，然后将服务器的数据下载下来存放到本地数据。
    函数参数:
        Activity context，一个activity的上下文
        String username,用户名
    */

    //更改，去掉static
    public int Download(Activity context,String username) {

        //传送的JSON数据格式
        JSONObject data = new JSONObject();
        String content = "";
        counter = 0;

        //存放下载下来的所有数据
        ArrayList<Info> allinfo = new ArrayList<Info>();

        //将JSON字符串的type设置为search_events
        try {
            data.put("type","search_events");
            data.put("id",username);
            content = String.valueOf(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            //创建http连接
            URL url = new URL(URLPATH);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(TIME_OUT);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("ser_Agent","Fiddler");
            con.setRequestProperty("Content-Type","application/json");

            //这里是向服务器传输信息
            OutputStream os = con.getOutputStream();
            os.write(content.getBytes());
            os.close();

            //服务器返回的状态码
            int code = con.getResponseCode();
            if(code == 200)
            {
                InputStream is = con.getInputStream();
                String jsonstr = NetUtils.readString(is);
                JSONObject json = new JSONObject(jsonstr);
                String detail = json.getString("detail");

                JSONArray jsonarray = new JSONArray(detail);
                //将下载下来的内容存放到一个ArrayList之中
                for(int i = 0 ; i < jsonarray.length();++i)
                {
                    Info info = new Info();

                    String temp = new String(jsonarray.getString(i));
                    JSONObject tempjson = new JSONObject(temp);
                    info.setDate(tempjson.getString("date"));
                    info.setTheme(tempjson.getString("topic"));
                    info.setId(Integer.parseInt(tempjson.getString("event_id")));
                    info.setContext(tempjson.getString("description"));
                    allinfo.add(info);
                }

            }
            else
            {
                Log.v("更新失败!","");
                return FAILURE;
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            Log.v("更新失败!","");
            return FAILURE;
        }

        //将表名设置为当前的用户名
        Info.setTableName(username);
        //清除原来表中的数据
        new CRUD(context);
        CRUD.createTable(Info.TABLE);
        CRUD.clearTableData(Info.TABLE);

        //单独下载每个事件的图片内容
        for(int i = 0 ; i < allinfo.size();++i)
        {
            int id = allinfo.get(i).getId();
            String filename = username + id;
            //byte[] bytes = Communicate.DownloadFile(filename);
            new DownloadThread(filename,allinfo.get(i)).start();

        }

        while(counter != allinfo.size());

        //然后将下载后的数据插入到表中

        Log.v("更新成功！","成功！！");
        Toast.makeText(context, "更新本地成功",
                Toast.LENGTH_SHORT).show();
        return SUCCESS;
    }

    /*
    函数说明：上传到服务器的逻辑实现，先从本地数据库取出数据，然后将数据上传到服务器
    函数参数:
        Activity context，一个activity的上下文
        String username,用户名
     */

    //更改，这里讲static去掉
    public int Upload(Activity context,String username) {

        counter = 0;

        //先从本地数据库中取出数据
        ArrayList<Info>allinfo = CRUD.getInfoList();
        //传送的JSON数据格式
        JSONObject data = new JSONObject();
        JSONArray arraydata = new JSONArray();
        String content = new String();

        //这里需要把数据提取出来转换成为Json格式，再转化为字符串
        try {
            data.put("type", "update_events");
            data.put("id", username);
            for (int i = 0; i < allinfo.size(); ++i) {
                arraydata.put(allinfo.get(i).toJSON());
            }
            data.put("detail", arraydata);
            content = String.valueOf(data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //创建http连接
            URL url = new URL(URLPATH);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("ser_Agent", "Fiddler");
            con.setRequestProperty("Content-Type", "application/json");

            //这里是向服务器传输信息
            OutputStream os = con.getOutputStream();
            os.write(content.getBytes());
            os.close();

            //这个是服务器传送的状态码
            int code = con.getResponseCode();
            if (code == 200) {
                InputStream is = con.getInputStream();
                String jsonstr = NetUtils.readString(is);
                JSONObject json = new JSONObject(jsonstr);

                String detail = json.getString("detail");
                if (detail.equals("Events deleted successfully!, Events added successfully!")) {
                    Log.v("上传文字数据成功","" + counter);
                }
                else
                {
                    Log.v("上传失败","!");
                    return FAILURE;
                }
            }
            else
            {
                Log.v("上传失败","!");
                return FAILURE;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.v("上传失败","!");

            return FAILURE;
        }

        //这里是单独将每张图片上传到网上。
        for(int j = 0; j < allinfo.size(); ++j)
        {
            String pictureid = username + allinfo.get(j).getId();
            byte[] bytes = allinfo.get(j).getPicture();
            new UploadThread(bytes,pictureid).start();

            //Communicate.UploadFile(bytes,pictureid);
        }

        while(counter != allinfo.size());
        Log.v("上传图片数据成功","" + counter);
        Toast.makeText(context, "上传所有数据成功",
                Toast.LENGTH_SHORT).show();
        return SUCCESS;
    }

    /*
         函数功能:上传单个文件到服务器
         函数参数
            byte[] bytes，存放文件的内容
            String filename,存放文件的名字
     */
    public static int UploadFile(byte[] bytes,String filename)
    {

        //自己构造HTTP请求头的一些内容
        String BOUNDARY = UUID.randomUUID().toString();//边界标识符
        String PREFIX = "--",LINE_END = "\r\n";         //前缀和后缀
        String CONTENT_TYPE = "multipart/form-data";    //文件内容类型

        try {
            //设置HTTP请求的一些参数
            URL url = new URL(FILEURLPATH);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setConnectTimeout(TIME_OUT);    //连接时间
            con.setReadTimeout(TIME_OUT);       //
            con.setDoOutput(true);              //允许输出流
            con.setDoInput(true);               //允许输入流
            con.setUseCaches(false);            //不允许缓存
            con.setRequestMethod("POST");       //请求方式
            con.setRequestProperty("Charset",CHARSET);
            con.setRequestProperty("Connection","keep-alive");
            con.setRequestProperty("Content-Type",CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if(bytes != null)
            {
                OutputStream outputStream = con.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputStream);
                StringBuffer sb = new StringBuffer();
                //构造HTTP请求的头部
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition: form-data; name = \"file\"; filename=\""+filename+"\""+LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
                sb.append(LINE_END);

                //写http头
                dos.write(sb.toString().getBytes());
                //写文件内容
                dos.write(bytes,0,bytes.length);
                //在文件
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX+BOUNDARY + PREFIX +LINE_END).getBytes();
                dos.write(end_data);

                dos.flush();
                int code = con.getResponseCode();
                if(code == 200)
                {
                    Log.v("Success!","上传图片" + filename + "成功");
                    synchronized (Communicate.class) {
                        counter++;
                    }
                    return SUCCESS;
                }
                else
                {
                    Log.v("FAILED!","上传图片" + filename + "失败");
                    return FAILURE;
                }
            }

        }catch (Exception e)
        {
            //抛出异常
            e.printStackTrace();
        }
        Log.v("FAILED!","上传图片" + filename + "失败");
        return FAILURE;
    }



    /*
        函数功能:从服务器下载指定指定文件名的单个文件。
        参数:filename 文件名称
     */
    private static byte[] DownloadFile(String filename,Info info)
    {
        //bytes存放从服务器下载下来的文件的字节流
        byte[] bytes = new byte[BYTELENGTH];
        try {
            //创建http连接

            //请求url的路径
            String urlPath = FILEURLPATH + "/"+filename;
            URL url = new URL(urlPath);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setConnectTimeout(TIME_OUT);
            con.setRequestMethod("GET");

            //这个是服务器传送的状态码
            int code = con.getResponseCode();
            //状态码为200代表服务器接收到了该请求。
            if(code == 200)
            {
                //从服务器接收数据
                InputStream is = con.getInputStream();
                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024]; // 用数据装
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    outstream.write(buffer, 0, len);
                }
                outstream.close();
                // 关闭流一定要记得。
                Log.v("Success!","下载图片" + filename + "成功");
                //这里是插入这个记录
                info.setPicture(outstream.toByteArray());
                int succ = CRUD.insert(info);
                if(succ == SUCCESS) {
                    synchronized (Communicate.class) {
                        counter++;
                    }
                    Log.v("" + counter + "更新成功", "！");
                }
                else
                {
                    Log.v("" + counter + "更新失败", "！");
                }
            }
            else//否则就弹出下载失败??
            {
                Log.v("Failed!","下载图片" + filename + "失败");
            }

        }catch (Exception e)
        {
            //如果联网从服务器下载过程中有错误，抛出异常
            Log.v("Failed!","下载图片" + filename + "失败");
            e.printStackTrace();
        }

        return bytes;
    }

    //上传线程
    private class UploadThread extends Thread
    {
        private byte[] picture;
        private String fileName;

        public UploadThread(byte[] bytes,String _fileName)
        {
            picture = bytes;
            fileName = _fileName;

        }

        public void run()
        {
            Communicate.UploadFile(picture,fileName);
        }
    }

    //下载线程
    private class DownloadThread extends Thread
    {
        private String fileName;
        private Info info;
        public DownloadThread(String _fileName,Info _info)
        {
            fileName = _fileName;
            info = _info;
        }

        public void run()
        {
            Communicate.DownloadFile(fileName,info);
        }
    }

}
